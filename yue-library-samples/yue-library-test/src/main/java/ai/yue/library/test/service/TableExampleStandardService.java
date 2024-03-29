package ai.yue.library.test.service;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.entity.TableExampleStandard;
import ai.yue.library.test.mapper.TableExampleStandardMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 表示例测试
 *
 * @author yl-yue
 * @since 2022/12/29
 */
@Service
public class TableExampleStandardService extends ServiceImpl<TableExampleStandardMapper, TableExampleStandard> {

    /**
     * 插入数据
     *
     * @param entity 实体参数，支持实体对象、map、json
     * @return 填充后的实体
     */
    public Result<TableExampleStandard> insert(Object entity) {
        TableExampleStandard entityObject = Convert.toJavaBean(entity, entityClass);
        super.save(entityObject);
        return R.success(entityObject);
    }

}
