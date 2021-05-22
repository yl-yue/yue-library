package ai.yue.library.data.jdbc.client;

import ai.yue.library.base.exception.DbException;
import ai.yue.library.base.util.ClassUtils;
import ai.yue.library.base.util.ListUtils;
import ai.yue.library.base.util.MapUtils;
import ai.yue.library.base.util.StringUtils;
import ai.yue.library.base.view.ResultPrompt;
import ai.yue.library.data.jdbc.client.dialect.Dialect;
import ai.yue.library.data.jdbc.config.properties.DataEncrypt;
import ai.yue.library.data.jdbc.config.properties.JdbcProperties;
import ai.yue.library.data.jdbc.constant.DbConstant;
import ai.yue.library.data.jdbc.constant.EncryptAlgorithmEnum;
import ai.yue.library.data.jdbc.support.BeanPropertyRowMapper;
import ai.yue.library.data.jdbc.support.ColumnMapRowMapper;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.lang.Nullable;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <h2>SQL优化型数据库操作</h2>
 * Created by sunJinChuan on 2016/6/6
 *
 * @author ylyue
 * @since 0.0.1
 */
@Slf4j
@Getter
@Setter
@ToString
public class DbBase {

    // 必须初始化变量

    protected Dialect dialect;

    // 私有常量

    private static final String IS_PREFIX = "is";
    private static final String IS_PREFIX_FORMAT = "is_%s";

    // 方法

    /**
     * 获得数据源
     *
     * @return 数据源
     */
    public DataSource getDataSource() {
        return getJdbcTemplate().getDataSource();
    }

    /**
     * 获得Druid数据源
     *
     * @return Druid数据源
     */
//    public DruidDataSource getDruidDataSource() {
//        DataSource dataSource = getDataSource();
//        DruidDataSource druidDataSource = null;
//        try {
//            if (dataSource instanceof DruidDataSource) {
//                druidDataSource = (DruidDataSource) dataSource;
//            } else {
//                druidDataSource = (DruidDataSource) ((DruidPooledConnection) dataSource.getConnection()).getConnectionHolder().getDataSource();
//            }
//        } catch (Exception e) {
//            // ignore
//        }
//
//        return druidDataSource;
//    }

