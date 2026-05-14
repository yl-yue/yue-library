package ai.yue.library.data.log.service;

import ai.yue.library.data.log.entity.LoginLogEntity;
import ai.yue.library.data.log.entity.OperLogEntity;
import ai.yue.library.data.log.mapper.LoginLogMapper;
import ai.yue.library.data.log.mapper.OperLogMapper;
import ai.yue.library.data.log.spi.LogStorageProvider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultLogStorageProvider implements LogStorageProvider {

	private final LoginLogMapper loginLogMapper;
	private final OperLogMapper operLogMapper;

	@Override
	public void storeLoginLog(LoginLogEntity entity) {
		loginLogMapper.insert(entity);
	}

	@Override
	public void storeOperLog(OperLogEntity entity) {
		operLogMapper.insert(entity);
	}
}
