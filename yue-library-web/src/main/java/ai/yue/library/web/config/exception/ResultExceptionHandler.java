package ai.yue.library.web.config.exception;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.config.exception.AbstractExceptionHandler;
import ai.yue.library.base.exception.AuthorizeException;
import ai.yue.library.base.exception.ParamDecryptException;
import ai.yue.library.base.exception.ParamException;
import ai.yue.library.base.exception.ParamVoidException;
import ai.yue.library.base.exception.ResultException;
import ai.yue.library.base.util.ExceptionUtils;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.web.util.servlet.ServletUtils;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 全局统一异常处理
 * 
 * @author	ylyue
 * @since	2017年10月8日
 */
@Slf4j
@ControllerAdvice
@ConditionalOnProperty(prefix = "yue.exception-handler", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ResultExceptionHandler extends AbstractExceptionHandler {
	
    // Restful 异常拦截
	
	/**
	 * 异常结果处理-synchronized
	 * 
	 * @param e 结果异常
	 * @return 结果
	 */
	@Override
	@ResponseBody
	@ExceptionHandler(ResultException.class)
	public synchronized Result<?> resultExceptionHandler(ResultException e) {
		var result = e.getResult();
		ServletUtils.getResponse().setStatus(result.getCode());
		log.error(result.toString());
		ExceptionUtils.printException(e);
		return result;
	}
	
    /**
	 * 参数效验为空统一处理-432
	 * @return 结果
	 */
	@Override
	@ResponseBody
	@ExceptionHandler(ParamVoidException.class)
	public Result<?> paramVoidExceptionHandler() {
		ServletUtils.getResponse().setStatus(432);
		return R.paramVoid();
	}
    
    /**
	 * 参数效验未通过统一处理-433
	 * @param e 参数校验未通过异常
	 * @return 结果
	 */
	@Override
    @ResponseBody
    @ExceptionHandler(ParamException.class)
	public Result<?> paramExceptionHandler(ParamException e) {
    	ServletUtils.getResponse().setStatus(433);
    	ExceptionUtils.printException(e);
		return R.paramCheckNotPass(e.getMessage());
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
		ServletUtils.getResponse().setStatus(433);
    	String uri = ServletUtils.getRequest().getRequestURI();
    	Console.error("uri={}", uri);
		List<ObjectError> errors = e.getAllErrors();
		JSONObject paramHint = new JSONObject();
		errors.forEach(error -> {
			String str = StrUtil.subAfter(error.getArguments()[0].toString(), "[", true);
			String key = str.substring(0, str.length() - 1);
			String msg = error.getDefaultMessage();
			paramHint.put(key, msg);
			Console.error(key + " " + msg);
		});
		
		return R.paramCheckNotPass(paramHint.toString());
	}
    
    /**
	 * 验证异常统一处理-433
	 * @param e 验证异常
	 * @return 结果
	 */
	@Override
    @ResponseBody
    @ExceptionHandler(ValidateException.class)
	public Result<?> validateExceptionHandler(ValidateException e) {
    	ServletUtils.getResponse().setStatus(433);
    	ExceptionUtils.printException(e);
		return R.paramCheckNotPass(e.getMessage());
	}
    
	/**
	 * 解密异常统一处理-435
	 * 
	 * @param e 解密异常
	 * @return 结果
	 */
	@Override
	@ResponseBody
	@ExceptionHandler(ParamDecryptException.class)
	public Result<?> paramDecryptExceptionHandler(ParamDecryptException e) {
		ServletUtils.getResponse().setStatus(435);
		log.error("【解密错误】错误信息如下：{}", e.getMessage());
		ExceptionUtils.printException(e);
		return R.paramDecryptError();
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
		ExceptionUtils.printException(e);
		ServletUtils.getResponse().sendRedirect("");
	}
    
}
