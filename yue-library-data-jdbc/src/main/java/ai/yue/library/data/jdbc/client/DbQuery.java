package ai.yue.library.data.jdbc.client;

import ai.yue.library.base.constant.SortEnum;
import ai.yue.library.base.exception.DbException;
import ai.yue.library.base.util.ClassUtils;
import ai.yue.library.base.util.ListUtils;
import ai.yue.library.base.util.MapUtils;
import ai.yue.library.base.util.StringUtils;
import ai.yue.library.data.jdbc.constant.DbConstant;
import ai.yue.library.data.jdbc.constant.DbSortEnum;
import ai.yue.library.data.jdbc.dto.PageDTO;
import ai.yue.library.data.jdbc.ipo.Page;
import ai.yue.library.data.jdbc.ipo.PageIPO;
import ai.yue.library.data.jdbc.support.BeanPropertyRowMapper;
import ai.yue.library.data.jdbc.support.ColumnMapRowMapper;
import ai.yue.library.data.jdbc.vo.PageBeforeAndAfterVO;
import ai.yue.library.data.jdbc.vo.PageTVO;
import ai.yue.library.data.jdbc.vo.PageVO;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <h2>SQL优化型数据库操作</h2>
 * Created by sunJinChuan on 2016/6/6
 *
 * @since 0.0.1
 */
@SuppressWarnings("deprecation")
@Slf4j
class DbQuery extends DbJdbcTemplate {

    // query

