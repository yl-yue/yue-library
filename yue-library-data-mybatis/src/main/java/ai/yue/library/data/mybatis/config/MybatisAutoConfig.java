package ai.yue.library.data.mybatis.config;

import ai.yue.library.data.mybatis.interceptor.LogicDeleteInterceptor;
import ai.yue.library.data.mybatis.interceptor.TenantCoInterceptor;
import ai.yue.library.data.mybatis.provider.AuditDataFill;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(MybatisProperties.class)
@Import({AuditDataFill.class, LogicDeleteInterceptor.class, TenantCoInterceptor.class})
public class MybatisAutoConfig {

}