    /**
     * 设置数据源
     *
     * @param dataSource 数据源
     */
    public void setDataSource(DataSource dataSource) {
        getJdbcTemplate().setDataSource(dataSource);
    }

    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return dialect.getNamedParameterJdbcTemplate();
    }

    public JdbcTemplate getJdbcTemplate() {
        return getNamedParameterJdbcTemplate().getJdbcTemplate();
    }

    public JdbcProperties getJdbcProperties() {
        return dialect.getJdbcProperties();
    }

    public <T> RowMapper<T> getRowMapper(Class<T> mappedClass, DbBase dbBase, String... tableNames) {
        RowMapper<T> rowMapper;
        if (mappedClass == null || Map.class.isAssignableFrom(mappedClass)) {
            rowMapper = (RowMapper<T>) new ColumnMapRowMapper(dbBase, tableNames);
        } else {
            rowMapper = ClassUtils.isSimpleValueType(mappedClass) ? SingleColumnRowMapper.newInstance(mappedClass) : new BeanPropertyRowMapper(mappedClass, dbBase, tableNames);
        }

        return rowMapper;
    }

    /**
     * <b>多行查询结果转换为单行查询结果</b>
     * <p>为 {@linkplain DbQuery#queryForObject(String, JSONObject, Class)} 提供安全的查询结果获取</p>
     * <p>为 {@linkplain DbQuery#queryForJson(String, JSONObject)} 提供安全的查询结果获取</p>
     *
     * @param list 多行查询结果
     * @return 根据如下规则，返回正确的单行查询结果：
     * <ol>
     *     <li>size < 1 return null</li>
     *     <li>size = 1 return list.get(0)</li>
     *     <li>size > 1 throw Exception</li>
     * </ol>
     */
    public <T> T listResultToGetResult(List<T> list) {
        int size = list.size();
        int expectedValue = 1;
        if (size != expectedValue) {
            if (size > expectedValue) {
                throw new DbException(ResultPrompt.dataStructure(expectedValue, size), true);
            }

            return null;
        }

        return list.get(0);
    }

    // queryFor

    /**
     * <b>查询一行数据</b>
     * <p>对 {@linkplain NamedParameterJdbcTemplate#queryForMap(String, Map)} 方法的优化实现</p>
     *
     * @param sql       要执行的SQL查询
     * @param paramJson 要绑定到查询的参数映射
     * @return 可以是一个正确的单行查询结果、或null、或查询结果是多条数据而引发的预期错误异常
     */
    public JSONObject queryForJson(String sql, JSONObject paramJson) {
        return queryForObject(sql, paramJson, null);
    }

    /**
     * <b>查询一行数据</b>
     * <p>对 {@linkplain NamedParameterJdbcTemplate#queryForObject(String, Map, org.springframework.jdbc.core.RowMapper)} 方法的优化实现</p>
     *
     * @param sql         要执行的SQL查询
     * @param paramJson   要绑定到查询的参数映射
     * @param mappedClass 查询结果映射类型，支持JavaBean与简单类型（如：Long, String, Boolean）
     * @return 可以是一个正确的单行查询结果、或null、或查询结果是多条数据而引发的预期错误异常
     */
    public <T> T queryForObject(String sql, JSONObject paramJson, Class<T> mappedClass) {
        List<T> list = queryForList(sql, paramJson, mappedClass);
        return listResultToGetResult(list);
    }

    /**
     * <b>查询多行数据</b>
     * <p>同 {@linkplain NamedParameterJdbcTemplate#queryForRowSet(String, Map)}</p>
     * <p>不支持数据自动解密</p>
     *
     * @param sql       要执行的SQL查询
     * @param paramJson 要绑定到查询的参数映射
     * @return 可用于方便的获取各种数据类型的结果集
     */
    public SqlRowSet queryForRowSet(String sql, JSONObject paramJson) {
        aopBefore(sql, null, paramJson);
        return getNamedParameterJdbcTemplate().queryForRowSet(sql, paramJson);
    }

    /**
     * <b>查询多行数据</b>
     * <p>对 {@link NamedParameterJdbcTemplate#queryForList(String, Map)} 方法的优化实现</p>
     *
     * @param sql       要执行的查询SQL
     * @param paramJson 要绑定到查询的参数映射
     * @return 多行查询结果
     */
    public List<JSONObject> queryForList(String sql, JSONObject paramJson) {
        return queryForList(sql, paramJson, null);
    }

    /**
     * <b>查询多行数据</b>
     * <p>对 {@linkplain NamedParameterJdbcTemplate#queryForList(String, Map, Class)} 方法的优化实现</p>
     *
     * @param sql         要执行的查询SQL
     * @param paramJson   要绑定到查询的参数映射
     * @param mappedClass 查询结果映射类型，支持JavaBean与简单类型（如：Long, String, Boolean）
     * @return 多行查询结果
     */
    public <T> List<T> queryForList(String sql, JSONObject paramJson, Class<T> mappedClass) {
        aopBefore(sql, null, paramJson);
        String[] tables = extractTables(sql);
        List<T> resultList = getNamedParameterJdbcTemplate().query(sql, paramJson, getRowMapper(mappedClass, this, tables));
//        if (mappedClass != null && CharSequence.class.isAssignableFrom(mappedClass) && resultList.size() == 1) {
//            List<SchemaStatVisitor> schemaStatVisitorList = extractSchemaStatVisitor(sql);
//            TableStat.Column column = ((ArrayList<TableStat.Column>) schemaStatVisitorList.get(0).getColumns()).get(0);
//            String columnName = column.getName();
//            JSONObject resultJson = new JSONObject();
//            resultJson.put(columnName, resultList.get(0));
//            aopAfter(tables, resultJson);
//            String resultDataStr = resultJson.getString(columnName);
//            resultList.clear();
//            resultList.add((T) resultDataStr);
//        }

        return resultList;
    }

    // is

    /**
     * 判断更新所影响的行数是否 <b>等于</b> 预期值
     * <p>若不是预期值，同时 updateRowsNumber &gt; 0 那么将会抛出一个 {@linkplain DbException}
     *
     * @param updateRowsNumber 更新所影响的行数
     * @param expectedValue    预期值
     * @return 是否 <b>等于</b> 预期值
     */
    public boolean isUpdateAndExpectedEqual(long updateRowsNumber, int expectedValue) {
        if (updateRowsNumber == expectedValue) {
            return true;
        }
        if (updateRowsNumber == 0) {
            return false;
        }

        String msg = ResultPrompt.dataStructure(expectedValue, updateRowsNumber);
        throw new DbException(msg);
    }

    /**
     * 判断更新所影响的行数是否 <b>大于等于</b> 预期值
     * <p>
     * 若不是预期结果，同时 updateRowsNumber &lt; expectedValue 那么将会抛出一个{@linkplain DbException}
     *
     * @param updateRowsNumber 更新所影响的行数
     * @param expectedValue    预期值
     * @return 是否 <b>大于等于</b> 预期值
     */
    public boolean isUpdateAndExpectedGreaterThanEqual(long updateRowsNumber, int expectedValue) {
        if (updateRowsNumber >= expectedValue) {
            return true;
        }
        if (updateRowsNumber == 0) {
            return false;
        }

        String msg = ResultPrompt.dataStructure(">= " + expectedValue, updateRowsNumber);
        throw new DbException(msg);
    }

    /**
     * 判断更新所影响的行数是否 <b>等于</b> 预期值
     * <p>
     * 若不是预期值，那么将会抛出一个{@linkplain DbException}
     *
     * @param updateRowsNumber 更新所影响的行数
     * @param expectedValue    预期值
     */
    public void updateAndExpectedEqual(long updateRowsNumber, int expectedValue) {
        if (updateRowsNumber != expectedValue) {
            String msg = ResultPrompt.dataStructure(expectedValue, updateRowsNumber);
            throw new DbException(msg);
        }
    }

    /**
     * 判断更新所影响的行数是否 <b>大于等于</b> 预期值
     * <p>
     * 若不是预期结果，那么将会抛出一个{@linkplain DbException}
     *
     * @param updateRowsNumber 更新所影响的行数
     * @param expectedValue    预期值
     */
    public void updateAndExpectedGreaterThanEqual(long updateRowsNumber, int expectedValue) {
        if (updateRowsNumber < expectedValue) {
            String msg = ResultPrompt.dataStructure(">= " + expectedValue, updateRowsNumber);
            throw new DbException(msg);
        }
    }

    /**
     * 确认批量更新每组参数所影响的行数，是否 <b>全部都等于</b> 同一个预期值
     * <p>
     * 若不是预期值，那么将会抛出一个{@linkplain DbException}
     *
     * @param updateRowsNumberArray 每组参数更新所影响的行数数组
     * @param expectedValue         预期值
     */
    public void updateBatchAndExpectedEqual(int[] updateRowsNumberArray, int expectedValue) {
        for (int updateRowsNumber : updateRowsNumberArray) {
            if (updateRowsNumber != expectedValue) {
                String msg = ResultPrompt.UPDATE_BATCH_ERROR;
                msg += ResultPrompt.dataStructure(expectedValue, updateRowsNumber);
                throw new DbException(msg);
            }
        }
    }

    // WHERE SQL

    private synchronized void paramToWhereSql(StringBuffer whereSql, final JSONObject paramJson, final String condition) {
        whereSql.append(" AND ");
        whereSql.append(dialect.getWrapper().wrap(condition));
        var value = paramJson.get(condition);
        if (null == value) {
            whereSql.append(" IS :");
            whereSql.append(condition);
        } else if (value instanceof Collection || ArrayUtil.isArray(value)) {
            whereSql.append(" IN (:");
            whereSql.append(condition);
            whereSql.append(") ");
            if (ArrayUtil.isArray(value)) {
                paramJson.replace(condition, ListUtils.toList((Object[]) value));
            }
        } else {
            whereSql.append(" = :");
            whereSql.append(condition);
        }
    }

    /**
     * <b>绝对条件查询参数whereSql化</b>
     * <p>
     * <i>已对 NULL 值进行特殊处理（IS NULL）</i><br><br>
     * <i>已对 {@linkplain List} 类型值进行特殊处理（IN (?, ?)）</i><br><br>
     *
     * <b>结果示例：</b><br>
     * <blockquote>
     * <pre>
     * <code>WHERE 1 = 1</code><br>
     * <code>AND</code><br>
     * <code>param1 = :param1</code><br>
     * <code>AND</code><br>
     * <code>param2 IS NULL :param2</code><br>
     * <code>AND</code><br>
     * <code>param3 IN :param3</code><br>
     * <code>AND ...</code>
     * </pre>
     * </blockquote>
     *
     * @param paramJson  参数
     * @param conditions where条件（对应paramJson key）
     * @return whereSql
     */
    public String paramToWhereSql(JSONObject paramJson, String... conditions) {
        StringBuffer whereSql = new StringBuffer();
        whereSql.append(" WHERE 1 = 1 ");
        if (ArrayUtil.isNotEmpty(conditions)) {
            for (String condition : conditions) {
                paramToWhereSql(whereSql, paramJson, condition);
            }
        }

        return whereSql.toString();
    }

    /**
     * <b>绝对条件查询参数whereSql化</b>
     * <p>
     * <i>已对 NULL 值进行特殊处理（IS NULL）</i><br><br>
     * <i>已对 {@linkplain List} 类型值进行特殊处理（IN (?, ?)）</i><br><br>
     *
     * <b>结果示例：</b><br>
     * <blockquote>
     * <pre>
     * <code>WHERE 1 = 1</code><br>
     * <code>AND</code><br>
     * <code>param1 = :param1</code><br>
     * <code>AND</code><br>
     * <code>param2 IS NULL :param2</code><br>
     * <code>AND</code><br>
     * <code>param3 IN :param3</code><br>
     * <code>AND ...</code>
     * </pre>
     * </blockquote>
     *
     * @param paramJson 参数
     * @return whereSql
     */
    public String paramToWhereSql(JSONObject paramJson) {
        StringBuffer whereSql = new StringBuffer(getDeleteWhereSql());
        paramJson.keySet().forEach(condition -> paramToWhereSql(whereSql, paramJson, condition));
        return whereSql.toString();
    }

    /**
     * 获得逻辑删除条件SQL
     *
     * @return 携带WHERE关键字的逻辑删除条件SQL
     */
    public String getDeleteWhereSql() {
        StringBuffer whereSql = new StringBuffer();
        if (getJdbcProperties().isEnableDeleteQueryFilter()) {
            whereSql.append(" WHERE ")
            .append(DbConstant.FIELD_DEFINITION_DELETE_TIME)
            .append(" = ")
            .append(DbConstant.FIELD_DEFAULT_VALUE_DELETE_TIME);
        } else {
            whereSql.append(" WHERE 1 = 1 ");
        }

        return whereSql.toString();
    }

    // ParamValidate

    /**
     * 参数验证
     *
     * @param tableName 表名
     */
    protected void paramValidate(String tableName) {
        if (StringUtils.isEmpty(tableName)) {
            throw new DbException("表名不能为空");
        }
    }

    /**
     * 参数验证
     *
     * @param tableName 表名
     * @param whereSql  条件sql
     */
    protected void paramValidate(String tableName, String whereSql) {
        if (StringUtils.isEmpty(tableName)) {
            throw new DbException("表名不能为空");
        }
        if (StringUtils.isEmpty(whereSql)) {
            throw new DbException("whereSql不能为空");
        }
    }

    /**
     * 参数验证
     *
     * @param tableName 表名
     * @param id        主键ID
     */
    protected void paramValidate(String tableName, Long id) {
        if (StringUtils.isEmpty(tableName)) {
            throw new DbException("表名不能为空");
        }
        if (null == id) {
            throw new DbException("参数id不能为空");
        }
    }

    /**
     * 参数验证
     *
     * @param tableName   表名
     * @param columnNames 列名
     */
    protected void paramValidate(String tableName, String... columnNames) {
        if (StringUtils.isEmpty(tableName)) {
            throw new DbException("表名不能为空");
        }
        if (StringUtils.isEmptys(columnNames)) {
            throw new DbException("条件列名不能为空");
        }
    }

    /**
     * 参数验证
     *
     * @param tableName 表名
     * @param id        主键ID
     * @param fieldName 字段名称
     */
    protected void paramValidate(String tableName, Long id, String[] fieldName) {
        if (StringUtils.isEmpty(tableName)) {
            throw new DbException("表名不能为空");
        }
        if (null == id) {
            throw new DbException("参数id不能为空");
        }
        if (StringUtils.isEmptys(fieldName)) {
            throw new DbException("fieldName不能为空");
        }
    }

    /**
     * 参数验证
     *
     * @param tableName 表名
     * @param paramJson 参数
     */
    protected void paramValidate(String tableName, JSONObject paramJson) {
        if (StringUtils.isEmpty(tableName)) {
            throw new DbException("表名不能为空");
        }
        if (MapUtils.isEmpty(paramJson)) {
            throw new DbException("参数不能为空");
        }
    }

    /**
     * 参数验证
     *
     * @param tableName  表名
     * @param paramJsons 参数数组
     */
    protected void paramValidate(String tableName, JSONObject[] paramJsons) {
        if (StringUtils.isEmpty(tableName)) {
            throw new DbException("表名不能为空");
        }
        if (MapUtils.isEmptys(paramJsons)) {
            throw new DbException("参数不能为空");
        }
    }

    /**
     * 参数验证
     *
     * @param tableName  表名
     * @param paramJson  参数
     * @param conditions 条件（对应paramJson key）
     */
    protected void paramValidate(String tableName, JSONObject paramJson, String[] conditions) {
        if (StringUtils.isEmpty(tableName)) {
            throw new DbException("表名不能为空");
        }
        if (MapUtils.isEmpty(paramJson)) {
            throw new DbException("参数不能为空");
        }
        if (StringUtils.isEmptys(conditions) || !MapUtils.isKeys(paramJson, conditions)) {
            throw new DbException("更新条件不能为空");
        }
    }

    // paramFormat

    /**
     * 参数美化（对SpringJDBC不支持的类型进行转换与布尔值映射识别）
     * <p>Character 转 String</p>
     * <p>JSONObject 转 JsonString</p>
     * <p>LocalDataTime 转 Date</p>
     *
     * @param paramJson 需要进行类型处理的paramJson
     */
    public void paramFormat(JSONObject paramJson) {
        if (MapUtils.isEmpty(paramJson)) {
            return;
        }

        JSONObject paramFormatJson = new JSONObject();
        for (Map.Entry<String, Object> entry : paramJson.entrySet()) {
            // 1. 参数确认
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value == null) {
                paramFormatJson.put(key, value);
                continue;
            }

            // 2. 参数美化
            String formatKey = key;
            Object formatValue = value;
            Class<?> valueClass = value.getClass();
            if (valueClass == JSONObject.class) {
                formatValue = ((JSONObject) value).toJSONString();
            } else if (valueClass == JSONArray.class) {
                formatValue = ((JSONArray) value).toJSONString();
            } else if (valueClass == Character.class || valueClass == LocalDateTime.class) {
                formatValue = value.toString();
            } else if (value instanceof List && ListUtils.isNotEmpty(Collections.singletonList(value))) {
                Object assertMap = ((List<?>) value).get(0);
                if (assertMap instanceof Map) {
                    formatValue = JSONObject.toJSONString(value);
                }
            }

            // 3. 布尔值映射识别
            boolean enableBooleanMapRecognition = getJdbcProperties().isEnableBooleanMapRecognition();
            boolean isBoolean = BooleanUtil.isBoolean(valueClass);
            boolean isStrBoolean = false;
            if (!isBoolean && valueClass == String.class) {
                isStrBoolean = StrUtil.equalsAnyIgnoreCase((String) value, "true", "false");
            }
            if (enableBooleanMapRecognition && isBoolean || isStrBoolean) {
                if (!StrUtil.startWith(key, IS_PREFIX, true, true)) {
                    String aliasFormat = String.format(IS_PREFIX_FORMAT, PropertyNamingStrategy.SnakeCase.translate(key));
                    formatKey = getJdbcProperties().getDatabaseFieldNamingStrategy().getPropertyNamingStrategy().translate(aliasFormat);
                    if (!BooleanUtil.isBoolean(valueClass)) {
                        formatValue = Boolean.parseBoolean((String) value);
                    }
                }
            }

            // 4. 设置处理后的值
            paramFormatJson.put(formatKey, formatValue);
        }

        paramJson.fluentClear().putAll(paramFormatJson);
    }

    // ========== 数据脱敏、JDBC审计 ==========

