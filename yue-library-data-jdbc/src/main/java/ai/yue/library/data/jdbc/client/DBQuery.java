package ai.yue.library.data.jdbc.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.exception.DBException;
import ai.yue.library.base.util.ArithCompute;
import ai.yue.library.base.util.ListUtils;
import ai.yue.library.base.util.MapUtils;
import ai.yue.library.base.util.StringUtils;
import ai.yue.library.data.jdbc.constant.DBConstant;
import ai.yue.library.data.jdbc.constant.DBSortEnum;
import ai.yue.library.data.jdbc.dto.PageDTO;
import ai.yue.library.data.jdbc.ipo.PageIPO;
import ai.yue.library.data.jdbc.vo.PageBeforeAndAfterVO;
import ai.yue.library.data.jdbc.vo.PageTVO;
import ai.yue.library.data.jdbc.vo.PageVO;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>SQL优化型数据库操作</h2>
 * Created by sunJinChuan on 2016/6/6
 * @since 0.0.1
 */
@Slf4j
class DBQuery extends DBBase {
	
	// Query
	
	private String querySql(String tableName, JSONObject paramJson, DBSortEnum dBSortEnum) {
		paramValidate(tableName, paramJson);
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ");
		sql.append(tableName);
		String whereSql = paramToWhereSql(paramJson);
		sql.append(whereSql);
		if (dBSortEnum == DBSortEnum.升序) {// 升序
			sql.append(" ORDER BY id");
		} else if (dBSortEnum == DBSortEnum.降序) {// 降序
			sql.append(" ORDER BY id DESC");
		}
		
		return sql.toString();
	}
	
	/**
	 * 绝对条件查询
	 * @param tableName 表名
	 * @param paramJson 查询参数
	 * @return 列表数据
	 */
	public List<JSONObject> query(String tableName, JSONObject paramJson) {
		String sql = querySql(tableName, paramJson, null);
		return ListUtils.toJsonList(namedParameterJdbcTemplate.queryForList(sql, paramJson));
	}
    
	/**
	 * 绝对条件查询
	 * @param <T> 泛型
	 * @param tableName 表名
	 * @param paramJson 查询参数
	 * @param mappedClass 映射类
	 * @return 列表数据
	 */
	public <T> List<T> query(String tableName, JSONObject paramJson, Class<T> mappedClass) {
		String sql = querySql(tableName, paramJson, null);
		return namedParameterJdbcTemplate.query(sql, paramJson, BeanPropertyRowMapper.newInstance(mappedClass));
	}
	
	/**
	 * 绝对条件查询
	 * @param tableName 表名
	 * @param paramJson 查询参数
	 * @param dBSortEnum 排序方式
	 * @return 列表数据
	 */
	public List<JSONObject> query(String tableName, JSONObject paramJson, DBSortEnum dBSortEnum) {
		String sql = querySql(tableName, paramJson, dBSortEnum);
		return ListUtils.toJsonList(namedParameterJdbcTemplate.queryForList(sql, paramJson));
	}
	
	/**
	 * 绝对条件查询
	 * @param <T> 泛型
	 * @param tableName 表名
	 * @param paramJson 查询参数
	 * @param mappedClass 映射类
	 * @param dBSortEnum 排序方式
	 * @return 列表数据
	 */
	public <T> List<T> query(String tableName, JSONObject paramJson, Class<T> mappedClass, DBSortEnum dBSortEnum) {
		String sql = querySql(tableName, paramJson, dBSortEnum);
		return namedParameterJdbcTemplate.query(sql, paramJson, BeanPropertyRowMapper.newInstance(mappedClass));
	}
	
	private String queryByIdSql(String tableName, Long id) {
		paramValidate(tableName, id);
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ");
		sql.append(tableName);
		sql.append(" WHERE id = :id ");
		return sql.toString();
	}
	
	/**
	 * 通过表主键ID查询
	 * @param tableName	表名
	 * @param id		表自增ID
	 * @return JSON数据
	 */
    public JSONObject queryById(String tableName, long id) {
    	String sql = queryByIdSql(tableName, id);
		JSONObject paramJson = new JSONObject();
		paramJson.put("id", id);
		return queryForJson(sql, paramJson);
	}
    
