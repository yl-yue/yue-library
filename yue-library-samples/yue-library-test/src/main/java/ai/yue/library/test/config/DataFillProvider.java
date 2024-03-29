package ai.yue.library.test.config;

import ai.yue.library.data.mybatis.provider.AuditDataFill;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

/**
 * 数据填充提供者
 *
 * @author ylyue
 * @since  2022/12/29
 */
@Configuration
public class DataFillProvider extends AuditDataFill {

    @Override
    public void insertFill(MetaObject metaObject) {
        // 保留数据审计实现
        super.insertFill(metaObject);

        // 自定义填充
        this.strictInsertFill(metaObject, "aaa", String.class, "co");
        this.strictInsertFill(metaObject, "bbb", String.class, "sys");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 保留数据审计实现
        super.insertFill(metaObject);

        // 自定义填充
    }

}
