package ai.yue.library.test.config;

import ai.yue.library.data.mybatis.interceptor.LogicDeleteInnerInterceptor;
import ai.yue.library.data.mybatis.provider.AuditUserProvider;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
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

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new LogicDeleteInnerInterceptor());
        return interceptor;
    }

}
