package ai.yue.library.data.jdbc.client;

import ai.yue.library.data.jdbc.support.ColumnMapRowMapper;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
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
 * 
 * @author	ylyue
 * @since	2020年8月19日
 */
@Slf4j
@Getter
class DbJdbcTemplate extends DbBase {
	
	// Jdbc Query
	
	/**
	 * 执行以 '?' 作为参数占位符的SQL语句进行预期对象查询
	 * <p>查询预期为单行/单列查询
	 * <p>返回的结果将直接映射到相应的对象类型
	 * 
	 * @param <T> SQL语句中SELECT参数列的类型
	 * @param sql SQL语句中可以包含一个或多个 '?' 参数占位符
	 * @param requiredType 结果对象期望匹配的类型
	 * @param args 查询参数，有序的对应SQL语句中的 '?' 参数占位符
	 * @return 所需普通类型的结果对象（如：Long, String, Boolean）或null
	 */
	public <T> T jdbcQueryObj(String sql, Class<T> requiredType, @Nullable Object... args) {
    	try {
    		return getJdbcTemplate().queryForObject(sql, requiredType, args);
		} catch (Exception e) {
			log.warn(e.getMessage());
			return null;
		}
	}
	
	public String jdbcQueryStr(String sql, @Nullable Object... args) {
		return jdbcQueryObj(sql, String.class, args);
	}
	
	public Integer jdbcQueryInt(String sql, @Nullable Object... args) {
		return jdbcQueryObj(sql, Integer.class, args);
	}
	
	public Long jdbcQueryLong(String sql, @Nullable Object... args) {
		return jdbcQueryObj(sql, Long.class, args);
	}
	
	public Double jdbcQueryDouble(String sql, @Nullable Object... args) {
		return jdbcQueryObj(sql, Double.class, args);
	}
	
	public BigDecimal jdbcQueryBigDecimal(String sql, @Nullable Object... args) {
		return jdbcQueryObj(sql, BigDecimal.class, args);
	}
	
	public Number jdbcQueryNumber(String sql, @Nullable Object... args) {
		return jdbcQueryObj(sql, Number.class, args);
	}
	
	public Boolean jdbcQueryBoolean(String sql, @Nullable Object... args) {
		return jdbcQueryObj(sql, Boolean.class, args);
	}
	
	public Date jdbcQueryDate(String sql, @Nullable Object... args) {
		return jdbcQueryObj(sql, Date.class, args);
	}
	
	public LocalDate jdbcQueryLocalDate(String sql, @Nullable Object... args) {
		return jdbcQueryObj(sql, LocalDate.class, args);
	}
	
	public LocalDateTime jdbcQueryLocalDateTime(String sql, @Nullable Object... args) {
		return jdbcQueryObj(sql, LocalDateTime.class, args);
	}
	
    /**
     * {@link JdbcTemplate#queryForMap(String, Object...)} 的安全查询方式
     * <p>执行以 '?' 作为参数占位符的SQL语句进行单行查询
	 * <p>查询预期应该是一个单行查询否则结果为null。
	 * 
     * @param sql SQL语句中可以包含一个或多个 '?' 参数占位符
     * @param args 查询参数，有序的对应SQL语句中的 '?' 参数占位符
     * @return Json对象
     */
	public JSONObject jdbcQueryForJson(String sql, @Nullable Object... args) {
		var list = jdbcQueryForList(sql, args);
		return resultToJson(list);
	}
	
    /**
     * 同 {@link JdbcTemplate#queryForList(String, Object...)}
     * <p>执行以 '?' 作为参数占位符的SQL语句进行列表查询
	 * <p>查询预期为一个多行查询
	 * 
     * @param sql SQL语句中可以包含一个或多个 '?' 参数占位符
     * @param args 查询参数，有序的对应SQL语句中的 '?' 参数占位符
     * @return 列表数据
     */
	public List<JSONObject> jdbcQueryForList(String sql, @Nullable Object... args) {
		return getJdbcTemplate().query(sql, args, new ColumnMapRowMapper());
	}
    
}