//    protected List<SchemaStatVisitor> extractSchemaStatVisitor(String sql) {
//        DruidDataSource druidDataSource = getDruidDataSource();
//        DbType dbType = DbType.of(druidDataSource.getDbType());
//        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, dbType);
//        List<SchemaStatVisitor> schemaStatVisitorList = new ArrayList<>();
//        for (SQLStatement sqlStatement : sqlStatements) {
//            SchemaStatVisitor visitor = new SchemaStatVisitor();
//            sqlStatement.accept(visitor);
//            schemaStatVisitorList.add(visitor);
//        }
//
//        return schemaStatVisitorList;
//    }

    protected String[] extractTables(String sql) {
        return null;
//        List<SchemaStatVisitor> schemaStatVisitorList = extractSchemaStatVisitor(sql);
//        List<String> tableNameList = new ArrayList<>();
//        for (SchemaStatVisitor schemaStatVisitor : schemaStatVisitorList) {
//            Set<TableStat.Name> names = schemaStatVisitor.getTables().keySet();
//            Iterator<TableStat.Name> iterator = names.iterator();
//            List<String> currentTableNameList = new ArrayList<>();
//            while (iterator.hasNext()) {
//                currentTableNameList.add(iterator.next().getName());
//            }
//
//            tableNameList.addAll(currentTableNameList);
//        }
//
//        String[] tableNames = new String[tableNameList.size()];
//        return tableNameList.toArray(tableNames);
    }

    /**
     * 动作执行前
     *
     * @param tableName 表名
     * @param paramJson 参数
     */
    protected void aopBefore(@Nullable String sql, @Nullable String tableName, @Nullable JSONObject paramJson) {
        JSONObject[] paramJsons = {paramJson};
        aopBefore(sql, tableName, paramJsons);
    }

    /**
     * 动作执行前
     *
     * @param tableName  表名
     * @param paramJsons 参数
     */
    protected void aopBefore(@Nullable String sql, @Nullable String tableName, @Nullable JSONObject[] paramJsons) {
        // 数据加密
//        if (tableName == null) {
//            String[] names = extractTables(sql);
//            for (String name : names) {
//                dataEncryptOrDecrypt(name, paramJsons, true);
//            }
//        } else {
//            dataEncryptOrDecrypt(tableName, paramJsons, true);
//        }

        // 审计
    }

    /**
     * 动作执行后（这是一个不适用于业务中调用的方法，请忽略）
     *
     * @param tableNames 表名
     * @param resultJson 单行结果数据
     */
    public void aopAfter(String[] tableNames, JSONObject resultJson) {
//        JSONObject[] resultJsons = {resultJson};
//        for (String tableName : tableNames) {
//            aopAfter(null, tableName, resultJsons);
//        }
    }

    /**
     * 动作执行后
     *
     * @param tableName  表名
     * @param resultJson 单行结果数据
     */
    protected void aopAfter(@Nullable String sql, @Nullable String tableName, @Nullable JSONObject resultJson) {
        JSONObject[] resultJsons = {resultJson};
        aopAfter(sql, tableName, resultJsons);
    }

    /**
     * 动作执行后
     *
     * @param tableName   表名
     * @param resultJsons 多行结果数据
     */
    protected void aopAfter(@Nullable String sql, @Nullable String tableName, @Nullable JSONObject[] resultJsons) {
        // 数据解密
//        if (tableName == null) {
//            String[] names = extractTables(sql);
//            for (String name : names) {
//                dataEncryptOrDecrypt(name, resultJsons, false);
//            }
//        } else {
//            dataEncryptOrDecrypt(tableName, resultJsons, false);
//        }

        // 审计
    }

    /**
     * 数据加密或解密
     *
     * @param tableName  表名
     * @param paramJsons 参数
     * @param encrypt    true == 加密，false == 解密
     */
    private void dataEncryptOrDecrypt(String tableName, JSONObject[] paramJsons, boolean encrypt) {
        List<DataEncrypt> dataEncryptConfigList = getJdbcProperties().getDataEncryptConfigs();
        if (ListUtils.isEmpty(dataEncryptConfigList)) {
            return;
        }

        for (DataEncrypt dataEncryptConfig : dataEncryptConfigList) {
            String dataEncryptTableName = dataEncryptConfig.getTableName();
            if (tableName.equalsIgnoreCase(dataEncryptTableName) == false) {
                continue;
            }

            // 初始化加密算法与密钥，表级配置未配置时，默认使用Bean级配置
            EncryptAlgorithmEnum dataEncryptAlgorithm = dataEncryptConfig.getDataEncryptAlgorithm();
            String dataEncryptKey = dataEncryptConfig.getDataEncryptKey();
            if (dataEncryptAlgorithm == null) {
                dataEncryptAlgorithm = getJdbcProperties().getDataEncryptAlgorithm();
            }
            if (dataEncryptKey == null) {
                dataEncryptKey = getJdbcProperties().getDataEncryptKey();
            }

            // 字段加密
            List<String> fieldNameList = dataEncryptConfig.getFieldNames();
            for (String fieldName : fieldNameList) {
                for (JSONObject paramJson : paramJsons) {
                    String data = paramJson.getString(fieldName);
                    if (encrypt == true) {
                        if (data == null) {
                            log.warn("【字段加密】未能获得加密数据，当前加密表：{}，加密字段：{}", tableName, fieldName);
                            continue;
                        }

                        String encryptedBase64 = dataEncryptAlgorithm.getSymmetricCrypto(dataEncryptKey).encryptBase64(data);
                        paramJson.replace(fieldName, encryptedBase64);
                        log.debug("【字段加密】当前加密表：{}，加密字段：{}，加密前数据：{}，加密后数据：{}", tableName, fieldName, data, encryptedBase64);
                    } else {
                        if (data == null) {
                            log.warn("【字段解密】未能获得解密数据，当前解密表：{}，解密字段：{}", tableName, fieldName);
                            continue;
                        }

                        String decryptStr = dataEncryptAlgorithm.getSymmetricCrypto(dataEncryptKey).decryptStr(data);
                        paramJson.replace(fieldName, decryptStr);
                        log.debug("【字段解密】当前解密表：{}，解密字段：{}，解密前数据：{}，解密后数据：{}", tableName, fieldName, data, decryptStr);
                    }
                }
            }

            break;
        }
    }

    protected void audit(String tableName, JSONObject paramJson) {
        List<String> auditTableNames = getJdbcProperties().getAuditTableNames();
        if (ListUtils.isNotEmpty(auditTableNames)) {
            for (String auditTableName : auditTableNames) {
                if (tableName.equalsIgnoreCase(auditTableName)) {
                    paramJson.put("","");
                }
            }
        }
    }

}
