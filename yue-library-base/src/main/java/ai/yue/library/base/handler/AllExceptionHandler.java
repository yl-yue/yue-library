package ai.yue.library.base.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import ai.yue.library.base.exception.AttackException;
import ai.yue.library.base.exception.AuthorizeException;
import ai.yue.library.base.exception.ClientFallbackException;
import ai.yue.library.base.exception.DBException;
import ai.yue.library.base.exception.DecryptException;
import ai.yue.library.base.exception.ForbiddenException;
import ai.yue.library.base.exception.JSONObjectException;
import ai.yue.library.base.exception.LoginException;
import ai.yue.library.base.exception.ParamException;
import ai.yue.library.base.exception.ParamVoidException;
import ai.yue.library.base.exception.ResultException;
import ai.yue.library.base.util.ExceptionUtils;
import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.ResultInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * @author  孙金川
 * @version 创建时间：2017年10月8日
 */
@Slf4j
public abstract class AllExceptionHandler {
	
	/**
	 * 拦截所有未处理异常
	 * @param e 异常
	 * @return 结果
	 */
	@ResponseBody
    @ExceptionHandler(Exception.class)
	public Result<?> exceptionHandler(Exception e) {
    	e.printStackTrace();
    	return ResultInfo.error();
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
	 * JSON格式字符串解析异常统一处理
	 * @param e JSON格式字符串解析异常
	 * @return 结果
	 */
    @ResponseBody
    @ExceptionHandler(JSONObjectException.class)
	public Result<?> jsonObjectExceptionHandler(JSONObjectException e) {
    	log.error("【JSONObject错误】格式化JSON字符串失败，错误信息如下：{}", e.getMessage());
    	ExceptionUtils.printException(e);
		return ResultInfo.json_parse_error();
	}
    
    /**
	 * 解密异常统一处理
	 * @param e 解密异常
	 * @return 结果
	 */
    @ResponseBody
    @ExceptionHandler(DecryptException.class)
	public Result<?> decryptExceptionHandler(DecryptException e) {
    	log.error("【解密错误】错误信息如下：{}", e.getMessage());
    	ExceptionUtils.printException(e);
		return ResultInfo.decrypt_error();
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
    	HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		response.sendRedirect("");
	}
    
}
