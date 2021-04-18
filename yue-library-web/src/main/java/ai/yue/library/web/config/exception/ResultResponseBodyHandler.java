package ai.yue.library.web.config.exception;

import ai.yue.library.base.view.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletResponse;

/**
 * 响应结果处理器
 * <p>标准HTTP状态码
 * 
 * @author	ylyue
 * @since	2020年9月18日
 */
@ControllerAdvice
public class ResultResponseBodyHandler<T> implements ResponseBodyAdvice<T> {

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
		HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
		int status = servletResponse.getStatus();
		if (code != null && code != status) {
			servletResponse.setStatus(code);
		}
		
		return body;
	}

}
