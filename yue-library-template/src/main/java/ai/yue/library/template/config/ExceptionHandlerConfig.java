package ai.yue.library.template.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import ai.yue.library.base.handler.AllExceptionHandler;
import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.ResultInfo;

/**
 * @author  孙金川
 * @version 创建时间：2018年6月12日
 */
@ControllerAdvice
public class ExceptionHandlerConfig extends AllExceptionHandler{

	/**
	 * 拦截所有未处理异常
	 * @return
	 */
	@Override
	@ResponseBody
    @ExceptionHandler(Exception.class)
	public Result<?> exceptionHandler(Exception e) {
    	e.printStackTrace();
    	return ResultInfo.error(e.toString());
	}
	
}
