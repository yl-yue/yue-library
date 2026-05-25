package ai.yue.library.data.log.config;

import ai.yue.library.data.log.aspect.LogAspect;
import ai.yue.library.data.log.controller.LogQueryController;
import ai.yue.library.data.log.controller.LogStorageController;
import ai.yue.library.data.log.filter.RequestLogFilter;
import ai.yue.library.data.log.mapper.LoginLogMapper;
import ai.yue.library.data.log.mapper.OperLogMapper;
import ai.yue.library.data.log.service.DefaultLogStorageProvider;
import ai.yue.library.data.log.service.HttpLogStorageProvider;
import ai.yue.library.data.log.service.LogArchiveService;
import ai.yue.library.data.log.service.LogMaskService;
import ai.yue.library.data.log.service.LoginLogService;
import ai.yue.library.data.log.service.NoOpLogStorageProvider;
import ai.yue.library.data.log.service.OperLogService;
import ai.yue.library.data.log.spi.LogStorageProvider;
import ai.yue.library.data.mybatis.service.SqlService;
import cn.hutool.v7.core.io.IoUtil;
import cn.hutool.v7.core.text.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 日志模块自动配置
 *
 * @author ylyue
 * @since 2025/5/13
 */
@Slf4j
@Configuration
@AutoConfigureAfter(name = "ai.yue.library.data.mybatis.config.MybatisAutoConfig")
@ConditionalOnProperty(prefix = LogProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({LogProperties.class, PlumelogProperties.class})
@EnableScheduling
@Import({LogAspect.class, LoginLogService.class, OperLogService.class, LogMaskService.class, LogArchiveService.class, RequestLogFilter.class})
public class LogAutoConfig {

    // ==================== 直连写库模式 ====================

    @Bean
    @ConditionalOnMissingBean(LogStorageProvider.class)
    @ConditionalOnProperty(prefix = LogProperties.PREFIX, name = "mode", havingValue = "direct")
    public DefaultLogStorageProvider defaultLogStorageProvider(LoginLogMapper loginLogMapper, OperLogMapper operLogMapper) {
        log.info("【日志模块】持久化模式：direct（直连写库）");
        return new DefaultLogStorageProvider(loginLogMapper, operLogMapper);
    }

    // ==================== 自动建表 ====================

    @Bean
    @ConditionalOnProperty(prefix = LogProperties.PREFIX, name = "auto-create-table", havingValue = "true", matchIfMissing = true)
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

    // ==================== 请求日志拦截器 ====================

    @Bean
    @ConditionalOnProperty(prefix = LogProperties.PREFIX + ".request-log", name = "enabled", havingValue = "true", matchIfMissing = true)
    public FilterRegistrationBean<RequestLogFilter> registerRequestLogFilter(RequestLogFilter requestLogFilter) {
        FilterRegistrationBean<RequestLogFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setOrder(-998);
        filterRegistrationBean.setFilter(requestLogFilter);
        return filterRegistrationBean;
    }

    // ==================== 持久化接口（日志服务端） ====================

    @Bean
    @ConditionalOnProperty(prefix = LogProperties.PREFIX + ".storage-api", name = "enabled", havingValue = "true")
    @ConditionalOnBean(DefaultLogStorageProvider.class)
    public LogStorageController logStorageController(DefaultLogStorageProvider defaultLogStorageProvider) {
        return new LogStorageController(defaultLogStorageProvider);
    }

    // ==================== 查询接口（业务管理后台） ====================

    @Bean
    @ConditionalOnProperty(prefix = LogProperties.PREFIX + ".query-api", name = "enabled", havingValue = "true")
    @ConditionalOnBean(DefaultLogStorageProvider.class)
    public LogQueryController logQueryController(LoginLogService loginLogService, OperLogService operLogService) {
        return new LogQueryController(loginLogService, operLogService);
    }

    // ==================== HTTP 转发模式（Forest 可用） ====================

    @Configuration
    @ConditionalOnClass(name = "com.dtflys.forest.Forest")
    @ConditionalOnProperty(prefix = LogProperties.PREFIX, name = "mode", havingValue = "http", matchIfMissing = true)
    static class HttpLogStorageConfig {

        @Bean
        @ConditionalOnMissingBean(LogStorageProvider.class)
        public HttpLogStorageProvider httpLogStorageProvider(LogProperties logProperties) {
            if (StrUtil.isBlank(logProperties.getHttp().getBaseUrl())) {
                log.warn("【日志模块】HTTP 模式未配置 base-url，日志记录将不生效。请配置 yue.data.log.http.base-url");
            }
            log.info("【日志模块】持久化模式：http（HTTP 转发），base-url={}", logProperties.getHttp().getBaseUrl());
            ai.yue.library.data.log.forest.LogStorageForestClient forestClient =
                    com.dtflys.forest.Forest.client(ai.yue.library.data.log.forest.LogStorageForestClient.class);
            return new HttpLogStorageProvider(forestClient);
        }

    }

    // ==================== HTTP 转发模式（Forest 缺失兜底） ====================

    @Configuration
    @ConditionalOnMissingClass("com.dtflys.forest.Forest")
    @ConditionalOnProperty(prefix = LogProperties.PREFIX, name = "mode", havingValue = "http", matchIfMissing = true)
    static class NoForestFallbackConfig {

        @Bean
        @ConditionalOnMissingBean(LogStorageProvider.class)
        public LogStorageProvider noForestFallback() {
            log.warn("【日志模块】HTTP 模式需要 Forest 依赖，请引入 forest-spring-boot3-starter");
            return new NoOpLogStorageProvider();
        }

    }

}
