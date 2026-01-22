package ai.yue.library.test.controller.web.exception;

import ai.yue.library.base.exception.ParamException;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.entity.DataAudit;
import ai.yue.library.test.service.DataAuditService;
import cn.dev33.satoken.exception.NotLoginException;
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

	final DataAuditService dataAuditService;

	@PostMapping("/exception")
	public Result<?> exception() {
		throw new ParamException("异常测试");
	}

	@PostMapping("/notloginexception")
	public Result<?> notloginexception(Integer type) {
		if (type == 1) {
			throw NotLoginException.newInstance("异常测试", NotLoginException.NOT_TOKEN, NotLoginException.NOT_TOKEN_MESSAGE, "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOjExNzkxNzE4LCJyblN0ciI6ImV1M2QxOHVnWHRFanVySHNsNTRaVW5aZFBsUkJYVHlYIiwiZXhwIjoxNzI4MTEwOTE1LCJ1c2VybmFtZSI6IjExNzkxNzE4In0.5OCeX0rKX-VXoiaVYvwPX94dm9DAsEFySgvbCH-OQvc");
		}
		if (type == 2) {
			throw NotLoginException.newInstance("异常测试", NotLoginException.INVALID_TOKEN, NotLoginException.INVALID_TOKEN_MESSAGE, "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOjExNzkxNzE4LCJyblN0ciI6ImV1M2QxOHVnWHRFanVySHNsNTRaVW5aZFBsUkJYVHlYIiwiZXhwIjoxNzI4MTEwOTE1LCJ1c2VybmFtZSI6IjExNzkxNzE4In0.5OCeX0rKX-VXoiaVYvwPX94dm9DAsEFySgvbCH-OQvc");
		}
		if (type == 3) {
			throw NotLoginException.newInstance("异常测试", NotLoginException.BE_REPLACED, NotLoginException.BE_REPLACED_MESSAGE, "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOjExNzkxNzE4LCJyblN0ciI6ImV1M2QxOHVnWHRFanVySHNsNTRaVW5aZFBsUkJYVHlYIiwiZXhwIjoxNzI4MTEwOTE1LCJ1c2VybmFtZSI6IjExNzkxNzE4In0.5OCeX0rKX-VXoiaVYvwPX94dm9DAsEFySgvbCH-OQvc");
		}
		if (type == 4) {
			throw NotLoginException.newInstance("异常测试", NotLoginException.KICK_OUT, NotLoginException.KICK_OUT_MESSAGE, "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOjExNzkxNzE4LCJyblN0ciI6ImV1M2QxOHVnWHRFanVySHNsNTRaVW5aZFBsUkJYVHlYIiwiZXhwIjoxNzI4MTEwOTE1LCJ1c2VybmFtZSI6IjExNzkxNzE4In0.5OCeX0rKX-VXoiaVYvwPX94dm9DAsEFySgvbCH-OQvc");
		}
		if (type == 5) {
			throw NotLoginException.newInstance("异常测试", NotLoginException.TOKEN_FREEZE, NotLoginException.TOKEN_FREEZE_MESSAGE, "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOjExNzkxNzE4LCJyblN0ciI6ImV1M2QxOHVnWHRFanVySHNsNTRaVW5aZFBsUkJYVHlYIiwiZXhwIjoxNzI4MTEwOTE1LCJ1c2VybmFtZSI6IjExNzkxNzE4In0.5OCeX0rKX-VXoiaVYvwPX94dm9DAsEFySgvbCH-OQvc");
		}

		throw NotLoginException.newInstance("异常测试", NotLoginException.NO_PREFIX, NotLoginException.NO_PREFIX_MESSAGE, "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOjExNzkxNzE4LCJyblN0ciI6ImV1M2QxOHVnWHRFanVySHNsNTRaVW5aZFBsUkJYVHlYIiwiZXhwIjoxNzI4MTEwOTE1LCJ1c2VybmFtZSI6IjExNzkxNzE4In0.5OCeX0rKX-VXoiaVYvwPX94dm9DAsEFySgvbCH-OQvc");
	}

	@PostMapping("/dataIntegrityViolationException")
	public Result<?> dataIntegrityViolationException() {
		DataAudit dataAudit = new DataAudit();
		dataAuditService.save(dataAudit);
		return R.success();
	}

	@PostMapping("/tooManyResultsException")
	public Result<?> tooManyResultsException() {
		DataAudit one = dataAuditService.lambdaQuery()
				.one();
		return R.success(one);
	}

}
