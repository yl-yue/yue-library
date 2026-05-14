package ai.yue.library.data.log.service;

import org.springframework.stereotype.Service;

import ai.yue.library.base.util.AsyncUtils;
import ai.yue.library.data.log.config.LogProperties;
import ai.yue.library.data.log.entity.OperLogEntity;
import ai.yue.library.data.log.spi.LogStorageProvider;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OperLogService {

	@Resource
	private LogStorageProvider logStorageProvider;

	@Resource
	private LogProperties logProperties;

	@Resource(name = "logMaskService")
	private LogMaskService logMaskService;

	public void recordOper(OperLogEntity entity) {
		try {
			if (logProperties.getMaskEnabled() && entity.getRequestParam() != null) {
				entity.setRequestParam(logMaskService.maskParam(entity.getRequestParam()));
			}

			if (logProperties.getAsync()) {
				AsyncUtils.exec(() -> {
					try {
						logStorageProvider.storeOperLog(entity);
					} catch (Exception e) {
						log.warn("【操作日志】异步记录失败：{}", e.getMessage());
					}
				});
			} else {
				logStorageProvider.storeOperLog(entity);
			}
		} catch (Exception e) {
			log.warn("【操作日志】记录异常：{}", e.getMessage());
		}
	}
}