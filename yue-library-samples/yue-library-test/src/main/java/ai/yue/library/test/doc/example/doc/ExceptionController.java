package ai.yue.library.test.doc.example.doc;

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
	
}
