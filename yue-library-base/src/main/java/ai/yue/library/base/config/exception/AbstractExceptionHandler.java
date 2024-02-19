package ai.yue.library.base.config.exception;

import ai.yue.library.base.exception.*;
import ai.yue.library.base.util.ExceptionUtils;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.exceptions.ValidateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.io.IOException;

/**
 * 全局统一异常处理
 * 
 * @author	ylyue
 * @since	2017年10月8日
 */
@Slf4j
public abstract class AbstractExceptionHandler {
	
    @PostConstruct
    private void init() {
    	log.info("【初始化配置-全局统一异常处理】拦截所有Controller层异常，返回HTTP请求最外层对象 ... 已初始化完毕。");
    }
    
    // RESTful 异常拦截
    
	/**
	 * 异常结果处理-synchronized
	 * 
	 * @param e 结果异常
	 * @return 结果
	 */
	@ResponseBody
    @ExceptionHandler(ResultException.class)
	public abstract Result<?> resultExceptionHandler(ResultException e);
	
    /**
	 * 拦截登录异常（User）-401
	 * @param e 登录异常
     * @return 结果
	 */
    @ResponseBody
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(LoginException.class)
	public Result<?> loginExceptionHandler(LoginException e) {
    	ExceptionUtils.printException(e);
    	return R.unauthorized();
	}
    
    /**
     * 非法请求异常拦截-402
     * @param e 非法请求异常
     * @return 结果
	 */
    @ResponseBody
    @ResponseStatus(code = HttpStatus.PAYMENT_REQUIRED)
    @ExceptionHandler(AttackException.class)
	public Result<?> attackExceptionHandler(AttackException e) {
    	ExceptionUtils.printException(e);
    	return R.attack(e.getMessage());
	}
    
    /**
     * 无权限异常访问处理-403
     * @param e 无权限异常
     * @return 结果
	 */
    @ResponseBody
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
	public Result<?> forbiddenExceptionHandler(ForbiddenException e) {
    	ExceptionUtils.printException(e);
    	return R.forbidden();
	}
    
    /**
	 * 拦截API接口版本弃用异常-410
	 * 
	 * @param e API接口版本弃用异常
     * @return 结果
	 */
    @ResponseBody
    @ResponseStatus(code = HttpStatus.GONE)
    @ExceptionHandler(ApiVersionDeprecatedException.class)
	public Result<?> apiVersionDeprecatedExceptionHandler(ApiVersionDeprecatedException e) {
    	ExceptionUtils.printException(e);
    	return R.gone();
	}
    
    /**
	 * 参数效验为空统一处理-432
	 * @return 结果
	 */
	@ResponseBody
	@ExceptionHandler(ParamVoidException.class)
	public abstract Result<?> paramVoidExceptionHandler();
    
    /**
	 * 参数效验未通过统一处理-433
	 * @param e 参数校验未通过异常
	 * @return 结果
	 */
    @ResponseBody
    @ExceptionHandler(ParamException.class)
	public abstract Result<?> paramExceptionHandler(ParamException e);
    
    /**
	 * {@linkplain Valid} 验证异常统一处理-433
	 * @param e 验证异常
	 * @return 结果
	 */
    @ResponseBody
    @ExceptionHandler(BindException.class)
	public abstract Result<?> bindExceptionHandler(BindException e);
    
    /**
	 * 验证异常统一处理-433
	 * @param e 验证异常
	 * @return 结果
	 */
    @ResponseBody
    @ExceptionHandler(ValidateException.class)
	public abstract Result<?> validateExceptionHandler(ValidateException e);
    
	/**
	 * 解密异常统一处理-435
	 * 
	 * @param e 解密异常
	 * @return 结果
	 */
    @ResponseBody
    @ExceptionHandler(ParamDecryptException.class)
	public abstract Result<?> paramDecryptExceptionHandler(ParamDecryptException e);
    
    /**
     * DB异常统一处理-506
     * @param e DB异常
     * @return 结果
	 */
    @ResponseBody
    @ResponseStatus(code = HttpStatus.VARIANT_ALSO_NEGOTIATES)
    @ExceptionHandler(DbException.class)
	public Result<?> dbExceptionHandler(DbException e) {
		e.printStackTrace();
		if (e.isShowMsg()) {
			return R.dbError(e.getMessage());
		}
		
		return R.dbError();
	}
    
	/**
	 * 服务降级-507
	 * @param e 服务降级异常
	 * @return 结果
	 */
	@ResponseBody
	@ResponseStatus(code = HttpStatus.INSUFFICIENT_STORAGE)
    @ExceptionHandler(ClientFallbackException.class)
	public Result<?> clientFallbackExceptionHandler(ClientFallbackException e) {
		ExceptionUtils.printException(e);
		return R.clientFallback();
	}
	
    /**
     * 类型转换异常统一处理-509
     * 
     * @param e 转换异常
     * @return 结果
     */
    @ResponseBody
    @ResponseStatus(code = HttpStatus.BANDWIDTH_LIMIT_EXCEEDED)
    @ExceptionHandler(ConvertException.class)
	public Result<?> convertExceptionHandler(ConvertException e) {
    	log.error("【类型转换异常】转换类型失败，错误信息如下：{}", e.getMessage());
    	ExceptionUtils.printException(e);
    	return R.typeConvertError(e.getMessage());
	}
    
    // WEB 异常拦截
    
	/**
	 * 拦截登录异常（Admin）-301
	 * 
	 * @param e 认证异常
	 * @throws IOException 重定向失败
	 */
    @ExceptionHandler(AuthorizeException.class)
	public abstract void authorizeExceptionHandler(AuthorizeException e) throws IOException;
    
}
