package ai.yue.library.test.controller.web.exception;

import ai.yue.library.base.exception.ParamException;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.entity.DataAudit;
import ai.yue.library.test.service.DataAuditService;
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
