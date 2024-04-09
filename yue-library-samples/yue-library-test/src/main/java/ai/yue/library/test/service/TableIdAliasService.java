package ai.yue.library.test.service;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.entity.TableIdAlias;
import ai.yue.library.test.mapper.TableIdAliasMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TableIdAliasService extends ServiceImpl<TableIdAliasMapper, TableIdAlias> {

    public Result<?> testIdAlias(TableIdAlias tableIdAlias) {
        super.save(tableIdAlias);
        System.out.println(tableIdAlias);
        super.getById(tableIdAlias.getUserId());
        tableIdAlias.setFieldThree(String.valueOf(System.currentTimeMillis()));
        super.updateById(tableIdAlias);
        System.out.println(tableIdAlias);
        super.removeById(tableIdAlias.getUserId());
        return R.success();
    }

}
