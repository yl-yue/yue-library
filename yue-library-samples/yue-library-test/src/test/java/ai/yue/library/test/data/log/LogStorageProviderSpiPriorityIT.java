package ai.yue.library.test.data.log;

import ai.yue.library.data.log.entity.LoginLogEntity;
import ai.yue.library.data.log.entity.OperLogEntity;
import ai.yue.library.data.log.spi.LogStorageProvider;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest
@TestPropertySource(properties = {
		"yue.data.log.async=false"
})
class LogStorageProviderSpiPriorityIT {

	private static final String CUSTOM_MARKER = "CUSTOM_SPI";

	@TestConfiguration
	static class CustomProviderConfig {

		@Bean
		public LogStorageProvider customLogStorageProvider() {
			return new LogStorageProvider() {
				@Override
				public void storeLoginLog(LoginLogEntity entity) {
					log.info("【自定义SPI】存储登录日志：username={}", entity.getUsername());
					entity.setMsg(CUSTOM_MARKER);
				}

				@Override
				public void storeOperLog(OperLogEntity entity) {
					log.info("【自定义SPI】存储操作日志：title={}", entity.getTitle());
					entity.setTitle(CUSTOM_MARKER);
				}
			};
		}
	}

	@Resource
	private LogStorageProvider logStorageProvider;

	@Test
	void customProvider_takesPriorityOverDefault() {
		String className = logStorageProvider.getClass().getName();
		log.info("【SPI优先级】当前 LogStorageProvider 类型：{}", className);
		assertTrue(className.contains("CustomProviderConfig") || className.contains("$Proxy") || className.contains("Lambda"),
				"自定义 LogStorageProvider 应优先于 DefaultLogStorageProvider，实际类型：" + className);
	}

	@Test
	void customProvider_interceptsLoginLog() {
		LoginLogEntity entity = LoginLogEntity.builder()
				.username("spi_test")
				.ip("127.0.0.1")
				.loginTime(System.currentTimeMillis())
				.status(1)
				.msg("原始消息")
				.build();

		logStorageProvider.storeLoginLog(entity);
		assertEquals(CUSTOM_MARKER, entity.getMsg(), "自定义 SPI 应修改 msg 字段");
	}

	@Test
	void customProvider_interceptsOperLog() {
		OperLogEntity entity = OperLogEntity.builder()
				.title("原始标题")
				.operType("R")
				.username("spi_test")
				.ip("127.0.0.1")
				.operTime(System.currentTimeMillis())
				.status(1)
				.build();

		logStorageProvider.storeOperLog(entity);
		assertEquals(CUSTOM_MARKER, entity.getTitle(), "自定义 SPI 应修改 title 字段");
	}

}