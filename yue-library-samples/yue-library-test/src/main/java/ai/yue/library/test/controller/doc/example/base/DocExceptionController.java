package ai.yue.library.test.controller.doc.example.base;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.yue.library.base.exception.ParamException;
import ai.yue.library.base.view.Result;

/**
 * @author	ylyue
 * @since	2019年10月12日
 */
@RestController
@RequestMapping("/docException")
public class DocExceptionController {

	/**
	 * exception
	 * 
	 * @return
	 */
	@PostMapping("/exception")
	public Result<?> exception() {
		throw new ParamException("异常测试");
	}
	
}
