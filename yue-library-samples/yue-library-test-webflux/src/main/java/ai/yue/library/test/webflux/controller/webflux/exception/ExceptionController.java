package ai.yue.library.test.webflux.controller.webflux.exception;

import ai.yue.library.base.exception.ParamException;
import ai.yue.library.base.view.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author	ylyue
 * @since	2019年10月12日
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/exception")
public class ExceptionController {

	@PostMapping("/exception")
	public Result<?> exception() {
		throw new ParamException("异常测试");
	}

}
