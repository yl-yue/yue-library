package ai.yue.library.test.controller.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.ResultInfo;

/**
 * @author	ylyue
 * @since	2019年8月9日
 */
@RestController
@RequestMapping("/test")
public class TestController {

	@GetMapping("/get")
	public Result<?> get() {
		return ResultInfo.success();
	}
	
}
