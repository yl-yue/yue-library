package ai.yue.library.web.config.exception;

import ai.yue.library.base.constant.Constant;
import ai.yue.library.base.util.I18nUtils;
import ai.yue.library.base.view.Result;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

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

		/*
		 * 2. 设置i18n msg
		 *   - code >= 600 需调用 R.errorPromptI18n() 方法
		 *   - 此处统一翻译 code < 600 的 msg
		 */
		Integer code = body.getCode();
		if (code != null && code >= 200 && code < 600) {
			body.setMsg(I18nUtils.getYue(body.getMsg()));
		}

		// 3. 设置链路ID
		body.setTraceId(MDC.get(Constant.TRACE_ID));

		// 4. 响应结果
		return body;
	}

}
