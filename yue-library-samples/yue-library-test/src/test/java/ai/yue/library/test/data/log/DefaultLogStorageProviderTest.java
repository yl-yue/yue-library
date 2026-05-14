package ai.yue.library.test.data.log;

import ai.yue.library.data.log.entity.LoginLogEntity;
import ai.yue.library.data.log.entity.OperLogEntity;
import ai.yue.library.data.log.mapper.LoginLogMapper;
import ai.yue.library.data.log.mapper.OperLogMapper;
import ai.yue.library.data.log.service.DefaultLogStorageProvider;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DefaultLogStorageProviderTest {

	private final LoginLogMapper loginLogMapper = mock(LoginLogMapper.class);
	private final OperLogMapper operLogMapper = mock(OperLogMapper.class);
	private final DefaultLogStorageProvider provider = new DefaultLogStorageProvider(loginLogMapper, operLogMapper);

	@Test
	void storeLoginLog_delegatesToMapper() {
		LoginLogEntity entity = LoginLogEntity.builder()
				.username("admin")
				.ip("127.0.0.1")
				.loginTime(System.currentTimeMillis())
				.status(1)
				.msg("登录成功")
				.build();

		provider.storeLoginLog(entity);

		ArgumentCaptor<LoginLogEntity> captor = ArgumentCaptor.forClass(LoginLogEntity.class);
		verify(loginLogMapper).insert(captor.capture());
		LoginLogEntity captured = captor.getValue();
		assertEquals("admin", captured.getUsername());
		assertEquals("127.0.0.1", captured.getIp());
		assertEquals(1, captured.getStatus());
		assertEquals("登录成功", captured.getMsg());
	}

	@Test
	void storeOperLog_delegatesToMapper() {
		OperLogEntity entity = OperLogEntity.builder()
				.title("新增用户")
				.operType("C")
				.username("admin")
				.ip("127.0.0.1")
				.operTime(System.currentTimeMillis())
				.requestUrl("/api/user/insert")
				.requestMethod("POST")
				.status(1)
				.build();

		provider.storeOperLog(entity);

		ArgumentCaptor<OperLogEntity> captor = ArgumentCaptor.forClass(OperLogEntity.class);
		verify(operLogMapper).insert(captor.capture());
		OperLogEntity captured = captor.getValue();
		assertEquals("新增用户", captured.getTitle());
		assertEquals("C", captured.getOperType());
		assertEquals("admin", captured.getUsername());
		assertEquals("POST", captured.getRequestMethod());
		assertEquals(1, captured.getStatus());
	}

}
