package ai.yue.library.test.service;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.mybatis.service.BaseService;
import ai.yue.library.test.entity.TableExampleTest;
import ai.yue.library.test.mapper.LogicDeleteMapper;
import org.springframework.stereotype.Service;

/**
 * 逻辑删除测试
 *
 * @author yl-yue
 * @since 2022/12/29
 */
@Service
public class LogicDeleteService extends BaseService<LogicDeleteMapper, TableExampleTest> {

    public Result<?> test() {
        TableExampleTest tableExampleTest = new TableExampleTest();
        tableExampleTest.setFieldOne("1");
        tableExampleTest.setFieldTwo("2");
        tableExampleTest.setFieldThree("3");
        baseMapper.insert(tableExampleTest);
        baseMapper.selectById(1608662675848990722L);
        baseMapper.selectList(null);
        baseMapper.getLogicDelete();
        baseMapper.listLogicDelete();
        baseMapper.updateLogicDelete();
        baseMapper.delLogicDelete();
        return R.success();
    }

}
