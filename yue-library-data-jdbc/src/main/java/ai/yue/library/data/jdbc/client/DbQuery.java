package ai.yue.library.data.jdbc.client;

import ai.yue.library.base.constant.SortEnum;
import ai.yue.library.base.exception.DbException;
import ai.yue.library.base.util.MapUtils;
import ai.yue.library.base.util.StringUtils;
import ai.yue.library.data.jdbc.constant.DbConstant;
import ai.yue.library.data.jdbc.ipo.Page;
import ai.yue.library.data.jdbc.ipo.PageIPO;
import ai.yue.library.data.jdbc.vo.PageBeforeAndAfterVO;
import ai.yue.library.data.jdbc.vo.PageVO;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

/**
 * <h2>SQL优化型数据库操作</h2>
 * Created by sunJinChuan on 2016/6/6
 *
 * @since 0.0.1
 */
@Slf4j
class DbQuery extends DbJdbcTemplate {

    // query

    /**
     * 同 {@linkplain NamedParameterJdbcTemplate#queryForObject(String, Map, Class)}
     * <p>指定SQL语句以创建预编译执行SQL和绑定查询参数，结果映射应该是一个单行查询否则结果为null。
     *
     * @deprecated 请使用：{@linkplain #queryForObject(String, JSONObject, Class)}
     * @param sql         要执行的SQL查询
     * @param paramJson   要绑定到查询的参数映射
     * @param mappedClass 结果对象期望匹配的普通类型
     * @return 所需普通类型的结果对象（如：Long, String, Boolean）或null
     */
    @Deprecated
    public <T> T queryObject(String sql, JSONObject paramJson, Class<T> mappedClass) {
        try {
            return getNamedParameterJdbcTemplate().queryForObject(sql, paramJson, mappedClass);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    // is

    /**
     * 是否有数据
     *
     * @deprecated 请使用：{@linkplain #isExistData(String, JSONObject)}
     * @param tableName 表名
     * @param paramJson 查询参数
     * @return 是否有数据
     */
    @Deprecated
    public boolean isDataSize(String tableName, JSONObject paramJson) {
        return isExistData(tableName, paramJson);
    }
    
    /**
     * 是否有数据
     *
     * @param tableName 表名
     * @param paramJson 查询参数
     * @return 是否有数据
     */
    public boolean isExistData(String tableName, JSONObject paramJson) {
        paramValidate(tableName, paramJson);
        aopBefore(null, tableName, paramJson);
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT count(*) FROM ");
        sql.append(dialect.getWrapper().wrap(tableName));
        String whereSql = paramToWhereSql(paramJson);
        sql.append(whereSql);
        Long count = queryForObject(sql.toString(), paramJson, Long.class);
        return count > 0;
    }

    // get

    private String querySqlBuild(String tableName, JSONObject paramJson, SortEnum sortEnum) {
        paramValidate(tableName, paramJson);
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ");
        sql.append(dialect.getWrapper().wrap(tableName));
        String whereSql = paramToWhereSql(paramJson);
        sql.append(whereSql);
        if (sortEnum == SortEnum.ASC) {// 升序
            sql.append(" ORDER BY id");
        } else if (sortEnum == SortEnum.DESC) {// 降序
            sql.append(" ORDER BY id DESC");
        }

        return sql.toString();
    }

    private String getByColumnNameSqlBuild(String tableName, String columnName) {
        paramValidate(tableName);
        if (StringUtils.isEmpty(columnName)) {
            throw new DbException("条件列名不能为空");
        }

        tableName = dialect.getWrapper().wrap(tableName);
        columnName = dialect.getWrapper().wrap(columnName);
        StringBuffer sql = new StringBuffer("SELECT * FROM ");
        sql.append(tableName);
        sql.append(" WHERE ").append(columnName).append(" = :").append(columnName);
        if (getJdbcProperties().isEnableDeleteQueryFilter()) {
            sql.append(" AND ").append(DbConstant.FIELD_DEFINITION_DELETE_TIME)
                    .append(" = ").append(DbConstant.FIELD_DEFAULT_VALUE_DELETE_TIME);
        }

        return sql.toString();
    }

    /**
     * 获得表的元数据
     * <p>检索元数据，即此行集合的列的数字、类型和属性。
     *
     * @param tableName 表名
     * @return Spring的SqlRowSet的元数据接口
     */
    public SqlRowSetMetaData getMetaData(String tableName) {
        tableName = dialect.getWrapper().wrap(tableName);
        StringBuffer sql = new StringBuffer("SELECT * FROM ").append(tableName).append(dialect.getPageJoinSql());
        return queryForRowSet(sql.toString(), Page.builder().page(0L).limit(0).build().toParamJson()).getMetaData();
    }

    /**
     * 获得总数
     *
     * @param tableName 表名
     * @return 总数
     */
    public long getCount(String tableName) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT count(*) count FROM ");
        sql.append(tableName);
        if (getJdbcProperties().isEnableDeleteQueryFilter()) {
            sql.append(getDeleteWhereSql());
        }
        return queryForObject(sql.toString(), null, Long.class);
    }

    /**
     * 单个-ById
     * <p>字段名=id，一般为表自增ID-主键</p>
     * <p><code style="color:red">依赖于接口传入 {@value DbConstant#PRIMARY_KEY} 参数时慎用此方法</code>，避免有序主键被遍历风险，造成数据越权行为。推荐使用 {@link #getByBusinessUk(String, String)}</p>
     *
     * @param tableName 表名
     * @param id        主键ID
     * @return 可以是一个正确的单行查询结果、或null、或查询结果是多条数据而引发的预期错误异常
     */
    public JSONObject getById(String tableName, long id) {
        return getById(tableName, id, null);
    }

    /**
     * 单个-ById
     * <p>字段名=id，一般为表自增ID-主键</p>
     * <p><code style="color:red">依赖于接口传入 {@value DbConstant#PRIMARY_KEY} 参数时慎用此方法</code>，避免有序主键被遍历风险，造成数据越权行为。推荐使用 {@link #getByBusinessUk(String, String, Class)}</p>
     *
     * @param tableName   表名
     * @param id          主键ID
     * @param mappedClass 查询结果映射类型，支持JavaBean与简单类型（如：Long, String, Boolean）
     * @return 可以是一个正确的单行查询结果、或null、或查询结果是多条数据而引发的预期错误异常
     */
    public <T> T getById(String tableName, Long id, Class<T> mappedClass) {
        paramValidate(tableName, id);
        String sql = getByColumnNameSqlBuild(tableName, DbConstant.PRIMARY_KEY);
        JSONObject paramJson = new JSONObject();
        paramJson.put(dialect.getWrapper().wrap(DbConstant.PRIMARY_KEY), id);
        return queryForObject(sql, paramJson, mappedClass);
    }

    /**
     * 单个-By业务键
     * <p>默认业务键为key
     * <p>业务键值推荐使用UUID5
     *
     * @param tableName       表名
     * @param businessUkValue 业务键的唯一值
     * @return 可以是一个正确的单行查询结果、或null、或查询结果是多条数据而引发的预期错误异常
     */
    public JSONObject getByBusinessUk(String tableName, String businessUkValue) {
        return getByBusinessUk(tableName, businessUkValue, null);
    }

    /**
     * 单个-By业务键
     * <p>默认业务键为key
     * <p>业务键值推荐使用UUID5
     *
     * @param tableName       表名
     * @param businessUkValue 业务键的唯一值
     * @param mappedClass     查询结果映射类型，支持JavaBean与简单类型（如：Long, String, Boolean）
     * @return 可以是一个正确的单行查询结果、或null、或查询结果是多条数据而引发的预期错误异常
     */
    public <T> T getByBusinessUk(String tableName, String businessUkValue, Class<T> mappedClass) {
        String sql = getByColumnNameSqlBuild(tableName, getJdbcProperties().getBusinessUk());
        JSONObject paramJson = new JSONObject();
        paramJson.put(dialect.getWrapper().wrap(getJdbcProperties().getBusinessUk()), businessUkValue);
        return queryForObject(sql, paramJson, mappedClass);
    }

    /**
     * 单个-绝对条件查询
     *
     * @param tableName 表名
     * @param paramJson 查询参数
     * @return 可以是一个正确的单行查询结果、或null、或查询结果是多条数据而引发的预期错误异常
     */
    public JSONObject get(String tableName, JSONObject paramJson) {
        return get(tableName, paramJson, null);
    }

    /**
     * 单个-绝对条件查询
     *
     * @param tableName   表名
     * @param paramJson   查询参数
     * @param mappedClass 查询结果映射类型，支持JavaBean与简单类型（如：Long, String, Boolean）
     * @return 可以是一个正确的单行查询结果、或null、或查询结果是多条数据而引发的预期错误异常
     */
    public <T> T get(String tableName, JSONObject paramJson, Class<T> mappedClass) {
        paramFormat(paramJson);
        aopBefore(null, tableName, paramJson);
        String sql = querySqlBuild(tableName, paramJson, null);
        return queryForObject(sql, paramJson, mappedClass);
    }

    // list

    /**
     * 列表-绝对条件查询
     *
     * @param tableName 表名
     * @param paramJson 查询参数
     * @return 列表数据
     */
    public List<JSONObject> list(String tableName, JSONObject paramJson) {
        return list(tableName, paramJson, (Class<JSONObject>) null);
    }

    /**
     * 列表-绝对条件查询
     *
     * @param <T>         泛型
     * @param tableName   表名
     * @param paramJson   查询参数
     * @param mappedClass 映射类
     * @return 列表数据
     */
    public <T> List<T> list(String tableName, JSONObject paramJson, Class<T> mappedClass) {
        paramFormat(paramJson);
        aopBefore(null, tableName, paramJson);
        String sql = querySqlBuild(tableName, paramJson, null);
        return queryForList(sql, paramJson, mappedClass);
    }

    /**
     * 列表-绝对条件查询
     *
     * @param tableName 表名
     * @param paramJson 查询参数
     * @param sortEnum  排序方式
     * @return 列表数据
     */
    public List<JSONObject> list(String tableName, JSONObject paramJson, SortEnum sortEnum) {
        return list(tableName, paramJson, sortEnum, null);
    }

    /**
     * 列表-绝对条件查询
     *
     * @param <T>         泛型
     * @param tableName   表名
     * @param paramJson   查询参数
     * @param sortEnum    排序方式
     * @param mappedClass 映射类
     * @return 列表数据
     */
    public <T> List<T> list(String tableName, JSONObject paramJson, SortEnum sortEnum, Class<T> mappedClass) {
        paramFormat(paramJson);
        aopBefore(null, tableName, paramJson);
        String sql = querySqlBuild(tableName, paramJson, sortEnum);
        return queryForList(sql, paramJson, mappedClass);
    }

    /**
     * 列表-查询表中所有数据
     *
     * @param tableName 表名
     * @return 列表数据
     */
    public List<JSONObject> listAll(String tableName) {
        return listAll(tableName, null);
    }

    /**
     * 列表-查询表中所有数据
     *
     * @param <T>         泛型
     * @param tableName   表名
     * @param mappedClass 映射类
     * @return 列表数据
     */
    public <T> List<T> listAll(String tableName, Class<T> mappedClass) {
        paramValidate(tableName);
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ");
        sql.append(dialect.getWrapper().wrap(tableName));
        if (getJdbcProperties().isEnableDeleteQueryFilter()) {
            sql.append(" WHERE ").append(DbConstant.FIELD_DEFINITION_DELETE_TIME)
                    .append(" = ").append(DbConstant.FIELD_DEFAULT_VALUE_DELETE_TIME);
        }

        return queryForList(sql.toString(), MapUtils.FINAL_EMPTY_JSON, mappedClass);
    }

    // Page

    /**
     * <b>单表分页查询</b><br><br>
     * <p>阿里最优SQL示例：</p>
     * <code>SELECT a.* FROM 表1 a, (select id from 表1 where 条件 LIMIT 100000,20 ) b where a.id=b.id</code><br><br>
     *
     * @param tableName 表名
     * @param pageIPO   分页查询参数 {@linkplain PageIPO}，所有的条件参数，都将以等于的形式进行SQL拼接
     * @return count（总数），data（分页列表数据）
     */
    public PageVO<JSONObject> page(String tableName, PageIPO pageIPO) {
        return page(tableName, pageIPO, (Class<JSONObject>) null);
    }

    /**
     * <b>单表分页查询</b><br><br>
     * <p>阿里最优SQL示例：</p>
     * <code>SELECT a.* FROM 表1 a, (select id from 表1 where 条件 LIMIT 100000,20 ) b where a.id=b.id</code><br><br>
     *
     * @param <T>         泛型
     * @param tableName   表名
     * @param pageIPO     分页查询参数 {@linkplain PageIPO}，所有的条件参数，都将以等于的形式进行SQL拼接
     * @param mappedClass 映射类
     * @return count（总数），data（分页列表数据）
     */
    public <T> PageVO<T> page(String tableName, PageIPO pageIPO, Class<T> mappedClass) {
        paramFormat(pageIPO.getConditions());
        return dialect.page(tableName, pageIPO, null, mappedClass);
    }

    /**
     * <b>单表分页查询</b><br><br>
     * <p>阿里最优SQL示例：</p>
     * <code>SELECT a.* FROM 表1 a, (select id from 表1 where 条件 ORDER BY id LIMIT 100000,20 ) b where a.id=b.id</code><br><br>
     *
     * @param tableName 表名
     * @param pageIPO   分页查询参数 {@linkplain PageIPO}，所有的条件参数，都将以等于的形式进行SQL拼接
     * @param sortEnum  排序方式 {@linkplain SortEnum}
     * @return count（总数），data（分页列表数据）
     */
    public PageVO<JSONObject> page(String tableName, PageIPO pageIPO, SortEnum sortEnum) {
        return page(tableName, pageIPO, sortEnum, null);
    }

    /**
     * <b>单表分页查询</b><br><br>
     * <p>阿里最优SQL示例：</p>
     * <code>SELECT a.* FROM 表1 a, (select id from 表1 where 条件 ORDER BY id LIMIT 100000,20 ) b where a.id=b.id</code><br><br>
     *
     * @param <T>         泛型
     * @param tableName   表名
     * @param pageIPO     分页查询参数 {@linkplain PageIPO}，所有的条件参数，都将以等于的形式进行SQL拼接
     * @param sortEnum    排序方式 {@linkplain SortEnum}
     * @param mappedClass 映射类
     * @return count（总数），data（分页列表数据）
     */
    public <T> PageVO<T> page(String tableName, PageIPO pageIPO, SortEnum sortEnum, Class<T> mappedClass) {
        paramFormat(pageIPO.getConditions());
        return dialect.page(tableName, pageIPO, sortEnum, mappedClass);
    }

    /**
     * <b>单表分页查询</b><br><br>
     * <p>阿里最优SQL示例：</p>
     * <code>SELECT a.* FROM 表1 a, (select id from 表1 where 条件 LIMIT 100000,20 ) b where a.id=b.id</code><br><br>
     *
     * @param tableName 表名
     * @param whereSql  自定义WHERE语句，若此参数为空，那么所有的条件参数，都将以等于的形式进行SQL拼接。<br><i>SQL示例：</i>
     *                  <code> WHERE 条件</code>
     * @param pageIPO   分页查询参数 {@linkplain PageIPO}
     * @return count（总数），data（分页列表数据）
     */
    public PageVO<JSONObject> pageWhere(String tableName, String whereSql, PageIPO pageIPO) {
        return pageWhere(tableName, whereSql, pageIPO, null);
    }

    /**
     * <b>单表分页查询</b><br><br>
     * <p>阿里最优SQL示例：</p>
     * <code>SELECT a.* FROM 表1 a, (select id from 表1 where 条件 LIMIT 100000,20 ) b where a.id=b.id</code><br><br>
     *
     * @param <T>         泛型
     * @param tableName   表名
     * @param whereSql    自定义WHERE语句，若此参数为空，那么所有的条件参数，都将以等于的形式进行SQL拼接。<br><i>SQL示例：</i>
     *                    <code> WHERE 条件</code>
     * @param pageIPO     分页查询参数 {@linkplain PageIPO}
     * @param mappedClass 映射类
     * @return count（总数），data（分页列表数据）
     */
    public <T> PageVO<T> pageWhere(String tableName, String whereSql, PageIPO pageIPO, Class<T> mappedClass) {
        return dialect.pageWhere(tableName, whereSql, pageIPO, mappedClass);
    }

    /**
     * <b>复杂SQL分页查询</b><br><br>
     * <p>阿里最优查询SQL示例：</p>
     * <code>SELECT a.* FROM 表1 a, (select id from 表1 where 条件 LIMIT :page, :limit) b where a.id=b.id</code><br><br>
     *
     * @param querySql 用于查询数据的sql语句
     * @param pageIPO  分页查询参数 {@linkplain PageIPO}
     * @return count（总数），data（分页列表数据）
     */
    public PageVO<JSONObject> pageSql(String querySql, PageIPO pageIPO) {
        return pageSql(querySql, pageIPO, null);
    }

    /**
     * <b>复杂SQL分页查询</b><br><br>
     * <p>阿里最优查询SQL示例：</p>
     * <code>SELECT a.* FROM 表1 a, (select id from 表1 where 条件 LIMIT :page, :limit) b where a.id=b.id</code><br><br>
     *
     * @param <T>         泛型
     * @param querySql    用于查询数据的sql语句
     * @param pageIPO     分页查询参数 {@linkplain PageIPO}
     * @param mappedClass 映射类
     * @return count（总数），data（分页列表数据）
     */
    public <T> PageVO<T> pageSql(String querySql, PageIPO pageIPO, Class<T> mappedClass) {
        return dialect.pageSql(querySql, pageIPO, mappedClass);
    }

    /**
     * <b>复杂SQL分页查询</b><br><br>
     * <p>统计SQL示例：</p>
     * <code>SELECT count(*) count FROM 表1 a, (select id from 表1 where 条件) b where a.id=b.id</code><br>
     * <p>阿里最优查询SQL示例：</p>
     * <code>SELECT a.* FROM 表1 a, (select id from 表1 where 条件 LIMIT :page, :limit) b where a.id=b.id</code><br><br>
     *
     * @param countSql 用于统计总数的sql语句 <i>（注意：count(*)必须拥有count别名）</i> 同时countSql可以为null表示不统计 <b>可选参数</b>
     * @param querySql 用于查询数据的sql语句
     * @param pageIPO  分页查询参数 {@linkplain PageIPO}
     * @return count（总数），data（分页列表数据）
     */
    public PageVO<JSONObject> pageSql(@Nullable String countSql, String querySql, PageIPO pageIPO) {
        return pageSql(countSql, querySql, pageIPO, null);
    }

    /**
     * <b>复杂SQL分页查询</b><br><br>
     * <p>统计SQL示例：</p>
     * <code>SELECT count(*) count FROM 表1 a, (select id from 表1 where 条件) b where a.id=b.id</code><br>
     * <p>阿里最优查询SQL示例：</p>
     * <code>SELECT a.* FROM 表1 a, (select id from 表1 where 条件 LIMIT :page, :limit) b where a.id=b.id</code><br><br>
     *
     * @param <T>         泛型
     * @param countSql    用于统计总数的sql语句 <i>（注意：count(*)必须拥有count别名）</i> 同时countSql可以为null表示不统计 <b>可选参数</b>
     * @param querySql    用于查询数据的sql语句
     * @param pageIPO     分页查询参数 {@linkplain PageIPO}
     * @param mappedClass 映射类
     * @return count（总数），data（分页列表数据）
     */
    public <T> PageVO<T> pageSql(@Nullable String countSql, String querySql, PageIPO pageIPO, Class<T> mappedClass) {
        return dialect.pageSql(countSql, querySql, pageIPO, mappedClass);
    }

    /**
     * <b>根据相同的列表条件，获得上一条与下一条数据</b>
     *
     * @param querySql 			用于查询数据的sql语句
     * @param pageIPO 			分页查询参数 {@linkplain PageIPO}
     * @param equalsId			做比较的条件ID（将与查询结果的主键ID做比较）
     * @return {@linkplain PageBeforeAndAfterVO}
     */
    public PageBeforeAndAfterVO pageBeforeAndAfter(String querySql, PageIPO pageIPO, Long equalsId) {
        // 1. 参数校验
        if (StringUtils.isEmpty(querySql)) {
            throw new DbException("querySql不能为空");
        }

        // 2. 查询数据
        JSONArray array = new JSONArray();
        array.addAll(queryForList(querySql, dialect.toPage(pageIPO).toParamJson()));
        int size = array.size();

        // 3. 获得前后值
        Long beforeId = null;
        Long afterId = null;
        String key = DbConstant.PRIMARY_KEY;
        for (int i = 0; i < size; i++) {
            JSONObject json = array.getJSONObject(i);
            // 比较列表中相等的值，然后获取前一条与后一条数据
            if (equalsId.equals(json.getLong(key))) {
                if (i != 0) {// 不是列表中第一条数据
                    beforeId = array.getJSONObject(i - 1).getLong(key);
                }
                if (i != size - 1) {// 不是列表中最后一条数据
                    afterId = array.getJSONObject(i + 1).getLong(key);
                }
                break;
            }
        }

        // 4. 返回结果
        return PageBeforeAndAfterVO.builder()
                .beforeId(beforeId)
                .afterId(afterId)
                .build();
    }

}
