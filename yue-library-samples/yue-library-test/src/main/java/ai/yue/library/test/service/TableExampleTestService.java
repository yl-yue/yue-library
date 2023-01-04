package ai.yue.library.test.service;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.entity.TableExampleTest;
import ai.yue.library.test.ipo.TableExampleTestIPO;
import ai.yue.library.test.mapper.TableExampleTestMapper;
import ai.yue.library.web.util.ServletUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 表示例测试
 *
 * @author yl-yue
 * @since 2022/12/29
 */
@Service
public class TableExampleTestService extends ServiceImpl<TableExampleTestMapper, TableExampleTest> {

    public Result<?> insert(TableExampleTestIPO tableExampleTestIPO) {
        TableExampleTest tableExampleTest = Convert.toJavaBean(tableExampleTestIPO, TableExampleTest.class);
        super.save(tableExampleTest);
        return R.success(tableExampleTest);
    }

    public Result<?> deleteById(long id) {
        super.removeById(id);
        return R.success();
    }

    public Result<?> updateById(TableExampleTestIPO tableExampleTestIPO) {
        TableExampleTest tableExampleTest = Convert.toJavaBean(tableExampleTestIPO, TableExampleTest.class);
        super.updateById(tableExampleTest);
        return R.success();
    }

    public Result<?> page(TableExampleTestIPO tableExampleTestIPO) {
        PageHelper.startPage(ServletUtils.getRequest());
        QueryWrapper<TableExampleTest> queryWrapper = new QueryWrapper(Convert.toJavaBean(tableExampleTestIPO, entityClass));
        List<TableExampleTest> list = list(queryWrapper);
        return R.success(PageInfo.of(list));
    }

}
