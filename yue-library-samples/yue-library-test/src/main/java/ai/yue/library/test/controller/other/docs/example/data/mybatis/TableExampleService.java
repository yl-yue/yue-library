package ai.yue.library.test.controller.other.docs.example.data.mybatis;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.ipo.TableExampleIPO;
import ai.yue.library.web.util.ServletUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Service示例
 *
 * @author yl-yue
 * @since 2023/1/17
 */
//@Service
public class TableExampleService extends ServiceImpl<TableExampleMapper, TableExample> {

    // 插入数据
    public Result<?> insert(TableExampleIPO tableExampleIPO) {
        TableExample tableExample = Convert.toJavaBean(tableExampleIPO, TableExample.class);
        super.save(tableExample);
        return R.success(tableExample);
    }

    // 删除数据
    public Result<?> deleteById() {
        boolean b = super.removeById(666666L);
        return R.success(b);
    }

    // 更新数据
    public Result<?> updateById() {
        boolean b = super.updateById(new TableExample());
        return R.success(b);
    }

    // 查询数据
    public Result<?> getById() {
        TableExample tableExample = super.getById(666666L);
        return R.success(tableExample);
    }

    // 分页
    public Result<?> page(String fieldOne, String fieldTwo) {
        PageHelper.startPage(ServletUtils.getRequest());
        List<TableExample> list = super.lambdaQuery()
                .eq(TableExample::getFieldOne, fieldOne)
                .eq(TableExample::getFieldTwo, fieldTwo)
                .list();

        PageInfo<TableExample> pageInfo = PageInfo.of(list);
        return R.success(pageInfo);
    }

    // 更多方法示例
    public void example() {
        // insert
        super.save(new TableExample());
        super.saveOrUpdate(new TableExample());
        super.saveBatch(new ArrayList<>());
        super.saveOrUpdateBatch(new ArrayList<>());

        // delete
        super.removeById(666666L);
        super.removeByIds(new ArrayList<>());

        // update
        super.updateById(new TableExample());
        super.updateBatchById(new ArrayList<>());

        // query
        super.getById(666666L);
        super.list();

        // 更多
        super.lambdaQuery();
        super.getBaseMapper();
    }

}
