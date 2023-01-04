package ai.yue.library.test.config;

import ai.yue.library.data.mybatis.provider.DataAuditProvider;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

/**
 * 数据填充提供者
 *
 * @author ylyue
 * @since  2022/12/29
 */
@Configuration
public class DataFillProvider extends DataAuditProvider {

    @Override
    public void insertFill(MetaObject metaObject) {
        // 保留数据审计实现
        super.insertFill(metaObject);

        // 自定义填充
        this.strictInsertFill(metaObject, "tenantSys", String.class, "sys");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 保留数据审计实现
        super.insertFill(metaObject);

        // 自定义填充
    }

}
