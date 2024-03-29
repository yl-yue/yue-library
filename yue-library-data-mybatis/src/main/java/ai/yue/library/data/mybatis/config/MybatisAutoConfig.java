package ai.yue.library.data.mybatis.config;

import ai.yue.library.data.mybatis.interceptor.LogicDeleteInnerInterceptor;
import ai.yue.library.data.mybatis.provider.AuditDataFill;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(MybatisProperties.class)
@Import({AuditDataFill.class, LogicDeleteInnerInterceptor.class})
public class MybatisAutoConfig {

}
