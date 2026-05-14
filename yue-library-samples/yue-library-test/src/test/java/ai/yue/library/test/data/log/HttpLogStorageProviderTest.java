package ai.yue.library.test.data.log;

import ai.yue.library.data.log.entity.LoginLogEntity;
import ai.yue.library.data.log.entity.OperLogEntity;
import ai.yue.library.data.log.forest.LogStorageForestClient;
import ai.yue.library.data.log.service.HttpLogStorageProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class HttpLogStorageProviderTest {

	private final LogStorageForestClient forestClient = mock(LogStorageForestClient.class);
	private final HttpLogStorageProvider provider = new HttpLogStorageProvider(forestClient);

	@Test
	void storeLoginLog_delegatesToForestClient() {
		LoginLogEntity entity = LoginLogEntity.builder()
				.username("admin")
				.ip("127.0.0.1")
				.loginTime(System.currentTimeMillis())
				.status(1)
				.msg("登录成功")
				.build();

		provider.storeLoginLog(entity);

		verify(forestClient).storeLoginLog(entity);
	}

	@Test
	void storeOperLog_delegatesToForestClient() {
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

		verify(forestClient).storeOperLog(entity);
	}

	@Test
	void storeLoginLog_exceptionIsolation_noPropagation() {
		doThrow(new RuntimeException("连接超时")).when(forestClient).storeLoginLog(any());

		LoginLogEntity entity = LoginLogEntity.builder()
				.username("admin")
				.ip("127.0.0.1")
				.loginTime(System.currentTimeMillis())
				.status(1)
				.msg("登录成功")
				.build();

		assertDoesNotThrow(() -> provider.storeLoginLog(entity));
	}

	@Test
	void storeOperLog_exceptionIsolation_noPropagation() {
		doThrow(new RuntimeException("服务不可用")).when(forestClient).storeOperLog(any());

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

		assertDoesNotThrow(() -> provider.storeOperLog(entity));
	}

}