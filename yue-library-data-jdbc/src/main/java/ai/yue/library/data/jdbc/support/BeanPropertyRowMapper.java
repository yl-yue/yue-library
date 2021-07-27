package ai.yue.library.data.jdbc.support;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.util.MapUtils;
import ai.yue.library.data.jdbc.client.DbBase;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

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
	private Class<T> mappedClass;
	private DbBase dbBase;

	/**
	 * Create a new {@code BeanPropertyRowMapper}, accepting unpopulated
	 * properties in the target bean.
	 *
	 * @param mappedClass the class that each row should be mapped to
	 */
	public BeanPropertyRowMapper(Class<T> mappedClass) {
		this.mappedClass = mappedClass;
	}

	public BeanPropertyRowMapper(Class<T> mappedClass, DbBase dbBase) {
		this.mappedClass = mappedClass;
		this.dbBase = dbBase;
	}

	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		ColumnMapRowMapper columnMapRowMapper = new ColumnMapRowMapper();
		JSONObject resultJson = columnMapRowMapper.mapRow(rs, rowNum);
		if (dbBase != null) {
			dbBase.dataDecrypt(mappedClass, resultJson);
		}
		return MapUtils.isEmpty(resultJson) ? null : Convert.toJavaBean(resultJson, this.mappedClass);
	}
	
}
