package ai.yue.library.data.jdbc.support;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.util.MapUtils;

/**
 * <h2>将每一行的结果集转换为指定 POJO 类型</h2>
 * <p>两倍有余 {@linkplain org.springframework.jdbc.core.BeanPropertyRowMapper} 的性能，对比结果见 <a href="https://ylyue.cn">官网文档</a> 说明。
 * <p>支持更多类型映射，如：{@linkplain JSONObject}、{@linkplain JSONArray}
 * <p>支持 {@link JSONField} 注解
 * 
 * @author	ylyue
 * @since	2020年11月2日
 */
public class BeanPropertyRowMapper<T> implements RowMapper<T> {

	/** The class we are mapping to. */
	@Nullable
	private Class<T> mappedClass;
	
	/**
	 * Create a new {@code BeanPropertyRowMapper}, accepting unpopulated
	 * properties in the target bean.
	 * <p>Consider using the {@link #newInstance} factory method instead,
	 * which allows for specifying the mapped type once only.
	 * @param mappedClass the class that each row should be mapped to
	 */
	public BeanPropertyRowMapper(Class<T> mappedClass) {
		this.mappedClass = mappedClass;
	}
	
	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		org.springframework.jdbc.core.ColumnMapRowMapper columnMapRowMapper = new org.springframework.jdbc.core.ColumnMapRowMapper();
		Map<String, Object> resultJson = columnMapRowMapper.mapRow(rs, rowNum);
		return MapUtils.isEmpty(resultJson) ? null : Convert.toJavaBean(resultJson, this.mappedClass);
	}
	
	/**
	 * Static factory method to create a new {@code BeanPropertyRowMapper}
	 * (with the mapped class specified only once).
	 * @param mappedClass the class that each row should be mapped to
	 */
	public static <T> BeanPropertyRowMapper<T> newInstance(Class<T> mappedClass) {
		return new BeanPropertyRowMapper<>(mappedClass);
	}
	
}