	/**
	 * 通过表ID查询（字段名=id，一般为表自增ID-主键）
	 * @param <T> 泛型
	 * @param tableName 表名
	 * @param id 主键ID
	 * @param mappedClass 映射类
	 * @return POJO对象
	 */
    public <T> T queryById(String tableName, Long id, Class<T> mappedClass) {
    	String sql = queryByIdSql(tableName, id);
		JSONObject paramJson = new JSONObject();
		paramJson.put("id", id);
		return queryForObject(sql, paramJson, mappedClass);
	}
    
    private String queryByIdSql(String tableName, Long id, String[] fieldName) {
		paramValidate(tableName, id, fieldName);
		String field = StringUtils.deleteFirstLastString(Arrays.toString(fieldName), 1);
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append(field);
		sql.append(" FROM ");
		sql.append(tableName);
		sql.append(" WHERE id = :id ");
		return sql.toString();
	}
	
	/**
	 * 通过表主键ID动态查询字段
	 * @param tableName	表名
	 * @param id		表自增ID
	 * @param fieldName	字段名
	 * @return JSON对象
	 */
    public JSONObject queryById(String tableName, Long id, String ... fieldName) {
    	String sql = queryByIdSql(tableName, id, fieldName);
		JSONObject paramJson = new JSONObject();
		paramJson.put("id", id);
		return queryForJson(sql, paramJson);
	}
    
    private String queryAllSql(String tableName) {
		paramValidate(tableName);
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ");
		sql.append(tableName);
		return sql.toString();
    }
    
	/**
	 * 查询表中所有数据
	 * @param tableName 表名
	 * @return 列表数据
	 */
    public List<JSONObject> queryAll(String tableName) {
    	String sql = queryAllSql(tableName);
    	return queryForList(sql, MapUtils.FINAL_EMPTY_JSON);
	}
    
