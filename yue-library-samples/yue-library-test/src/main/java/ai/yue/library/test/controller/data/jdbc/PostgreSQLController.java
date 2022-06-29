package ai.yue.library.test.controller.data.jdbc;

import ai.yue.library.base.util.ListUtils;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.jdbc.ipo.PageIPO;
import ai.yue.library.test.dao.data.jdbc.PostgreSQLTestDAO;
import ai.yue.library.test.dataobject.jdbc.TableExampleTestDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PostgreSQL测试
 *
 * @author ylyue
 * @since 2022/6/29
 */
@RestController
@RequestMapping("/jdbc/postgreSQL")
public class PostgreSQLController {

    @Autowired
    PostgreSQLTestDAO postgreSQLTestDAO;

    @PostMapping("/insert")
    public Result<?> insert(TableExampleTestDO tableExampleTestDO) {
        return R.success(postgreSQLTestDAO.insert(tableExampleTestDO));
    }

    @PostMapping("/insertAndReturnUuid")
    public Result<?> insertAndReturnUuid(TableExampleTestDO tableExampleTestDO) {
        return R.success(postgreSQLTestDAO.insertAndReturnUuid(tableExampleTestDO));
    }

    @PostMapping("/insertBatch")
    public Result<?> insertBatch(List<TableExampleTestDO> tableExampleTestDOList) {
        postgreSQLTestDAO.insertBatch(ListUtils.toJsonsT(tableExampleTestDOList));
        return R.success(tableExampleTestDOList);
    }

    @GetMapping("/listAll")
    public Result<?> listAll() {
        return R.success(postgreSQLTestDAO.listAll());
    }

    @GetMapping("/page")
    public Result<?> page() {
        return postgreSQLTestDAO.page(PageIPO.builder().page(1).limit(10).build()).toResult();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/deleteById")
    public Result<?> deleteById(@RequestParam("id") Long id) {
        postgreSQLTestDAO.deleteById(id);
        return R.success();
    }

}
