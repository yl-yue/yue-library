package ai.yue.library.test.docs.example.data.mybatis;

import ai.yue.library.data.mybatis.service.BaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Service示例
 *
 * @author yl-yue
 * @since 2023/1/17
 */
@Service
public class TableExampleService extends BaseService<TableExampleMapper, TableExample> {

    public void example() {
        // insert
        super.insert(new TableExample());
        super.insertOrUpdate(new TableExample());
        super.insertBatch(new ArrayList<>());
        super.insertOrUpdateBatch(new ArrayList<>());

        // delete
        super.deleteById(666666L);
        super.deleteByIds(new ArrayList<>());

        // update
        super.updateById(new TableExample());
        super.updateBatchById(new ArrayList<>());

        // query
        super.getById(666666L);
        super.listAll();
        super.page(new TableExample());

        // 更多
        ServiceImpl<TableExampleMapper, TableExample> serviceImpl = super.getServiceImpl();
        // serviceImpl.xxx
        TableExampleMapper baseMapper = super.getBaseMapper();
        // baseMapper.xxx
    }

}
