package ai.yue.library.data.jdbc.client;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 使用JdbcTemplate进行SQL优化型数据库操作，以 '?' 作为SQL语句参数占位符
 * <p>示例：<code>SELECT * FROM tableName WHERE id = ?</code>
 * <p>jdbcQuery开头的查询方法暂时不支持参数自动加密匹配，因为SQL中使用`?`作为占位符，无法解析具体第几个参数是需要加密的</p>
 * 
 * @author	ylyue
 * @since	2020年8月19日
 */
@Slf4j
@Getter
class DbJdbcTemplate extends DbBase {
	
	// jdbcQueryFor

	/**
	 * <b>查询多行数据</b>
	 * <p>同 {@link JdbcTemplate#queryForList(String, Object...)}</p>
	 * <p>执行以 '?' 作为参数占位符的SQL语句进行列表查询
	 *
	 * @param sql  SQL语句中可以包含一个或多个 '?' 参数占位符
	 * @param args 查询参数，有序的对应SQL语句中的 '?' 参数占位符
	 * @return 多行查询结果
	 */
	public List<JSONObject> jdbcQueryForList(String sql, @Nullable Object... args) {
		return jdbcQueryForList(sql, null, args);
	}

	/**
	 * <b>查询多行数据</b>
	 * <p>对 {@link JdbcTemplate#queryForList(String, Class, Object...)} 方法的优化实现</p>
	 * <p>执行以 '?' 作为参数占位符的SQL语句进行列表查询
	 *
	 * @param sql         SQL语句中可以包含一个或多个 '?' 参数占位符
	 * @param mappedClass 查询结果映射类型，支持JavaBean与简单类型（如：Long, String, Boolean）
	 * @param args        查询参数，有序的对应SQL语句中的 '?' 参数占位符
	 * @return 多行查询结果
	 */
	public <T> List<T> jdbcQueryForList(String sql, Class<T> mappedClass, @Nullable Object... args) {
		return getJdbcTemplate().query(sql, getRowMapper(mappedClass), args);
	}

	/**
	 * <b>查询一行数据</b>
	 * <p>对 {@link JdbcTemplate#queryForMap(String, Object...)} 方法的优化实现</p>
	 * <p>执行以 '?' 作为参数占位符的SQL语句进行单行查询
	 *
	 * @param sql SQL语句中可以包含一个或多个 '?' 参数占位符
	 * @param args 查询参数，有序的对应SQL语句中的 '?' 参数占位符
	 * @return 可以是一个正确的单行查询结果、或null、或查询结果是多条数据而引发的预期错误异常
	 */
	public JSONObject jdbcQueryForJson(String sql, @Nullable Object... args) {
		return jdbcQueryForObj(sql, null, args);
	}

	/**
	 * <b>查询一行数据</b>
	 * <p>对 {@link JdbcTemplate#queryForObject(String, Class, Object...)} 方法的优化实现</p>
	 * <p>执行以 '?' 作为参数占位符的SQL语句进行单行查询
	 *
	 * @param sql SQL语句中可以包含一个或多个 '?' 参数占位符
	 * @param mappedClass 查询结果映射类型，支持JavaBean与简单类型（如：Long, String, Boolean）   
	 * @param args 查询参数，有序的对应SQL语句中的 '?' 参数占位符
	 * @return 可以是一个正确的单行查询结果、或null、或查询结果是多条数据而引发的预期错误异常
	 */
	public <T> T jdbcQueryForObj(String sql, Class<T> mappedClass, @Nullable Object... args) {
		List<T> list = jdbcQueryForList(sql, mappedClass, args);
		return listResultToGetResult(list);
	}
	
	// jdbcQuery

	public String jdbcQueryStr(String sql, @Nullable Object... args) {
		return jdbcQueryForObj(sql, String.class, args);
	}
	
	public Integer jdbcQueryInt(String sql, @Nullable Object... args) {
		return jdbcQueryForObj(sql, Integer.class, args);
	}
	
	public Long jdbcQueryLong(String sql, @Nullable Object... args) {
		return jdbcQueryForObj(sql, Long.class, args);
	}
	
	public Double jdbcQueryDouble(String sql, @Nullable Object... args) {
		return jdbcQueryForObj(sql, Double.class, args);
	}
	
	public BigDecimal jdbcQueryBigDecimal(String sql, @Nullable Object... args) {
		return jdbcQueryForObj(sql, BigDecimal.class, args);
	}
	
	public Number jdbcQueryNumber(String sql, @Nullable Object... args) {
		return jdbcQueryForObj(sql, Number.class, args);
	}
	
	public Boolean jdbcQueryBoolean(String sql, @Nullable Object... args) {
		return jdbcQueryForObj(sql, Boolean.class, args);
	}
	
	public Date jdbcQueryDate(String sql, @Nullable Object... args) {
		return jdbcQueryForObj(sql, Date.class, args);
	}
	
	public LocalDate jdbcQueryLocalDate(String sql, @Nullable Object... args) {
		return jdbcQueryForObj(sql, LocalDate.class, args);
	}
	
	public LocalDateTime jdbcQueryLocalDateTime(String sql, @Nullable Object... args) {
		return jdbcQueryForObj(sql, LocalDateTime.class, args);
	}

}
