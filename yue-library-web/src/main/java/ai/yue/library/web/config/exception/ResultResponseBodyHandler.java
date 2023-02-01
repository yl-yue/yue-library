package ai.yue.library.web.config.exception;

import ai.yue.library.base.view.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * 响应结果处理器
 * <p>标准HTTP状态码
 * 
 * @author	ylyue
 * @since	2020年9月18日
 */
@Slf4j
@ControllerAdvice
public class ResultResponseBodyHandler<T> implements ResponseBodyAdvice<T> {

	MessageSource messageSource;

	{
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("YueMessages");
		messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
		this.messageSource = messageSource;
	}

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		if (returnType.getMethod().getReturnType() != Result.class) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public T beforeBodyWrite(T body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		Integer code = body == null ? null : ((Result<?>) body).getCode();
		((Result<?>) body).setMsg(get(((Result<?>) body).getMsg()));
		HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
		int status = servletResponse.getStatus();
		if (code != null && code != status) {
			servletResponse.setStatus(code);
		}
		
		return body;
	}

	/**
	 * 获取单个国际化翻译值
	 */
	private String get(String msgKey) {
		try {
			return messageSource.getMessage(msgKey, null, LocaleContextHolder.getLocale());
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				e.printStackTrace();
			}
			return msgKey;
		}
	}

}