    /**
     * 同 {@linkplain NamedParameterJdbcTemplate#queryForObject(String, Map, Class)}
     * <p>指定SQL语句以创建预编译执行SQL和绑定查询参数，结果映射应该是一个单行查询否则结果为null。
     *
     * @param <T>         普通类型泛型
     * @param sql         要执行的SQL查询
     * @param paramJson   要绑定到查询的参数映射
     * @param mappedClass 结果对象期望匹配的普通类型
     * @return 所需普通类型的结果对象（如：Long, String, Boolean）或null
     */
    public <T> T queryObject(String sql, JSONObject paramJson, Class<T> mappedClass) {
        try {
            return getNamedParameterJdbcTemplate().queryForObject(sql, paramJson, mappedClass);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    // queryFor

    /**
     * {@linkplain NamedParameterJdbcTemplate#queryForMap(String, Map)} 的安全查询方式<br><br>
     * 指定SQL语句以创建预编译执行SQL和绑定查询参数，结果映射应该是一个单行查询否则结果为null。
     *
     * @param sql       要执行的SQL查询
     * @param paramJson 要绑定到查询的参数映射
     * @return JSON对象
     */
    public JSONObject queryForJson(String sql, JSONObject paramJson) {
        var list = queryForList(sql, paramJson);
        return resultToJson(list);
    }

    /**
     * <h3>同 {@linkplain NamedParameterJdbcTemplate#queryForObject(String, Map, org.springframework.jdbc.core.RowMapper)}</h3>
     * <b>注：{@linkplain RowMapper} 的实现为 {@linkplain BeanPropertyRowMapper}</b>
     * <p>指定SQL语句以创建预编译执行SQL和绑定查询参数，结果映射应该是一个单行查询否则结果为null。
     *
     * @param <T>         JavaBean的泛型
     * @param sql         要执行的SQL查询
     * @param paramJson   要绑定到查询的参数映射
     * @param mappedClass POJO映射类
     * @return POJO对象
     */
    public <T> T queryForObject(String sql, JSONObject paramJson, Class<T> mappedClass) {
        List<T> list = queryForList(sql, paramJson, mappedClass);
        return resultToObject(list);
    }

    /**
     * 同 {@linkplain NamedParameterJdbcTemplate#queryForRowSet(String, Map)}
     * <p>指定SQL语句以创建预编译执行SQL和绑定查询参数，结果集可用于方便的获取各种类型的数据。
     *
     * @param sql       要执行的SQL查询
     * @param paramJson 要绑定到查询的参数映射
     * @return 结果集可用于方便的获取各种类型的数据
     */
    public SqlRowSet queryForRowSet(String sql, JSONObject paramJson) {
        return getNamedParameterJdbcTemplate().queryForRowSet(sql, paramJson);
    }

    /**
     * 同 {@link NamedParameterJdbcTemplate#queryForList(String, Map)}<br><br>
     * 指定SQL语句以创建预编译执行SQL和绑定查询参数，结果映射应该是一个多行查询。
     *
     * @param sql       要执行的查询SQL
     * @param paramJson 要绑定到查询的参数映射
     * @return 列表数据
     */
    public List<JSONObject> queryForList(String sql, JSONObject paramJson) {
        return getNamedParameterJdbcTemplate().query(sql, paramJson, new ColumnMapRowMapper());
    }

    /**
     * 同 {@linkplain NamedParameterJdbcTemplate#queryForList(String, Map, Class)}<br>
     * 指定SQL语句以创建预编译执行SQL和绑定查询参数，结果映射应该是一个多行查询。
     *
     * @param <T>         泛型
     * @param sql         要执行的查询SQL
     * @param paramJson   要绑定到查询的参数映射
     * @param mappedClass 映射类
     * @return 列表数据
     */
    public <T> List<T> queryForList(String sql, JSONObject paramJson, Class<T> mappedClass) {
        RowMapper<T> rowMapper = ClassUtils.isSimpleValueType(mappedClass) ?
                SingleColumnRowMapper.newInstance(mappedClass) : BeanPropertyRowMapper.newInstance(mappedClass);
        return getNamedParameterJdbcTemplate().query(sql, paramJson, rowMapper);
    }

    // is

    /**
     * 是否有数据
     *
     * @param tableName 表名
     * @param paramJson 查询参数
     * @return 是否有数据
     */
    public boolean isDataSize(String tableName, JSONObject paramJson) {
        return MapUtils.isNotEmpty(get(tableName, paramJson));
    }

    // get

    /**
     * 获得表的元数据
     * <p>检索元数据，即此行集合的列的数字、类型和属性。
     *
     * @param tableName
     * @return
     */
    public SqlRowSetMetaData getMetaData(String tableName) {
        tableName = dialect.getWrapper().wrap(tableName);
        StringBuffer sql = new StringBuffer("SELECT * FROM ").append(tableName).append(dialect.getPageJoinSql());
        return queryForRowSet(sql.toString(), Page.builder().page(0L).limit(0).build().toParamJson()).getMetaData();
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
     * 单个-ById
     * <p>字段名=id，一般为表自增ID-主键</p>
     * <p><code style="color:red">依赖于接口传入 {@value DbConstant#PRIMARY_KEY} 参数时慎用此方法</code>，避免有序主键被遍历风险，造成数据越权行为。推荐使用 {@link #getByBusinessUk(String, String)}</p>
     *
     * @param tableName 表名
     * @param id        主键ID
     * @return JSON数据
     */
    public JSONObject getById(String tableName, long id) {
        paramValidate(tableName, id);
        String sql = getByColumnNameSqlBuild(tableName, DbConstant.PRIMARY_KEY);
        JSONObject paramJson = new JSONObject();
        paramJson.put(dialect.getWrapper().wrap(DbConstant.PRIMARY_KEY), id);
        return queryForJson(sql, paramJson);
    }

    /**
     * 单个-ById
     * <p>字段名=id，一般为表自增ID-主键</p>
     * <p><code style="color:red">依赖于接口传入 {@value DbConstant#PRIMARY_KEY} 参数时慎用此方法</code>，避免有序主键被遍历风险，造成数据越权行为。推荐使用 {@link #getByBusinessUk(String, String, Class)}</p>
     *
     * @param <T>         泛型
     * @param tableName   表名
     * @param id          主键ID
     * @param mappedClass 映射类
     * @return POJO对象
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
     * @return JSON数据
     */
    public JSONObject getByBusinessUk(String tableName, String businessUkValue) {
        String sql = getByColumnNameSqlBuild(tableName, getJdbcProperties().getBusinessUk());
        JSONObject paramJson = new JSONObject();
        paramJson.put(dialect.getWrapper().wrap(getJdbcProperties().getBusinessUk()), businessUkValue);
        return queryForJson(sql, paramJson);
    }

    /**
     * 单个-By业务键
     * <p>默认业务键为key
     * <p>业务键值推荐使用UUID5
     *
     * @param <T>             泛型
     * @param tableName       表名
     * @param businessUkValue 业务键的唯一值
     * @param mappedClass     映射类
     * @return POJO对象
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
     * @return JSON数据
     */
    public JSONObject get(String tableName, JSONObject paramJson) {
        paramFormat(paramJson);
        String sql = listSqlBuild(tableName, paramJson, null);
        return queryForJson(sql, paramJson);
    }

    /**
     * 单个-绝对条件查询
     *
     * @param <T>         泛型
     * @param tableName   表名
     * @param paramJson   查询参数
     * @param mappedClass 映射类
     * @return POJO对象
     */
    public <T> T get(String tableName, JSONObject paramJson, Class<T> mappedClass) {
        paramFormat(paramJson);
        String sql = listSqlBuild(tableName, paramJson, null);
        return queryForObject(sql, paramJson, mappedClass);
    }

    // list

    private String listSqlBuild(String tableName, JSONObject paramJson, SortEnum sortEnum) {
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

    /**
     * 列表-绝对条件查询
     *
     * @param tableName 表名
     * @param paramJson 查询参数
     * @return 列表数据
     */
    public List<JSONObject> list(String tableName, JSONObject paramJson) {
        paramFormat(paramJson);
        String sql = listSqlBuild(tableName, paramJson, null);
        return ListUtils.toJsonList(getNamedParameterJdbcTemplate().queryForList(sql, paramJson));
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
        String sql = listSqlBuild(tableName, paramJson, null);
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
        paramFormat(paramJson);
        String sql = listSqlBuild(tableName, paramJson, sortEnum);
        return queryForList(sql, paramJson);
    }

    /**
     * @deprecated 请使用：{@linkplain SortEnum}
     */
    @Deprecated
    public List<JSONObject> list(String tableName, JSONObject paramJson, DbSortEnum dBSortEnum) {
        SortEnum sortEnum = null;
        if (dBSortEnum != null) {
            sortEnum = SortEnum.valueOf(dBSortEnum.name());
        }

        return list(tableName, paramJson, sortEnum);
    }

    /**
     * 列表-绝对条件查询
     *
     * @param <T>         泛型
     * @param tableName   表名
     * @param paramJson   查询参数
     * @param mappedClass 映射类
     * @param sortEnum    排序方式
     * @return 列表数据
     */
    public <T> List<T> list(String tableName, JSONObject paramJson, Class<T> mappedClass, SortEnum sortEnum) {
        paramFormat(paramJson);
        String sql = listSqlBuild(tableName, paramJson, sortEnum);
        return queryForList(sql, paramJson, mappedClass);
    }

    /**
     * @deprecated 请使用：{@linkplain SortEnum}
     */
    @Deprecated
    public <T> List<T> list(String tableName, JSONObject paramJson, Class<T> mappedClass, DbSortEnum dBSortEnum) {
        SortEnum sortEnum = null;
        if (dBSortEnum != null) {
            sortEnum = SortEnum.valueOf(dBSortEnum.name());
        }

        return list(tableName, paramJson, mappedClass, sortEnum);
    }

    private String listAllSqlBuild(String tableName) {
        paramValidate(tableName);
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ");
        sql.append(dialect.getWrapper().wrap(tableName));
        if (getJdbcProperties().isEnableDeleteQueryFilter()) {
            sql.append(" WHERE ").append(DbConstant.FIELD_DEFINITION_DELETE_TIME)
                    .append(" = ").append(DbConstant.FIELD_DEFAULT_VALUE_DELETE_TIME);
        }

        return sql.toString();
    }

    /**
     * 列表-查询表中所有数据
     *
     * @param tableName 表名
     * @return 列表数据
     */
    public List<JSONObject> listAll(String tableName) {
        String sql = listAllSqlBuild(tableName);
        return queryForList(sql, MapUtils.FINAL_EMPTY_JSON);
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
        String sql = listAllSqlBuild(tableName);
        return queryForList(sql, MapUtils.FINAL_EMPTY_JSON, mappedClass);
    }

    // Page

    protected PageVO toPageVO(PageDTO pageDTO) {
        // 1. 处理PageDTO
        Long count = pageDTO.getCount();
        String querySql = pageDTO.getQuerySql();
        JSONObject paramJson = pageDTO.getParamJson();

        // 2. 查询数据
        List<JSONObject> data = new ArrayList<>();
        if (count == null || count != 0) {
            data = queryForList(querySql, paramJson);
        }

        // 3. 分页
        return PageVO.builder().count(count).data(data).build();
    }

    protected <T> PageTVO<T> toPageTVO(PageDTO pageDTO, Class<T> mappedClass) {
        // 1. 处理PageDTO
        Long count = pageDTO.getCount();
        String querySql = pageDTO.getQuerySql();
        JSONObject paramJson = pageDTO.getParamJson();

        // 2. 查询数据
        List<T> data = new ArrayList<>();
        if (count != 0) {
            data = queryForList(querySql, paramJson, mappedClass);
        }

        // 3. 分页
        PageTVO<T> pageTVO = new PageTVO<>();
        return pageTVO.toBuilder().count(count).data(data).build();
    }

    /**
     * <b>单表分页查询</b><br><br>
     * <p>阿里最优SQL示例：</p>
     * <code>SELECT a.* FROM 表 1 a, (select id from 表 1 where 条件 LIMIT 100000,20 ) b where a.id=b.id</code><br><br>
     *
     * @param tableName 表名
     * @param pageIPO   分页查询参数 {@linkplain PageIPO}，所有的条件参数，都将以等于的形式进行SQL拼接
     * @return count（总数），data（分页列表数据）
     */
    public PageVO page(String tableName, PageIPO pageIPO) {
        paramFormat(pageIPO.getConditions());
        PageDTO pageDTO = dialect.pageDTOBuild(tableName, pageIPO, null);
        return toPageVO(pageDTO);
    }

    /**
     * <b>单表分页查询</b><br><br>
     * <p>阿里最优SQL示例：</p>
     * <code>SELECT a.* FROM 表 1 a, (select id from 表 1 where 条件 LIMIT 100000,20 ) b where a.id=b.id</code><br><br>
     *
     * @param <T>         泛型
     * @param tableName   表名
     * @param pageIPO     分页查询参数 {@linkplain PageIPO}，所有的条件参数，都将以等于的形式进行SQL拼接
     * @param mappedClass 映射类
     * @return count（总数），data（分页列表数据）
     */
    public <T> PageTVO<T> page(String tableName, PageIPO pageIPO, Class<T> mappedClass) {
        paramFormat(pageIPO.getConditions());
        PageDTO pageDTO = dialect.pageDTOBuild(tableName, pageIPO, null);
        return toPageTVO(pageDTO, mappedClass);
    }

    /**
     * <b>单表分页查询</b><br><br>
     * <p>阿里最优SQL示例：</p>
     * <code>SELECT a.* FROM 表 1 a, (select id from 表 1 where 条件 ORDER BY id LIMIT 100000,20 ) b where a.id=b.id</code><br><br>
     *
     * @param tableName 表名
     * @param pageIPO   分页查询参数 {@linkplain PageIPO}，所有的条件参数，都将以等于的形式进行SQL拼接
     * @param sortEnum  排序方式 {@linkplain SortEnum}
     * @return count（总数），data（分页列表数据）
     */
    public PageVO page(String tableName, PageIPO pageIPO, SortEnum sortEnum) {
        paramFormat(pageIPO.getConditions());
        PageDTO pageDTO = dialect.pageDTOBuild(tableName, pageIPO, sortEnum);
        return toPageVO(pageDTO);
    }

    /**
     * @deprecated 请使用：{@linkplain SortEnum}
     */
    public PageVO page(String tableName, PageIPO pageIPO, DbSortEnum dBSortEnum) {
        SortEnum sortEnum = null;
        if (dBSortEnum != null) {
            sortEnum = SortEnum.valueOf(dBSortEnum.name());
        }

        return page(tableName, pageIPO, sortEnum);
    }

    /**
     * <b>单表分页查询</b><br><br>
     * <p>阿里最优SQL示例：</p>
     * <code>SELECT a.* FROM 表 1 a, (select id from 表 1 where 条件 ORDER BY id LIMIT 100000,20 ) b where a.id=b.id</code><br><br>
     *
     * @param <T>         泛型
     * @param tableName   表名
     * @param pageIPO     分页查询参数 {@linkplain PageIPO}，所有的条件参数，都将以等于的形式进行SQL拼接
     * @param mappedClass 映射类
     * @param sortEnum    排序方式 {@linkplain SortEnum}
     * @return count（总数），data（分页列表数据）
     */
    public <T> PageTVO<T> page(String tableName, PageIPO pageIPO, Class<T> mappedClass, SortEnum sortEnum) {
        paramFormat(pageIPO.getConditions());
        PageDTO pageDTO = dialect.pageDTOBuild(tableName, pageIPO, sortEnum);
        return toPageTVO(pageDTO, mappedClass);
    }

    /**
     * @deprecated 请使用：{@linkplain SortEnum}
     */
    public <T> PageTVO<T> page(String tableName, PageIPO pageIPO, Class<T> mappedClass, DbSortEnum dBSortEnum) {
        SortEnum sortEnum = null;
        if (dBSortEnum != null) {
            sortEnum = SortEnum.valueOf(dBSortEnum.name());
        }

        return page(tableName, pageIPO, mappedClass, sortEnum);
    }

    /**
     * <b>单表分页查询</b><br><br>
     * <p>阿里最优SQL示例：</p>
     * <code>SELECT a.* FROM 表 1 a, (select id from 表 1 where 条件 LIMIT 100000,20 ) b where a.id=b.id</code><br><br>
     *
     * @param tableName 表名
     * @param whereSql  自定义WHERE语句，若此参数为空，那么所有的条件参数，都将以等于的形式进行SQL拼接。<br><i>SQL示例：</i>
     *                  <code> WHERE 条件</code>
     * @param pageIPO   分页查询参数 {@linkplain PageIPO}
     * @return count（总数），data（分页列表数据）
     */
    public PageVO pageWhere(String tableName, String whereSql, PageIPO pageIPO) {
        PageDTO pageDTO = dialect.pageDTOBuild(tableName, whereSql, pageIPO);
        return toPageVO(pageDTO);
    }

    /**
     * <b>单表分页查询</b><br><br>
     * <p>阿里最优SQL示例：</p>
     * <code>SELECT a.* FROM 表 1 a, (select id from 表 1 where 条件 LIMIT 100000,20 ) b where a.id=b.id</code><br><br>
     *
     * @param <T>         泛型
     * @param tableName   表名
     * @param whereSql    自定义WHERE语句，若此参数为空，那么所有的条件参数，都将以等于的形式进行SQL拼接。<br><i>SQL示例：</i>
     *                    <code> WHERE 条件</code>
     * @param pageIPO     分页查询参数 {@linkplain PageIPO}
     * @param mappedClass 映射类
     * @return count（总数），data（分页列表数据）
     */
    public <T> PageTVO<T> pageWhere(String tableName, String whereSql, PageIPO pageIPO, Class<T> mappedClass) {
        PageDTO pageDTO = dialect.pageDTOBuild(tableName, whereSql, pageIPO);
        return toPageTVO(pageDTO, mappedClass);
    }

    private PageDTO pageDTOBuild(String countSql, String querySql, PageIPO pageIPO) {
        // 1. 参数校验
        if (StringUtils.isEmpty(querySql)) {
            throw new DbException("querySql不能为空");
        }

        // 2. 处理分页参数
        JSONObject paramJson = dialect.toParamJson(pageIPO);
        JSONObject conditions = pageIPO.getConditions();

        // 3. 统计
        Long count = null;
        if (!StringUtils.isEmpty(countSql)) {
            count = queryForJson(countSql, conditions).getLong("count");
        }

        // 4. 返回结果
        return PageDTO.builder().count(count).querySql(querySql.toString()).paramJson(paramJson).build();
    }

    /**
     * <b>复杂SQL分页查询</b><br><br>
     * <p>阿里最优查询SQL示例：</p>
     * <code>SELECT a.* FROM 表 1 a, (select id from 表 1 where 条件 LIMIT :page, :limit) b where a.id=b.id</code><br><br>
     *
     * @param querySql 用于查询数据的sql语句
     * @param pageIPO  分页查询参数 {@linkplain PageIPO}
     * @return count（总数），data（分页列表数据）
     */
    public PageVO pageSql(String querySql, PageIPO pageIPO) {
        PageDTO pageDTO = dialect.pageDTOBuild(querySql, pageIPO);
        return toPageVO(pageDTO);
    }

    /**
     * <b>复杂SQL分页查询</b><br><br>
     * <p>阿里最优查询SQL示例：</p>
     * <code>SELECT a.* FROM 表 1 a, (select id from 表 1 where 条件 LIMIT :page, :limit) b where a.id=b.id</code><br><br>
     *
     * @param <T>         泛型
     * @param querySql    用于查询数据的sql语句
     * @param pageIPO     分页查询参数 {@linkplain PageIPO}
     * @param mappedClass 映射类
     * @return count（总数），data（分页列表数据）
     */
    public <T> PageTVO<T> pageSql(String querySql, PageIPO pageIPO, Class<T> mappedClass) {
        PageDTO pageDTO = dialect.pageDTOBuild(querySql, pageIPO);
        return toPageTVO(pageDTO, mappedClass);
    }

    /**
     * <b>复杂SQL分页查询</b><br><br>
     * <p>统计SQL示例：</p>
     * <code>SELECT count(*) count FROM 表 1 a, (select id from 表 1 where 条件) b where a.id=b.id</code><br>
     * <p>阿里最优查询SQL示例：</p>
     * <code>SELECT a.* FROM 表 1 a, (select id from 表 1 where 条件 LIMIT :page, :limit) b where a.id=b.id</code><br><br>
     *
     * @param countSql 用于统计总数的sql语句 <i>（注意：count(*)必须拥有count别名）</i> 同时countSql可以为null表示不统计 <b>可选参数</b>
     * @param querySql 用于查询数据的sql语句
     * @param pageIPO  分页查询参数 {@linkplain PageIPO}
     * @return count（总数），data（分页列表数据）
     */
    public PageVO pageSql(@Nullable String countSql, String querySql, PageIPO pageIPO) {
        PageDTO pageDTO = pageDTOBuild(countSql, querySql, pageIPO);
        return toPageVO(pageDTO);
    }

    /**
     * <b>复杂SQL分页查询</b><br><br>
     * <p>统计SQL示例：</p>
     * <code>SELECT count(*) count FROM 表 1 a, (select id from 表 1 where 条件) b where a.id=b.id</code><br>
     * <p>阿里最优查询SQL示例：</p>
     * <code>SELECT a.* FROM 表 1 a, (select id from 表 1 where 条件 LIMIT :page, :limit) b where a.id=b.id</code><br><br>
     *
     * @param <T>         泛型
     * @param countSql    用于统计总数的sql语句 <i>（注意：count(*)必须拥有count别名）</i> 同时countSql可以为null表示不统计 <b>可选参数</b>
     * @param querySql    用于查询数据的sql语句
     * @param pageIPO     分页查询参数 {@linkplain PageIPO}
     * @param mappedClass 映射类
     * @return count（总数），data（分页列表数据）
     */
    public <T> PageTVO<T> pageSql(@Nullable String countSql, String querySql, PageIPO pageIPO, Class<T> mappedClass) {
        PageDTO pageDTO = pageDTOBuild(countSql, querySql, pageIPO);
        return toPageTVO(pageDTO, mappedClass);
    }

    /**
     * <b>根据相同的列表条件，获得上一条与下一条数据</b>
     *
     * @param querySql 用于查询数据的sql语句
     * @param pageIPO  分页查询参数 {@linkplain PageIPO}
     * @param equalsId 做比较的条件ID（将与查询结果的主键ID做比较）
     * @return {@linkplain PageBeforeAndAfterVO}
     */
    public PageBeforeAndAfterVO pageBeforeAndAfter(String querySql, PageIPO pageIPO, Long equalsId) {
        return dialect.pageBeforeAndAfter(querySql, pageIPO, equalsId);
    }

}
