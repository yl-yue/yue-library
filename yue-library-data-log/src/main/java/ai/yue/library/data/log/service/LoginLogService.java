package ai.yue.library.data.log.service;

import org.springframework.stereotype.Service;

import ai.yue.library.base.util.AsyncUtils;
import ai.yue.library.data.log.config.LogProperties;
import ai.yue.library.data.log.entity.LoginLogEntity;
import ai.yue.library.data.log.spi.LogStorageProvider;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LoginLogService {

	@Resource
	private LogStorageProvider logStorageProvider;

	@Resource
	private LogProperties logProperties;

	@Resource(name = "logMaskService")
	private LogMaskService logMaskService;

	public void recordLogin(String username, String ip, boolean success, String msg, String requestParam) {
		try {
			LoginLogEntity entity = LoginLogEntity.builder()
					.username(username)
					.ip(ip)
					.loginTime(System.currentTimeMillis())
					.status(success ? 1 : 0)
					.msg(msg)
					.requestParam(maskParam(requestParam))
					.build();

			if (logProperties.getAsync()) {
				AsyncUtils.exec(() -> {
					try {
						logStorageProvider.storeLoginLog(entity);
					} catch (Exception e) {
						log.warn("【登录日志】异步记录失败：{}", e.getMessage());
					}
				});
			} else {
				logStorageProvider.storeLoginLog(entity);
			}
		} catch (Exception e) {
			log.warn("【登录日志】记录异常：{}", e.getMessage());
		}
	}

	private String maskParam(String param) {
		if (!logProperties.getMaskEnabled() || param == null) {
			return param;
		}
		return logMaskService.maskParam(param);
	}
}