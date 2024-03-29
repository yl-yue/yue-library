package ai.yue.library.test.service;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.entity.TableExampleStandard;
import ai.yue.library.test.mapper.LogicDeleteMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 逻辑删除测试
 *
 * @author yl-yue
 * @since 2022/12/29
 */
@Service
public class LogicDeleteService extends ServiceImpl<LogicDeleteMapper, TableExampleStandard> {

    @Autowired
    TableExampleEliminateService tableExampleEliminateService;

    public Result<?> testDefault() {
        TableExampleStandard tableExample = new TableExampleStandard();
        tableExample.setFieldOne("1");
        tableExample.setFieldTwo("2");
        tableExample.setFieldThree("3");
        baseMapper.insert(tableExample);
        baseMapper.deleteById(tableExample.getId());

        tableExample.setFieldOne("22");
        baseMapper.updateById(tableExample);

        // ======查看Druid执行SQL打印，是否追加delete_time = 0======
        TableExampleStandard selectById = baseMapper.selectById(tableExample.getId());
        assert selectById == null;
        return R.success();
    }

    // ======查看Druid执行SQL打印，是否追加delete_time = 0======

    public Result<?> getLogicDelete() {
        System.out.println("======实体类中没有deleteTime字段，不应该受逻辑删除影响======start");
        tableExampleEliminateService.getById(283);
        System.out.println();
        System.out.println();
        tableExampleEliminateService.getBaseMapper().getDataNotLogicDelete();
        System.out.println("======实体类中没有deleteTime字段，不应该受逻辑删除影响======end");

        // 追加delete_time = 0
        return R.success(baseMapper.getLogicDelete());
    }

    public Result<?> listLogicDelete() {
        return R.success(baseMapper.listLogicDelete());
    }

    public Result<?> updateLogicDelete() {
        System.out.println("======实体类中没有deleteTime字段，不应该受逻辑删除影响======start");
        tableExampleEliminateService.getBaseMapper().updateLogicDelete();
        System.out.println("======实体类中没有deleteTime字段，不应该受逻辑删除影响======end");

        baseMapper.updateLogicDelete();
        return R.success();
    }

    public Result<?> delLogicDelete() {
        System.out.println("======实体类中没有deleteTime字段，不应该受逻辑删除影响======start");
        tableExampleEliminateService.getBaseMapper().delLogicDelete();
        System.out.println("======实体类中没有deleteTime字段，不应该受逻辑删除影响======end");

        baseMapper.delLogicDelete();
        return R.success();
    }

}
