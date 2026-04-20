package ai.yue.library.test.config;

import ai.yue.library.base.util.StrUtils;
import ai.yue.library.data.mybatis.provider.AuditDataProvider;
import ai.yue.library.web.util.ServletUtils;
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
            public String getTenantSysId() {
                return "sys";
            }

            @Override
            public String getTenantCoId() {
                String tenantCoId = ServletUtils.getRequest().getHeader("tenantCoId");
                if (StrUtils.isBlank(tenantCoId)) {
                    tenantCoId = "co";
                }

                return tenantCoId;
            }
        };
    }

}
