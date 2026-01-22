package ai.yue.library.web.config.exception;

import ai.yue.library.base.config.exception.AbstractExceptionHandler;
import ai.yue.library.base.exception.AuthorizeException;
import ai.yue.library.base.util.ExceptionUtils;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.web.util.ServletUtils;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.v7.core.reflect.ClassUtil;
import cn.hutool.v7.core.reflect.method.MethodUtil;
import cn.hutool.v7.core.text.StrUtil;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

/**
 * 全局统一异常处理
 * 
 * @author	ylyue
 * @since	2017年10月8日
 */
@Slf4j
@ControllerAdvice
public class ResultExceptionHandler extends AbstractExceptionHandler {

	private static final String VIOLATION_FIELD_ERROR_CLASS_NAME = "org.springframework.validation.beanvalidation.SpringValidatorAdapter$ViolationFieldError";
	private static final String GET_REJECTED_VALUE_METHOD_NAME = "getRejectedValue";

    // RESTful 异常拦截
	
	/**
	 * 方法不允许（Method Not Allowed）-405
	 * <p>客户端使用服务端不支持的 Http Request Method 进行接口调用
	 * 
     * @param e 方法不允许异常
     * @return 结果
	 */
	@ResponseBody
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public Result<?> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
    	String uri = ServletUtils.getRequest().getRequestURI();
		log.error("【方法不允许】uri={}, 错误信息如下：{}", uri, ExceptionUtils.getPrintExceptionToStr(e));
		return R.methodNotAllowed(e.getMessage());
	}

    /**
	 * {@linkplain Valid} 验证异常统一处理-433
	 * @param e 验证异常
	 * @return 结果
	 */
	@Override
    @ResponseBody
    @ExceptionHandler(BindException.class)
	public Result<?> bindExceptionHandler(BindException e) {
		List<ObjectError> errors = e.getAllErrors();
		JSONArray errorHints = new JSONArray();
		errors.forEach(error -> {
			String str = StrUtil.subAfter(error.getArguments()[0].toString(), "[", true);
			String errorKey = str.substring(0, str.length() - 1);
			String errorHintMsg = error.getDefaultMessage();
			Object errorValue = null;
			String className = ClassUtil.getClassName(error.getClass(), false);
			if (VIOLATION_FIELD_ERROR_CLASS_NAME.equals(className)) {
				errorValue = MethodUtil.invoke(error, GET_REJECTED_VALUE_METHOD_NAME);
			}

			JSONObject errorHint = new JSONObject();
			errorHint.put("errorKey", errorKey);
			errorHint.put("errorHintMsg", errorHintMsg);
			errorHint.put("errorValue", errorValue);
			errorHints.add(errorHint);
		});

		String uri = ServletUtils.getRequest().getRequestURI();
		log.error("BindException: uri={}, errorHints={}", uri, errorHints);
		return R.paramCheckNotPass(errorHints);
	}
    
    // WEB 异常拦截
    
	/**
	 * 拦截登录异常（Admin）-301
	 * 
	 * @param e 认证异常
	 * @throws IOException 重定向失败
	 */
	@Override
    @ExceptionHandler(AuthorizeException.class)
	public void authorizeExceptionHandler(AuthorizeException e) throws IOException {
		log.error("【登录异常】{}", ExceptionUtils.getPrintExceptionToStr(e));
		ServletUtils.getResponse().sendRedirect("");
	}
    
}