	/**
	 * 查询表中所有数据
	 * @param <T> 泛型
	 * @param tableName 表名
	 * @param mappedClass 映射类
	 * @return 列表数据
	 */
    public <T> List<T> queryAll(String tableName, Class<T> mappedClass) {
    	String sql = queryAllSql(tableName);
		return namedParameterJdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(mappedClass));
	}
	
    /**
     * {@linkplain NamedParameterJdbcTemplate#queryForMap(String, Map)} 的安全查询方式<br><br>
     * 指定SQL语句以创建预编译执行SQL和绑定查询参数，结果映射应该是一个单行查询否则结果为null。
     * @param sql 要执行的SQL查询
     * @param paramJson 要绑定到查询的参数映射
     * @return JSON对象
     */
	public JSONObject queryForJson(String sql, JSONObject paramJson) {
		var list = queryForList(sql, paramJson);
		return resultToJson(list);
	}
    
    /**
     * 同{@linkplain NamedParameterJdbcTemplate#queryForObject(String, Map, Class)}<br><br>
     * 指定SQL语句以创建预编译执行SQL和绑定查询参数，结果映射应该是一个单行查询否则结果为null。
     * @param <T> 泛型
     * @param sql 要执行的SQL查询
     * @param paramJson 要绑定到查询的参数映射
     * @param mappedClass 映射类
     * @return POJO对象
     */
    public <T> T queryForObject(String sql, JSONObject paramJson, Class<T> mappedClass) {
    	try {
    		return namedParameterJdbcTemplate.queryForObject(sql, paramJson, BeanPropertyRowMapper.newInstance(mappedClass));
    	}catch (Exception e) {
    		log.warn(e.getMessage());
    		return null;
		}
	}
    
    /**
     * 同 {@link NamedParameterJdbcTemplate#queryForList(String, Map)}<br><br>
     * 指定SQL语句以创建预编译执行SQL和绑定查询参数，结果映射应该是一个多行查询。
     * @param sql 要执行的查询SQL
     * @param paramJson 要绑定到查询的参数映射
     * @return 列表数据
     */
    public List<JSONObject> queryForList(String sql, JSONObject paramJson) {
    	return ListUtils.toJsonList(namedParameterJdbcTemplate.queryForList(sql, paramJson));
	}
    
    /**
     * 同 {@linkplain NamedParameterJdbcTemplate#queryForList(String, Map, Class)}<br>
     * 指定SQL语句以创建预编译执行SQL和绑定查询参数，结果映射应该是一个多行查询。
     * @param <T> 泛型
     * @param sql 要执行的查询SQL
     * @param paramJson 要绑定到查询的参数映射
     * @param mappedClass 映射类
     * @return 列表数据
     */
    public <T> List<T> queryForList(String sql, JSONObject paramJson, Class<T> mappedClass) {
    	return namedParameterJdbcTemplate.query(sql, paramJson, BeanPropertyRowMapper.newInstance(mappedClass));
	}
	
	// Page
	
	/**
	 * 处理分页参数
	 * @param pageIPO
	 * @return
	 */
    private JSONObject pageIPO(PageIPO pageIPO) {
		// 1. 处理分页参数
		int page = pageIPO.getPage();
		int limit = pageIPO.getLimit();
		JSONObject conditions = pageIPO.getConditions();
		page--;
		if (page >= 1) {
			page = (int) ArithCompute.mul(page, limit);
		}
		
		// 2. 处理查询条件
		JSONObject paramJson = new JSONObject();
		paramJson.put("page", page);
		paramJson.put("limit", limit);
		if (null != conditions && !conditions.isEmpty()) {
			paramJson.putAll(conditions);
		}
		
		// 3. 返回结果
		return paramJson;
	}
	
    private PageDTO pageDTO(String tableName, PageIPO pageIPO, DBSortEnum dBSortEnum) {
		// 1. 参数验证
		paramValidate(tableName);
		
		// 2. 处理分页参数
		JSONObject paramJson = pageIPO(pageIPO);
		JSONObject conditions = pageIPO.getConditions();
		
		// 3. 预编译SQL拼接
		StringBuffer querySql = new StringBuffer();
		querySql.append("SELECT a.* FROM ");
		querySql.append(tableName + " a, ");
		querySql.append("(SELECT id FROM ");
		querySql.append(tableName);
		// 添加查询条件
		String whereSql = "";
		if (conditions != null) {
			whereSql = paramToWhereSql(conditions);
		}
		querySql.append(whereSql);
		// 排序
		if (dBSortEnum == null) {// 默认（不排序）
			querySql.append(" LIMIT :page, :limit) b WHERE a.id = b.id");
		} else {
			if (DBSortEnum.升序 == dBSortEnum) {// 升序
				querySql.append(" ORDER BY id LIMIT :page, :limit) b WHERE a.id = b.id");
			} else {// 降序
				querySql.append(" ORDER BY id DESC LIMIT :page, :limit) b WHERE a.id = b.id");
			}
		}
		
		// 4. 统计总数
		StringBuffer countSql = new StringBuffer();
		countSql.append("SELECT COUNT(*) count FROM ");
		countSql.append(tableName);
		countSql.append(whereSql);
		Long count = (Long) namedParameterJdbcTemplate.queryForMap(countSql.toString(), paramJson).get("count");
		
		// 5. 返回结果
		return PageDTO.builder().count(count).querySql(querySql.toString()).paramJson(paramJson).build();
	}
    
    private PageVO pageVO(PageDTO pageDTO) {
		// 1. 处理PageDTO
		Long count = pageDTO.getCount();
		String querySql = pageDTO.getQuerySql();
		JSONObject paramJson = pageDTO.getParamJson();
		
		// 2. 查询数据
		List<JSONObject> data = new ArrayList<>();
		if (count == null || count != 0) {
			data = ListUtils.toJsonList(namedParameterJdbcTemplate.queryForList(querySql, paramJson));
		}
		
		// 3. 分页
		return PageVO.builder().count(count).data(data).build();
	}
	
    private <T> PageTVO<T> pageTVO(PageDTO pageDTO, Class<T> mappedClass) {
		// 1. 处理PageDTO
		Long count = pageDTO.getCount();
		String querySql = pageDTO.getQuerySql();
		JSONObject paramJson = pageDTO.getParamJson();
		
		// 2. 查询数据
		List<T> data = new ArrayList<>();
		if (count != 0) {
			data = namedParameterJdbcTemplate.query(querySql, paramJson, BeanPropertyRowMapper.newInstance(mappedClass));
		}
		// 3. 分页
		PageTVO<T> pageTVO = new PageTVO<>();
		return pageTVO.toBuilder().count(count).data(data).build();
	}
	
    /**
     * <b>单表分页查询</b><br><br>
     * <p>阿里最优SQL示例：</p>
     * <code>SELECT a.* FROM 表 1 a, (select id from 表 1 where 条件 LIMIT 100000,20 ) b where a.id=b.id</code><br><br>
     * @param tableName 表名
     * @param pageIPO 分页查询参数 {@linkplain PageIPO}，所有的条件参数，都将以等于的形式进行SQL拼接
     * @return count（总数），data（分页列表数据）
     */
	public PageVO page(String tableName, PageIPO pageIPO) {
		PageDTO pageDTO = pageDTO(tableName, pageIPO, null);
		return pageVO(pageDTO);
	}
	
    /**
     * <b>单表分页查询</b><br><br>
     * <p>阿里最优SQL示例：</p>
     * <code>SELECT a.* FROM 表 1 a, (select id from 表 1 where 条件 LIMIT 100000,20 ) b where a.id=b.id</code><br><br>
     * @param <T> 泛型
     * @param tableName 表名
     * @param pageIPO 分页查询参数 {@linkplain PageIPO}，所有的条件参数，都将以等于的形式进行SQL拼接
     * @param mappedClass 映射类
     * @return count（总数），data（分页列表数据）
     */
	public <T> PageTVO<T> page(String tableName, PageIPO pageIPO, Class<T> mappedClass) {
		// 1. 获得PageDTO
		PageDTO pageDTO = pageDTO(tableName, pageIPO, null);
		return pageTVO(pageDTO, mappedClass);
	}
	
    /**
     * <b>单表分页查询</b><br><br>
     * <p>阿里最优SQL示例：</p>
     * <code>SELECT a.* FROM 表 1 a, (select id from 表 1 where 条件 ORDER BY id LIMIT 100000,20 ) b where a.id=b.id</code><br><br>
     * @param tableName 	表名
     * @param pageIPO 		分页查询参数 {@linkplain PageIPO}，所有的条件参数，都将以等于的形式进行SQL拼接
     * @param dBSortEnum 	排序方式 {@linkplain DBSortEnum}
     * @return count（总数），data（分页列表数据）
     */
	public PageVO page(String tableName, PageIPO pageIPO, DBSortEnum dBSortEnum) {
		PageDTO pageDTO = pageDTO(tableName, pageIPO, dBSortEnum);
		return pageVO(pageDTO);
	}
	
    /**
     * <b>单表分页查询</b><br><br>
     * <p>阿里最优SQL示例：</p>
     * <code>SELECT a.* FROM 表 1 a, (select id from 表 1 where 条件 ORDER BY id LIMIT 100000,20 ) b where a.id=b.id</code><br><br>
     * @param <T> 泛型
     * @param tableName 	表名
     * @param pageIPO 		分页查询参数 {@linkplain PageIPO}，所有的条件参数，都将以等于的形式进行SQL拼接
     * @param mappedClass 	映射类
     * @param dBSortEnum 	排序方式 {@linkplain DBSortEnum}
     * @return count（总数），data（分页列表数据）
     */
	public <T> PageTVO<T> page(String tableName, PageIPO pageIPO, Class<T> mappedClass, DBSortEnum dBSortEnum) {
		// 1. 获得PageDTO
		PageDTO pageDTO = pageDTO(tableName, pageIPO, dBSortEnum);
		return pageTVO(pageDTO, mappedClass);
	}
	
	private PageDTO pageWhereDTO(String tableName, String whereSql, PageIPO pageIPO) {
		// 1. 参数验证
		paramValidate(tableName, whereSql);
		
		// 2. 处理分页参数
		JSONObject paramJson = pageIPO(pageIPO);
		
		// 3. 预编译SQL拼接
		StringBuffer querySql = new StringBuffer();
		querySql.append("SELECT a.* FROM ");
		querySql.append(tableName + " a, ");
		querySql.append(" (select id from ");
		querySql.append(tableName);
		querySql.append(" ");
		querySql.append(whereSql);
		querySql.append(" LIMIT :page, :limit) b WHERE a.id = b.id");
		
		// 4. 统计总数
		StringBuffer countSql = new StringBuffer();
		countSql.append("SELECT COUNT(*) count FROM ");
		countSql.append(tableName);
		countSql.append(" ");
		countSql.append(whereSql);
		Long count = (Long) namedParameterJdbcTemplate.queryForMap(countSql.toString(), paramJson).get("count");
		
		// 5. 返回结果
		return PageDTO.builder().count(count).querySql(querySql.toString()).paramJson(paramJson).build();
	}
	
	/**
     * <b>单表分页查询</b><br><br>
     * <p>阿里最优SQL示例：</p>
     * <code>SELECT a.* FROM 表 1 a, (select id from 表 1 where 条件 LIMIT 100000,20 ) b where a.id=b.id</code><br><br>
     * @param tableName 表名
     * @param whereSql 自定义WHERE语句，若此参数为空，那么所有的条件参数，都将以等于的形式进行SQL拼接。<br><i>SQL示例：</i>
     * <code> WHERE 条件</code>
     * @param pageIPO 分页查询参数 {@linkplain PageIPO}
     * @return count（总数），data（分页列表数据）
     */
	public PageVO pageWhere(String tableName, String whereSql, PageIPO pageIPO) {
		PageDTO pageDTO = pageWhereDTO(tableName, whereSql, pageIPO);
		return pageVO(pageDTO);
	}
	
    /**
     * <b>单表分页查询</b><br><br>
     * <p>阿里最优SQL示例：</p>
     * <code>SELECT a.* FROM 表 1 a, (select id from 表 1 where 条件 LIMIT 100000,20 ) b where a.id=b.id</code><br><br>
     * @param <T> 泛型
     * @param tableName 表名
     * @param whereSql 自定义WHERE语句，若此参数为空，那么所有的条件参数，都将以等于的形式进行SQL拼接。<br><i>SQL示例：</i>
     * <code> WHERE 条件</code>
     * @param pageIPO 分页查询参数 {@linkplain PageIPO}
     * @param mappedClass 映射类
     * @return count（总数），data（分页列表数据）
     */
	public <T> PageTVO<T> pageWhere(String tableName, String whereSql, PageIPO pageIPO, Class<T> mappedClass) {
		PageDTO pageDTO = pageWhereDTO(tableName, whereSql, pageIPO);
		return pageTVO(pageDTO, mappedClass);
	}
	
	private PageDTO pageSqlDTO(String querySql, PageIPO pageIPO) {
		// 1. 参数校验
		if (StringUtils.isEmpty(querySql)) {
			throw new DBException("querySql不能为空");
		}
		
		// 2. 处理分页参数
		JSONObject paramJson = pageIPO(pageIPO);
		JSONObject conditions = pageIPO.getConditions();
		
		// 3. 统计
		int fromIndex = querySql.toUpperCase().indexOf("FROM");
		String countStr = DBConstant.PAGE_COUNT_SQL_PREFIX + querySql.substring(fromIndex);
		int limitIndex = countStr.toUpperCase().indexOf("LIMIT");
		if (-1 == limitIndex) {
			throw new DBException("querySql不能没有LIMIT");
		}
		int EndIndex = countStr.indexOf(")", limitIndex);
		if (-1 == EndIndex) {
			System.err.println("错误的querySql：\n");
			System.err.println(querySql);
			throw new DBException("querySql应当是一个优化后的语句，其中LIMIT必须放在子查询内，详细请参照示例语句编写。");
		}
		StringBuffer countSql = new StringBuffer(countStr);
		countSql = countSql.delete(limitIndex, EndIndex);
		Long count = (Long) namedParameterJdbcTemplate.queryForMap(countSql.toString(), conditions).get("count");
		
		// 4. 返回结果
		return PageDTO.builder().count(count).querySql(querySql.toString()).paramJson(paramJson).build();
	}
	
	private PageDTO pageSqlDTO(String countSql, String querySql, PageIPO pageIPO) {
		// 1. 参数校验
		if (StringUtils.isEmpty(querySql)) {
			throw new DBException("querySql不能为空");
		}
		
		// 2. 处理分页参数
		JSONObject paramJson = pageIPO(pageIPO);
		JSONObject conditions = pageIPO.getConditions();
		
		// 3. 统计
		Long count = null;
		if (!StringUtils.isEmpty(countSql)) {
			count = (Long) namedParameterJdbcTemplate.queryForMap(countSql, conditions).get("count");
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
     * @param pageIPO 分页查询参数 {@linkplain PageIPO}
     * @return count（总数），data（分页列表数据）
     */
	public PageVO pageSql(String querySql, PageIPO pageIPO) {
		PageDTO pageDTO = pageSqlDTO(querySql, pageIPO);
		return pageVO(pageDTO);
	}
	
    /**
     * <b>复杂SQL分页查询</b><br><br>
     * <p>阿里最优查询SQL示例：</p>
     * <code>SELECT a.* FROM 表 1 a, (select id from 表 1 where 条件 LIMIT :page, :limit) b where a.id=b.id</code><br><br>
     * @param <T> 泛型
     * @param querySql 用于查询数据的sql语句
     * @param pageIPO 分页查询参数 {@linkplain PageIPO}
     * @param mappedClass 映射类
     * @return count（总数），data（分页列表数据）
     */
	public <T> PageTVO<T> pageSql(String querySql, PageIPO pageIPO, Class<T> mappedClass) {
		PageDTO pageDTO = pageSqlDTO(querySql, pageIPO);
		return pageTVO(pageDTO, mappedClass);
	}
	
    /**
     * <b>复杂SQL分页查询</b><br><br>
     * <p>统计SQL示例：</p>
     * <code>SELECT count(*) count FROM 表 1 a, (select id from 表 1 where 条件) b where a.id=b.id</code><br>
     * <p>阿里最优查询SQL示例：</p>
     * <code>SELECT a.* FROM 表 1 a, (select id from 表 1 where 条件 LIMIT :page, :limit) b where a.id=b.id</code><br><br>
     * 
     * @param countSql 用于统计总数的sql语句<i>（注意：count(*)必须拥有count别名）</i>
     * <i>同时countSql可以为null表示不统计</i>
     * @param querySql 用于查询数据的sql语句
     * @param pageIPO 分页查询参数 {@linkplain PageIPO}
     * @return count（总数），data（分页列表数据）
     */
	public PageVO pageSql(String countSql, String querySql, PageIPO pageIPO) {
		PageDTO pageDTO = pageSqlDTO(countSql, querySql, pageIPO);
		return pageVO(pageDTO);
	}
	
    /**
     * <b>复杂SQL分页查询</b><br><br>
     * <p>统计SQL示例：</p>
     * <code>SELECT count(*) count FROM 表 1 a, (select id from 表 1 where 条件) b where a.id=b.id</code><br>
     * <p>阿里最优查询SQL示例：</p>
     * <code>SELECT a.* FROM 表 1 a, (select id from 表 1 where 条件 LIMIT :page, :limit) b where a.id=b.id</code><br><br>
     * @param <T> 泛型
     * @param countSql 用于统计总数的sql语句<i>（注意：count(*)必须拥有count别名）</i>
     * <i>同时countSql可以为null表示不统计</i>
     * @param querySql 用于查询数据的sql语句
     * @param pageIPO 分页查询参数 {@linkplain PageIPO}
     * @param mappedClass 映射类
     * @return count（总数），data（分页列表数据）
     */
	public <T> PageTVO<T> pageSql(String countSql, String querySql, PageIPO pageIPO, Class<T> mappedClass) {
		PageDTO pageDTO = pageSqlDTO(countSql, querySql, pageIPO);
		return pageTVO(pageDTO, mappedClass);
	}
	
    /**
     * <b>根据相同的列表条件，获得上一条与下一条数据</b><br><br>
     * @param querySql 			用于查询数据的sql语句
     * @param pageIPO 			分页查询参数 {@linkplain PageIPO}
     * @param equalsId			做比较的条件ID（将与查询结果的主键ID做比较）
     * @return {@linkplain PageBeforeAndAfterVO}
     */
	public PageBeforeAndAfterVO pageBeforeAndAfter(String querySql, PageIPO pageIPO, Long equalsId) {
		// 1. 参数校验
		if (StringUtils.isEmpty(querySql)) {
			throw new DBException("querySql不能为空");
		}
		
		// 2. 处理分页参数
		int page = pageIPO.getPage();
		int limit = pageIPO.getLimit();
		JSONObject conditions = pageIPO.getConditions();
		page--;
		if (page >= 1) {
			page = (int) ArithCompute.mul(page, limit);
		}
		
		if (page > 0) {
			page -= 1;
		}
		limit += 1;
		
		conditions.put("page", page);
		conditions.put("limit", limit);
		
		// 3. 查询数据
		JSONArray array = new JSONArray();
		array.addAll(namedParameterJdbcTemplate.queryForList(querySql, conditions));
		int size = array.size();
		
		// 4. 获得前后值
		Long before_id = null;
		Long after_id = null;
		String key = "id";
		for (int i = 0; i < size; i++) {
			JSONObject json = array.getJSONObject(i);
			// 比较列表中相等的值，然后获取前一条与后一条数据
			if (equalsId.equals(json.getLong(key))) {
				if (i != 0) {// 不是列表中第一条数据
					before_id = array.getJSONObject(i - 1).getLong(key);
				}
				if (i != size - 1) {// 不是列表中最后一条数据
					after_id = array.getJSONObject(i + 1).getLong(key);
				}
				break;
			}
		}
		
		return PageBeforeAndAfterVO.builder()
		.before_id(before_id)
		.after_id(after_id)
		.build();
	}
    
}
