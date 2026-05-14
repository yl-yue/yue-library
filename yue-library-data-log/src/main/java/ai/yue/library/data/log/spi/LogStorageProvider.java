package ai.yue.library.data.log.spi;

import ai.yue.library.data.log.entity.LoginLogEntity;
import ai.yue.library.data.log.entity.OperLogEntity;

public interface LogStorageProvider {

	void storeLoginLog(LoginLogEntity entity);

	void storeOperLog(OperLogEntity entity);
}
