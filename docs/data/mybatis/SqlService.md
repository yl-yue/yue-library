## SqlService <!-- {docsify-ignore} -->
用于执行动态传入的 SQL，可用于 SQL 控制台等业务。

### SqlService 方法
```java
/**
 * <b>查询一行数据</b>
 *
 * @param sql       要执行的SQL查询
 * @param param     要绑定到查询的参数映射
 * @return 可以是一个正确的单行查询结果、或null、或查询结果是多条数据而引发的预期错误异常
 */
public JSONObject queryForJson(String dsName, String sql, @Nullable Map<String, Object> param);

/**
 * <b>查询一行数据</b>
 *
 * @param sql               要执行的SQL查询
 * @param param             要绑定到查询的参数映射
 * @param javaBeanClass     查询结果映射类型，只支持JavaBean类型
 * @return 可以是一个正确的单行查询结果、或null、或查询结果是多条数据而引发的预期错误异常
 */
public <T> T queryForJavaBean(String dsName, String sql, @Nullable Map<String, Object> param, Class<T> javaBeanClass);

/**
 * <b>查询多行数据</b>
 *
 * @param sql       要执行的查询SQL
 * @param param     要绑定到查询的参数映射
 * @return 多行查询结果
 */
public List<JSONObject> queryForJsonList(String dsName, String sql, @Nullable Map<String, Object> param);

/**
 * <b>查询多行数据</b>
 *
 * @param sql               要执行的查询SQL
 * @param param             要绑定到查询的参数映射
 * @param javaBeanClass     查询结果映射类型，只支持JavaBean类型
 * @return 多行查询结果
 */
public <T> List<T> queryForJavaBeanList(String dsName, String sql, @Nullable Map<String, Object> param, Class<T> javaBeanClass);

/**
 * <b>查询一个数据</b>
 *
 * @param sql               要执行的SQL查询
 * @param param             要绑定到查询的参数映射
 * @param simpleTypeClass   查询结果映射类型，只支持简单类型（如：Long, String, Boolean、BigDecimal、LocalDateTime）
 * @return 可以是一个正确的单个查询结果、或null、或查询结果是多条数据而引发的预期错误异常
 */
public <T> T queryForSimpleType(String dsName, String sql, @Nullable Map<String, Object> param, Class<T> simpleTypeClass);

// ========== 执行 sql ==========

/**
 * 执行 sql 语句，返回一行结果
 * - 支持 select、insert、update、delete 语句
 * - 支持 ddl 语句
 *
 * @param sql       要执行的 sql 语句
 * @param param     要绑定的参数
 * @return 返回一行结果，可以是一行查询结果，也可以是 ddl 语句执行的结果
 */
public JSONObject execSqlForJson(String dsName, String sql, @Nullable Map<String, Object> param);

/**
 * 执行 sql 语句，返回多行结果
 * - 支持 select、insert、update、delete 语句
 * - 支持 ddl 语句
 *
 * @param sql       要执行的 sql 语句
 * @param param     要绑定的参数
 * @return 返回多行结果，可以是多行查询结果，也可以是多个 ddl 语句执行的结果
 */
public List<JSONObject> execSqlForJsonList(String dsName, String sql, @Nullable Map<String, Object> param);

/**
 * 执行多条 sql 语句，返回多个执行结果
 * - 支持 select、insert、update、delete 语句
 * - 支持 ddl 语句
 *
 * @param sql       要执行的多条 sql 语句
 * @param param     要绑定的参数
 * @param pageIPO   分页请求参数，亦可作分页限制（避免用户一次性查询所有数据，如果 sql 中已包含分页，此参数将被忽略）
 * @return 返回多个执行结果
 */
public List<MultiSqlExecResult> execMultiSqlForJsonList(String dsName, String sql, @Nullable Map<String, Object> param, @Nullable PageIPO pageIPO);
```

> 配置 `mybatis-plus.configuration.call-setters-on-nulls = true` 解决 null 值映射问题，可返回 null 字段

### 使用示例
```java
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
                SELECT * FROM table_example_standard;
                SELECT * FROM table_example_standard LIMIT 1;
                SELECT * FROM table_example_eliminate;
                SELECT * FROM table_example_standard_error LIMIT 1;
                show databases;
                """;

        List<MultiSqlExecResult> execMultiSqlForJsonList = sqlService.execMultiSqlForJsonList("mysql", sqlList, null, PageIPO.builder().pageNum(1).pageSize(5).build());
        return R.success(execMultiSqlForJsonList);
    }

}
```
