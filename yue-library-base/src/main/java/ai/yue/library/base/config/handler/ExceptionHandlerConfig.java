package ai.yue.library.base.config.handler;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.exception.AttackException;
import ai.yue.library.base.exception.AuthorizeException;
import ai.yue.library.base.exception.ClientFallbackException;
import ai.yue.library.base.exception.DBException;
import ai.yue.library.base.exception.ForbiddenException;
import ai.yue.library.base.exception.LoginException;
import ai.yue.library.base.exception.ParamDecryptException;
import ai.yue.library.base.exception.ParamException;
import ai.yue.library.base.exception.ParamVoidException;
import ai.yue.library.base.exception.ResultException;
import ai.yue.library.base.util.ExceptionUtils;
import ai.yue.library.base.util.servlet.ServletUtils;
import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.ResultInfo;
import cn.hutool.core.convert.ConvertException;
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
@EnableConfigurationProperties(ExceptionHandlerProperties.class)
@ConditionalOnProperty(prefix = "yue.exception-handler", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ExceptionHandlerConfig {
	
    @PostConstruct
    private void init() {
    	log.info("【初始化配置-全局统一异常处理】拦截所有Controller层异常，返回HTTP请求最外层对象 ... 已初始化完毕。");
    }
	
	/**
	 * 拦截所有未处理异常
	 * @param e 异常
	 * @return 结果
	 */
	@ResponseBody
    @ExceptionHandler(Exception.class)
	public Result<?> exceptionHandler(Exception e) {
    	e.printStackTrace();
    	return ResultInfo.error(e.toString());
	}
	
	/**
	 * 异常结果处理
	 * @param e 结果异常
	 * @return 结果
	 */
	@ResponseBody
    @ExceptionHandler(ResultException.class)
	public synchronized Result<?> resultExceptionHandler(ResultException e) {
		var result = e.getResult();
    	log.error(result.toString());
    	ExceptionUtils.printException(e);
    	return result;
	}
	
	/**
	 * 服务降级
	 * @param e 服务降级异常
	 * @return 结果
	 */
	@ResponseBody
    @ExceptionHandler(ClientFallbackException.class)
	public Result<?> clientFallbackExceptionHandler(ClientFallbackException e) {
		ExceptionUtils.printException(e);
		return ResultInfo.client_fallback();
	}
	
    /**
     * 非法请求异常拦截
     * @param e 非法请求异常
     * @return 结果
	 */
    @ResponseBody
    @ExceptionHandler(AttackException.class)
	public Result<?> attackExceptionHandler(AttackException e) {
    	ExceptionUtils.printException(e);
    	return ResultInfo.attack(e.getMessage());
	}
    
    /**
	 * 参数效验为空统一处理
	 * @return 结果
	 */
	@ResponseBody
	@ExceptionHandler(ParamVoidException.class)
	public Result<?> paramVoidExceptionHandler() {
		return ResultInfo.param_void();
	}
    
    /**
	 * 参数效验未通过统一处理
	 * @param e 参数校验未通过异常
	 * @return 结果
	 */
    @ResponseBody
    @ExceptionHandler(ParamException.class)
	public Result<?> paramExceptionHandler(ParamException e) {
    	ExceptionUtils.printException(e);
		return ResultInfo.param_check_not_pass(e.getMessage());
	}
    
    /**
	 * {@linkplain Valid} 验证异常统一处理
	 * @param e 验证异常
	 * @return 结果
	 */
    @ResponseBody
    @ExceptionHandler(BindException.class)
	public Result<?> bindExceptionHandler(BindException e) {
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
		
		return ResultInfo.param_check_not_pass(paramHint.toString());
	}
    
    /**
	 * 验证异常统一处理
	 * @param e 验证异常
	 * @return 结果
	 */
    @ResponseBody
    @ExceptionHandler(ValidateException.class)
	public Result<?> validateExceptionHandler(ValidateException e) {
    	ExceptionUtils.printException(e);
		return ResultInfo.param_check_not_pass(e.getMessage());
	}
    
    /**
     * 类型转换异常统一处理
     * 
     * @param e 转换异常
     * @return 结果
     */
    @ResponseBody
    @ExceptionHandler(ConvertException.class)
	public Result<?> convertExceptionHandler(ConvertException e) {
    	log.error("【类型转换异常】转换类型失败，错误信息如下：{}", e.getMessage());
    	ExceptionUtils.printException(e);
    	return ResultInfo.type_convert_error(e.getMessage());
	}
    
    /**
	 * 解密异常统一处理
	 * @param e 解密异常
	 * @return 结果
	 */
    @ResponseBody
    @ExceptionHandler(ParamDecryptException.class)
	public Result<?> paramDecryptExceptionHandler(ParamDecryptException e) {
    	log.error("【解密错误】错误信息如下：{}", e.getMessage());
    	ExceptionUtils.printException(e);
		return ResultInfo.param_decrypt_error();
	}
    
    /**
     * DB异常统一处理
     * @param e DB异常
     * @return 结果
	 */
    @ResponseBody
    @ExceptionHandler(DBException.class)
	public Result<?> dbExceptionHandler(DBException e) {
    	e.printStackTrace();
    	return ResultInfo.db_error();
	}
    
    /**
     * 无权限异常访问处理
     * @param e 无权限异常
     * @return 结果
	 */
    @ResponseBody
    @ExceptionHandler(ForbiddenException.class)
	public Result<?> forbiddenExceptionHandler(ForbiddenException e) {
    	ExceptionUtils.printException(e);
    	return ResultInfo.forbidden();
	}
    
    /**
	 * 拦截登录异常（User）
	 * @param e 登录异常
     * @return 结果
	 */
    @ResponseBody
    @ExceptionHandler(LoginException.class)
	public Result<?> loginExceptionHandler(LoginException e) {
    	ExceptionUtils.printException(e);
    	return ResultInfo.unauthorized();
	}
    
    /**
	 * 拦截登录异常（Admin）
	 * @param e 认证异常
	 * @throws IOException 重定向失败
	 */
    @ExceptionHandler(AuthorizeException.class)
	public void authorizeExceptionHandler(AuthorizeException e) throws IOException {
    	ExceptionUtils.printException(e);
		HttpServletResponse response = ServletUtils.getResponse();
		response.sendRedirect("");
	}
    
}
