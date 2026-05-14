package ai.yue.library.data.log.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.log.entity.LoginLogEntity;
import ai.yue.library.data.log.entity.OperLogEntity;
import ai.yue.library.data.log.ipo.LoginLogPageIPO;
import ai.yue.library.data.log.ipo.OperLogPageIPO;
import ai.yue.library.data.log.service.LoginLogService;
import ai.yue.library.data.log.service.OperLogService;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;

/**
 * 日志查询接口（业务管理后台）
 * <p>使用 auth 安全前缀，需认证后访问</p>
 *
 * @author ylyue
 * @since 2025/5/14
 */
@RestController
@RequestMapping("/auth/v1/log")
@RequiredArgsConstructor
public class LogQueryController {

	private final LoginLogService loginLogService;
	private final OperLogService operLogService;

	@GetMapping("/loginLog/page")
	public Result<PageInfo<LoginLogEntity>> pageLoginLog(LoginLogPageIPO ipo) {
		return R.success(loginLogService.pageLoginLog(ipo));
	}

	@GetMapping("/loginLog/getById")
	public Result<LoginLogEntity> getLoginLog(Long id) {
		return R.success(loginLogService.getLoginLog(id));
	}

	@GetMapping("/operLog/page")
	public Result<PageInfo<OperLogEntity>> pageOperLog(OperLogPageIPO ipo) {
		return R.success(operLogService.pageOperLog(ipo));
	}

	@GetMapping("/operLog/getById")
	public Result<OperLogEntity> getOperLog(Long id) {
		return R.success(operLogService.getOperLog(id));
	}

}
