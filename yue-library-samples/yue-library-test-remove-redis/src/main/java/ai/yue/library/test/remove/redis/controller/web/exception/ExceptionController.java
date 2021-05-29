package ai.yue.library.test.remove.redis.controller.web.exception;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.yue.library.base.exception.ParamException;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;

/**
 * @author	ylyue
 * @since	2019年10月12日
 */
@RestController
@RequestMapping("/exception")
public class ExceptionController {

	/**
	 * exception
	 * 
	 * @return
	 */
	@PostMapping("/exception")
	public Result<?> exception() {
		throw new ParamException("异常测试");
	}
	
	/**
	 * exception
	 * 
	 * @return
	 */
	@PostMapping("/clientFallback")
	public Result<?> clientFallback() {
		return R.clientFallback();
	}
	
}
