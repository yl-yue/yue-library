package ai.yue.library.data.mybatis.interceptor;

import ai.yue.library.base.util.SpringUtils;
import ai.yue.library.data.mybatis.config.MybatisProperties;
import ai.yue.library.data.mybatis.constant.DbConstant;
import ai.yue.library.data.mybatis.provider.AuditDataProvider;
import ai.yue.library.data.mybatis.provider.AuditValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * 企业租户拦截器
 *
 * @author yl-yue
 * @since 2023/2/20
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "yue.data.mybatis", name = "enable-tenant-co", havingValue = "true", matchIfMissing = true)
public class TenantCoInterceptor extends FieldInterceptor {

    public TenantCoInterceptor(MybatisProperties mybatisProperties) {
        AuditDataProvider auditDataProvider = null;
        try {
            auditDataProvider = SpringUtils.getBean(AuditDataProvider.class);
        } catch (Exception e) {
            log.error("【企业租户拦截器】未找到合适的Bean：{}", AuditDataProvider.class);
            throw e;
        }

        this.interceptorName = "企业租户拦截器";
        this.ignoreTableList = mybatisProperties.getIgnoreTableTenantCos();
        this.classField = DbConstant.CLASS_TENANT_CO;
        this.dbField = DbConstant.DB_TENANT_CO;
        this.tenantIdExpression = new AuditValue(auditDataProvider);
    }

}
