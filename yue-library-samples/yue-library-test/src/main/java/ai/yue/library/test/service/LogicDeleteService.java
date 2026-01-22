package ai.yue.library.test.service;

import ai.yue.library.base.util.DateUtils;
import ai.yue.library.base.util.time.interval.TimeInterval;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.entity.TableExampleStandard;
import ai.yue.library.test.mapper.LogicDeleteMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @DS("mysql2")
    public Result<?> getLogicDelete() {
        System.out.println("======实体类中没有deleteTime字段，不应该受逻辑删除影响======start");
        tableExampleEliminateService.getById(283);
        System.out.println();
        System.out.println();
        tableExampleEliminateService.getBaseMapper().getDataNotLogicDelete();
        System.out.println("======实体类中没有deleteTime字段，不应该受逻辑删除影响======end");

        // 追加 delete_time = 0
        return R.success(baseMapper.getLogicDelete());
    }

    public Result<?> listLogicDelete() {
        return R.success(baseMapper.listLogicDelete());
    }

    public Result<?> listLogicDeleteData() {
        List<TableExampleStandard> list = super.lambdaQuery()
//                .gt(BaseEntity::getDeleteTime, 0)
                .list();
        return R.success(list);
    }

    public Result<?> queryLogicDelete1() {
        return R.success(baseMapper.queryLogicDelete1());
    }

    public Result<?> queryLogicDelete3() {
        return R.success(baseMapper.queryLogicDelete3());
    }

    public Result<?> queryLogicDeleteAll() {
        baseMapper.queryLogicDelete1();
        baseMapper.queryLogicDelete2();
        baseMapper.queryLogicDelete3();
        baseMapper.queryLogicDelete4();
        baseMapper.queryLogicDelete5();
        baseMapper.queryLogicDelete6();
        baseMapper.queryLogicDelete7();
        return R.success();
    }

    public Result<?> updateLogicDelete() {
        TimeInterval timeInterval = DateUtils.timer();
        System.out.println("====== 实体类中没有 deleteTime 字段，不应该受逻辑删除影响 ====== - start");
        tableExampleEliminateService.getBaseMapper().updateLogicDelete();
        System.out.println("====== 实体类中没有 deleteTime 字段，不应该受逻辑删除影响 ====== - end");

        System.out.println("====== xml 拦截 deleteTime 追加 ====== - start");
        baseMapper.updateLogicDelete();
        System.out.println("====== xml 拦截 deleteTime 追加 ====== - end");


        TableExampleStandard tableExample = new TableExampleStandard();
        tableExample.setId(1924658465827774465L);
        tableExample.setFieldOne("1");
        tableExample.setFieldTwo("2");
        tableExample.setFieldThree("3");
        super.updateById(tableExample);
        long intervalMs = timeInterval.intervalMs();
        System.out.println("耗时：" + intervalMs + "ms");
        System.out.println("耗时：" + intervalMs + "ms");
        System.out.println("耗时：" + intervalMs + "ms");
        System.out.println("耗时：" + intervalMs + "ms");
        System.out.println("耗时：" + intervalMs + "ms");
        System.out.println("耗时：" + intervalMs + "ms");
        System.out.println("耗时：" + intervalMs + "ms");

        // 租户拦截 + 逻辑拦截 ≈ 10-15ms

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
