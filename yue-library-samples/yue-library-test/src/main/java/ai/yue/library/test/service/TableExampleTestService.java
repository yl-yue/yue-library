package ai.yue.library.test.service;

import ai.yue.library.data.mybatis.service.BaseService;
import ai.yue.library.test.entity.TableExampleTest;
import ai.yue.library.test.mapper.TableExampleTestMapper;
import org.springframework.stereotype.Service;

/**
 * 表示例测试
 *
 * @author yl-yue
 * @since 2022/12/29
 */
@Service
public class TableExampleTestService extends BaseService<TableExampleTestMapper, TableExampleTest> {

}
