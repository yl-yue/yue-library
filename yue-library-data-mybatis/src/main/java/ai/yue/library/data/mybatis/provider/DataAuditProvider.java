package ai.yue.library.data.mybatis.provider;

import ai.yue.library.base.util.SpringUtils;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;

/**
 * 数据审计提供者
 *
 * @author ylyue
 * @since  2022/12/29
 */
@Slf4j
@Configuration
@ConditionalOnMissingBean(MetaObjectHandler.class)
@ConditionalOnProperty(prefix = "yue.data.mybatis", name = "enableDataAudit", havingValue = "true")
public class DataAuditProvider implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        // 1. 获得用户
        String user = null;
        String userId = null;
        try {
            user = SpringUtils.getBean(AuditUserProvider.class).getUser();
            userId = SpringUtils.getBean(AuditUserProvider.class).getUserId();
        } catch (Exception e) {
            log.error("【数据审计】未找到合适的Bean：{}", AuditUserProvider.class);
            throw e;
        }

        // 2. 新增填充（支持i18n）
        this.strictInsertFill(metaObject, "createUser", String.class, user);
        this.strictInsertFill(metaObject, "createUserId", String.class, userId);
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now(LocaleContextHolder.getTimeZone().toZoneId()));
        this.strictInsertFill(metaObject, "updateUser", String.class, user);
        this.strictInsertFill(metaObject, "updateUserId", String.class, userId);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now(LocaleContextHolder.getTimeZone().toZoneId()));
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 1. 获得用户
        String user = null;
        String userId = null;
        try {
            user = SpringUtils.getBean(AuditUserProvider.class).getUser();
            userId = SpringUtils.getBean(AuditUserProvider.class).getUserId();
        } catch (Exception e) {
            log.error("【数据审计】未找到合适的Bean：{}", AuditUserProvider.class);
            throw e;
        }

        // 2. 更新填充（支持i18n）
        this.strictUpdateFill(metaObject, "updateUser", String.class, user);
        this.strictUpdateFill(metaObject, "updateUserId", String.class, userId);
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now(LocaleContextHolder.getTimeZone().toZoneId()));
    }

}
