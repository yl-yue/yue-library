package ai.yue.library.test.config;

import ai.yue.library.data.mybatis.provider.AuditUserProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis配置
 *
 * @author yl-yue
 * @since 2022/12/29
 */
@Configuration
public class MybatisConfig {

    /**
     * 审计用户提供
     */
    @Bean
    public AuditUserProvider auditUserProvider() {
        return new AuditUserProvider() {
            @Override
            public String getUser() {
                return "ylyue";
            }

            @Override
            public String getUserId() {
                return "e07f659764054ed6a222139fb83f4171";
            }
        };
    }

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
//    @Bean
//    public MybatisPlusInterceptor mybatisPlusInterceptor() {
//        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
//        return interceptor;
//    }

//    @Bean
//    public ConfigurationCustomizer configurationCustomizer() {
//        return configuration -> configuration.setUseDeprecatedExecutor(false);
//    }

}
