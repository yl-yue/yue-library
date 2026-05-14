package ai.yue.library.data.log.config;

import cn.hutool.v7.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import ai.yue.library.data.log.aspect.LogAspect;
import ai.yue.library.data.log.controller.LogStorageController;
import ai.yue.library.data.log.mapper.LoginLogMapper;
import ai.yue.library.data.log.mapper.OperLogMapper;
import ai.yue.library.data.log.service.DefaultLogStorageProvider;
import ai.yue.library.data.log.service.HttpLogStorageProvider;
import ai.yue.library.data.log.service.LogArchiveService;
import ai.yue.library.data.log.service.LoginLogService;
import ai.yue.library.data.log.service.LogMaskService;
import ai.yue.library.data.log.service.OperLogService;
import ai.yue.library.data.log.spi.LogStorageProvider;
import ai.yue.library.data.mybatis.service.SqlService;

@Slf4j
@Configuration
@AutoConfigureAfter(name = "ai.yue.library.data.mybatis.config.MybatisAutoConfig")
@ConditionalOnProperty(prefix = "yue.data.log", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(LogProperties.class)
@EnableScheduling
@Import({LogAspect.class, LoginLogService.class, OperLogService.class, LogMaskService.class, LogArchiveService.class})
public class LogAutoConfig {

	@Bean
	@ConditionalOnMissingBean(LogStorageProvider.class)
	@ConditionalOnProperty(prefix = "yue.data.log.http", name = "enabled", havingValue = "false", matchIfMissing = true)
	public DefaultLogStorageProvider defaultLogStorageProvider(LoginLogMapper loginLogMapper, OperLogMapper operLogMapper) {
		return new DefaultLogStorageProvider(loginLogMapper, operLogMapper);
	}

	@Bean
	@ConditionalOnMissingBean(LogStorageProvider.class)
	@ConditionalOnProperty(prefix = "yue.data.log.http", name = "enabled", havingValue = "true")
	@ConditionalOnClass(name = "com.dtflys.forest.Forest")
	public HttpLogStorageProvider httpLogStorageProvider(LogProperties logProperties) {
		return new HttpLogStorageProvider(logProperties);
	}

	@Bean
	@ConditionalOnProperty(prefix = "yue.data.log.auto-create-table", name = "enabled", havingValue = "true", matchIfMissing = true)
	@ConditionalOnBean(DefaultLogStorageProvider.class)
	public CommandLineRunner initLogTables(SqlService sqlService) {
		return args -> {
			log.info("【日志模块】开始自动建表...");
			String ddl = IoUtil.readUtf8(getClass().getClassLoader().getResourceAsStream("sql/log_tables.sql"));
			String[] sqls = ddl.split(";");
			for (String sql : sqls) {
				String trimmedSql = sql.trim();
				if (trimmedSql.isEmpty() || trimmedSql.length() < 10) {
					continue;
				}
				try {
					sqlService.execSqlForJson(null, trimmedSql, null);
				} catch (Exception e) {
					log.warn("【日志模块】建表SQL执行异常（表可能已存在）：{}", e.getMessage());
				}
			}
			log.info("【日志模块】自动建表完成");
		};
	}

	@Bean
	@ConditionalOnProperty(prefix = "yue.data.log.storage-api", name = "enabled", havingValue = "true")
	@ConditionalOnBean(DefaultLogStorageProvider.class)
	public LogStorageController logStorageController(DefaultLogStorageProvider defaultLogStorageProvider) {
		return new LogStorageController(defaultLogStorageProvider);
	}
}
