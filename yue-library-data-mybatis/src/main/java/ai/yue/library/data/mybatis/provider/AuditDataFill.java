package ai.yue.library.data.mybatis.provider;

import ai.yue.library.base.util.SpringUtils;
import ai.yue.library.data.mybatis.constant.DbConstant;
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
@ConditionalOnProperty(prefix = "yue.data.mybatis", name = "enable-data-audit", havingValue = "true")
public class AuditDataFill implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        // 1. 获得用户
        String user;
        Long userId;
        String tenantSysId;
        String tenantCoId;
        try {
            AuditDataProvider auditDataProvider = SpringUtils.getBean(AuditDataProvider.class);
            user = auditDataProvider.getUser();
            userId = auditDataProvider.getUserId();
            tenantSysId = auditDataProvider.getTenantSysId();
            tenantCoId = auditDataProvider.getTenantCoId();
        } catch (Exception e) {
            log.error("【数据审计】未找到合适的Bean：{}", AuditDataProvider.class);
            throw e;
        }
        
        // 2. 新增填充（支持i18n）
        long currentTimeMillis = System.currentTimeMillis();
        this.strictInsertFill(metaObject, DbConstant.CLASS_CREATE_USER, String.class, user);
        this.strictInsertFill(metaObject, DbConstant.CLASS_CREATE_USER_ID, Long.class, userId);
        this.strictInsertFill(metaObject, DbConstant.CLASS_CREATE_TIME, Long.class, currentTimeMillis);
        this.strictInsertFill(metaObject, DbConstant.CLASS_UPDATE_USER, String.class, user);
        this.strictInsertFill(metaObject, DbConstant.CLASS_UPDATE_USER_ID, Long.class, userId);
        this.strictInsertFill(metaObject, DbConstant.CLASS_UPDATE_TIME, Long.class, currentTimeMillis);
        this.strictInsertFill(metaObject, DbConstant.CLASS_TENANT_SYS_ID, String.class, tenantSysId);
        this.strictInsertFill(metaObject, DbConstant.CLASS_TENANT_CO_ID, String.class, tenantCoId);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 1. 获得用户
        String user;
        Long userId;
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
        this.strictUpdateFill(metaObject, DbConstant.CLASS_UPDATE_USER, String.class, user);
        this.strictUpdateFill(metaObject, DbConstant.CLASS_UPDATE_USER_ID, Long.class, userId);
        this.strictUpdateFill(metaObject, DbConstant.CLASS_UPDATE_TIME, Long.class, currentTimeMillis);
    }

}
