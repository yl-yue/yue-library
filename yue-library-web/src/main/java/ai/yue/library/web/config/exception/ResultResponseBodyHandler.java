package ai.yue.library.web.config.exception;

import ai.yue.library.base.constant.Constant;
import ai.yue.library.base.view.Result;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
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
public class ResultResponseBodyHandler implements ResponseBodyAdvice<Result> {

	MessageSource messageSource;

	{
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasenames("messages", "YueMessages");
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
	public Result beforeBodyWrite(@Nullable Result body, MethodParameter returnType, MediaType selectedContentType,
								  Class<? extends HttpMessageConverter<?>> selectedConverterType,
								  ServerHttpRequest request, ServerHttpResponse response) {
		// 1. 处理参数
		if (body == null) {
			return null;
		}
		Integer code = body.getCode();
		String msg = get(body.getMsg());
		String traceId = MDC.get(Constant.TRACE_ID);

		// 2. 设置HTTP状态码
		if (code != null) {
			HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
			int status = servletResponse.getStatus();
			if (code != status) {
				servletResponse.setStatus(code);
			}
		}

		// 3. 设置i18n msg
		body.setMsg(msg);

		// 4. 设置链路ID
		body.setTraceId(traceId);

		// 5. 响应结果
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
