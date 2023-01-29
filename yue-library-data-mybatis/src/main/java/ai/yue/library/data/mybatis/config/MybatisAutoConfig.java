package ai.yue.library.data.mybatis.config;

import ai.yue.library.data.mybatis.config.properties.JdbcProperties;
import ai.yue.library.data.mybatis.provider.DataAuditProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * data-mybatis配置，提供自动配置项支持与增强
 * 
 * @author	ylyue
 * @since	2018年6月11日
 */
@Configuration
@Import(DataAuditProvider.class)
@EnableConfigurationProperties({JdbcProperties.class})
public class MybatisAutoConfig {

//    @Bean
//    public MybatisPlusInterceptor mybatisPlusInterceptor() {
//        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//        AaaInnerInterceptor aaaInnerInterceptor = new AaaInnerInterceptor();
//        interceptor.addInnerInterceptor(aaaInnerInterceptor);
//        return interceptor;
//    }

}
