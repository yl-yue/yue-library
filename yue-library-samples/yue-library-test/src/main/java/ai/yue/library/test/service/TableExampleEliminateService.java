package ai.yue.library.test.service;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.entity.TableExampleEliminate;
import ai.yue.library.test.mapper.TableExampleEliminateMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TableExampleEliminateService extends ServiceImpl<TableExampleEliminateMapper, TableExampleEliminate> {

    public Result<TableExampleEliminate> insert(Object entity) {
        TableExampleEliminate entityObject = Convert.toJavaBean(entity, entityClass);
        super.save(entityObject);
        return R.success(entityObject);
    }

}
