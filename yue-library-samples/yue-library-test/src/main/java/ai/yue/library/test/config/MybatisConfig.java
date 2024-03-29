package ai.yue.library.test.config;

import ai.yue.library.data.mybatis.interceptor.LogicDeleteInnerInterceptor;
import ai.yue.library.data.mybatis.provider.AuditDataProvider;
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
    public AuditDataProvider auditDataProvider() {
        return new AuditDataProvider() {
            @Override
            public String getUser() {
                return "ylyue";
            }

            @Override
            public Long getUserId() {
                return 1711185600000L;
            }

            @Override
            public String getTenantSys() {
                return "sys";
            }

            @Override
            public String getTenantCo() {
                return "co";
            }
        };
    }

}
