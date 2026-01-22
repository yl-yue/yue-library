package ai.yue.library.test.controller.data.mybatis;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.mybatis.dto.MultiSqlExecResult;
import ai.yue.library.data.mybatis.dto.PageIPO;
import ai.yue.library.data.mybatis.service.SqlService;
import ai.yue.library.test.entity.TableExampleStandard;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/data/mybatis/sqlService")
public class SqlServiceController {

    @Resource
    SqlService sqlService;

    @PostMapping("/test")
    public Result<?> test() {
        String sql = """
                SELECT * FROM table_example_standard LIMIT 1
                """;

        String sqlMap = """
                SELECT * FROM table_example_standard WHERE id = #{param.id}
                """;
        JSONObject param = new JSONObject();
        param.put("id", 0);

        String sqlList = """
                SELECT * FROM table_example_standard LIMIT 1;
                SELECT * FROM table_example_standard LIMIT 1;
                SELECT * FROM table_example_standard_error LIMIT 1;
                show databases;
                """;

        JSONObject queryForJson = sqlService.queryForJson("mysql", sql, null);
        TableExampleStandard queryForJavaBean = sqlService.queryForJavaBean("mysql", sql, null, TableExampleStandard.class);
        List<JSONObject> queryForJsonList = sqlService.queryForJsonList("mysql", sqlMap, param);
        List<TableExampleStandard> queryForJavaBeanList = sqlService.queryForJavaBeanList("mysql", sql, null, TableExampleStandard.class);
        Long queryForSimpleType = sqlService.queryForSimpleType("mysql", sql, null, Long.class);
        List<MultiSqlExecResult> execMultiSqlForJsonList = sqlService.execMultiSqlForJsonList("mysql", sqlList, null, null);
        JSONObject result = new JSONObject();
        result.put("queryForJson", queryForJson);
        result.put("queryForJavaBean", queryForJavaBean);
        result.put("queryForJsonList", queryForJsonList);
        result.put("queryForJavaBeanList", queryForJavaBeanList);
        result.put("queryForSimpleType", queryForSimpleType);
        result.put("execMultiSqlForJsonList", execMultiSqlForJsonList);
        return R.success(result);
    }

    @PostMapping("/ds")
    public Result<?> ds(String dsName) {
        String sql = """
                SHOW databases;
                """;

        List<JSONObject> dsNameResult = sqlService.queryForJsonList(dsName, sql, null);
        List<JSONObject> bd_dinky = sqlService.queryForJsonList("bd_dinky", sql, null);
        List<JSONObject> doris = sqlService.queryForJsonList("doris", sql, null);

        JSONObject result = new JSONObject();
        result.put(dsName, dsNameResult);
        result.put("bd_dinky", bd_dinky);
        result.put("doris", doris);
        return R.success(result);
    }

    @PostMapping("/execMultiSqlForJsonList")
    public Result<?> execMultiSqlForJsonList() {
        String sqlList = """
                SELECT * FROM table_id_alias;
                SELECT * FROM table_id_alias LIMIT 1;
                SELECT * FROM table_example_eliminate;
                SELECT * FROM table_example_standard_error LIMIT 1;
                show databases;
                """;

        List<MultiSqlExecResult> execMultiSqlForJsonList = sqlService.execMultiSqlForJsonList("mysql", sqlList, null, PageIPO.builder().pageNum(1).pageSize(5).build());
        return R.success(execMultiSqlForJsonList);
    }

}
