package ai.yue.library.webflux.config.exception;

import ai.yue.library.base.config.exception.AbstractExceptionHandler;
import ai.yue.library.base.exception.AuthorizeException;
import ai.yue.library.base.util.ExceptionUtils;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import com.alibaba.fastjson2.JSONObject;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.v7.core.text.StrUtil;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
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
	
    // RESTful 异常拦截

	/**
	 * {@linkplain Valid} 验证异常统一处理-433
	 * 
	 * @param e 验证异常
	 * @return 结果
	 */
	@Override
    @ResponseBody
    @ExceptionHandler(BindException.class)
	public Result<?> bindExceptionHandler(BindException e) {
//    	String uri = ServletUtils.getRequest().getRequestURI();
//    	Console.error("uri={}", uri);
		List<ObjectError> errors = e.getAllErrors();
		JSONObject paramHint = new JSONObject();
		errors.forEach(error -> {
			String str = StrUtil.subAfter(error.getArguments()[0].toString(), "[", true);
			String key = str.substring(0, str.length() - 1);
			String msg = error.getDefaultMessage();
			paramHint.put(key, msg);
			log.error(key + " " + msg);
		});
		
		return R.paramCheckNotPass(paramHint.toString());
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
    	log.error("WebFlux 暂不支持异常拦截后重定向操作 {}", ExceptionUtils.getPrintExceptionToStr(e));
//    	ServletUtils.getResponse().sendRedirect("");
	}
    
}
