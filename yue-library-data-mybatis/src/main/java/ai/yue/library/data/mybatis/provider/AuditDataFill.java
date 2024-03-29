package ai.yue.library.data.mybatis.provider;

import ai.yue.library.base.util.SpringUtils;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * 数据审计填充
 *
 * @author ylyue
 * @since  2022/12/29
 */
@Slf4j
@Configuration
@ConditionalOnMissingBean(MetaObjectHandler.class)
@ConditionalOnProperty(prefix = "yue.data.mybatis", name = "enable-data-audit", havingValue = "true", matchIfMissing = true)
public class AuditDataFill implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        // 1. 获得用户
        String user = null;
        Long userId = null;
        String tenantSys = null;
        String tenantCo = null;
        try {
            AuditDataProvider auditDataProvider = SpringUtils.getBean(AuditDataProvider.class);
            user = auditDataProvider.getUser();
            userId = auditDataProvider.getUserId();
            tenantSys = auditDataProvider.getTenantSys();
            tenantCo = auditDataProvider.getTenantCo();
        } catch (Exception e) {
            log.error("【数据审计】未找到合适的Bean：{}", AuditDataProvider.class);
            throw e;
        }

        // 2. 新增填充（支持i18n）
        long currentTimeMillis = System.currentTimeMillis();
        this.strictInsertFill(metaObject, "createUser", String.class, user);
        this.strictInsertFill(metaObject, "createUserId", Long.class, userId);
        this.strictInsertFill(metaObject, "createTime", Long.class, currentTimeMillis);
        this.strictInsertFill(metaObject, "updateUser", String.class, user);
        this.strictInsertFill(metaObject, "updateUserId", Long.class, userId);
        this.strictInsertFill(metaObject, "updateTime", Long.class, currentTimeMillis);
        this.strictInsertFill(metaObject, "tenantSys", String.class, tenantSys);
        this.strictInsertFill(metaObject, "tenantCo", String.class, tenantCo);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 1. 获得用户
        String user = null;
        Long userId = null;
        try {
            AuditDataProvider auditDataProvider = SpringUtils.getBean(AuditDataProvider.class);
            user = auditDataProvider.getUser();
            userId = auditDataProvider.getUserId();
        } catch (Exception e) {
            log.error("【数据审计】未找到合适的Bean：{}", AuditDataProvider.class);
            throw e;
        }

        // 2. 更新填充（支持i18n）
        long currentTimeMillis = System.currentTimeMillis();
        this.strictUpdateFill(metaObject, "updateUser", String.class, user);
        this.strictUpdateFill(metaObject, "updateUserId", Long.class, userId);
        this.strictUpdateFill(metaObject, "updateTime", Long.class, currentTimeMillis);
    }

}
