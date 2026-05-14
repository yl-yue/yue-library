package ai.yue.library.data.log.controller;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.log.entity.LoginLogEntity;
import ai.yue.library.data.log.entity.OperLogEntity;
import ai.yue.library.data.log.service.DefaultLogStorageProvider;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lan/v1/log")
public class LogStorageController {

	private final DefaultLogStorageProvider logStorageProvider;

	public LogStorageController(DefaultLogStorageProvider logStorageProvider) {
		this.logStorageProvider = logStorageProvider;
	}

	@PostMapping("/loginLog/insert")
	public Result<?> insertLoginLog(LoginLogEntity entity) {
		logStorageProvider.storeLoginLog(entity);
		return R.success();
	}

	@PostMapping("/operLog/insert")
	public Result<?> insertOperLog(OperLogEntity entity) {
		logStorageProvider.storeOperLog(entity);
		return R.success();
	}
}
