package ai.yue.library.data.log.service;

import ai.yue.library.data.log.config.LogProperties;
import ai.yue.library.data.log.entity.LoginLogEntity;
import ai.yue.library.data.log.entity.OperLogEntity;
import ai.yue.library.data.log.forest.LogStorageForestClient;
import ai.yue.library.data.log.spi.LogStorageProvider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpLogStorageProvider implements LogStorageProvider {

	private final LogStorageForestClient forestClient;

	public HttpLogStorageProvider(LogProperties logProperties) {
		this.forestClient = com.dtflys.forest.Forest.client(LogStorageForestClient.class);
	}

	public HttpLogStorageProvider(LogStorageForestClient forestClient) {
		this.forestClient = forestClient;
	}

	@Override
	public void storeLoginLog(LoginLogEntity entity) {
		try {
			forestClient.storeLoginLog(entity);
		} catch (Exception e) {
			log.warn("【日志HTTP转发】登录日志转发失败：{}", e.getMessage());
		}
	}

	@Override
	public void storeOperLog(OperLogEntity entity) {
		try {
			forestClient.storeOperLog(entity);
		} catch (Exception e) {
			log.warn("【日志HTTP转发】操作日志转发失败：{}", e.getMessage());
		}
	}
}
