package ai.yue.library.data.jdbc.client;

import ai.yue.library.base.exception.DbException;
import ai.yue.library.base.util.ListUtils;
import ai.yue.library.base.util.MapUtils;
import ai.yue.library.base.util.StringUtils;
import ai.yue.library.base.view.ResultPrompt;
import ai.yue.library.data.jdbc.client.dialect.Dialect;
import ai.yue.library.data.jdbc.config.properties.JdbcProperties;
import ai.yue.library.data.jdbc.constant.DbConstant;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <h2>SQL优化型数据库操作</h2>
 * Created by sunJinChuan on 2016/6/6
 * @author ylyue
 * @since 0.0.1
 */
@Slf4j
@Data
public class DbBase {
	
	// 必须初始化变量
	
	protected JdbcTemplate jdbcTemplate;
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	protected Dialect dialect;
	protected JdbcProperties jdbcProperties;

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
		return this.jdbcTemplate.getDataSource();
	}

	/**
	 * 设置数据源
	 *
	 * @param dataSource 数据源
	 */
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate.setDataSource(dataSource);
		namedParameterJdbcTemplate.getJdbcTemplate().setDataSource(dataSource);
	}

    /**
     * 是否有数据
     * 
     * @param dataSize 数据大小
     * @return 是否 <b>&lt;=</b> 0
     */
	public boolean isDataSize(long dataSize) {
		if (dataSize <= 0) {
			return false;
		}
		
		return true;
	}
	
    /**
     * 判断更新所影响的行数是否 <b>等于</b> 预期值
     * <p>若不是预期值，同时 updateRowsNumber &gt; 0 那么将会抛出一个 {@linkplain DbException}
     * 
     * @param updateRowsNumber	更新所影响的行数
     * @param expectedValue		预期值
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
     * @param updateRowsNumber	更新所影响的行数
     * @param expectedValue		预期值
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
     * @param updateRowsNumber	更新所影响的行数
     * @param expectedValue		预期值
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
     * @param updateRowsNumber	更新所影响的行数
     * @param expectedValue		预期值
     */
	public void updateAndExpectedGreaterThanEqual(long updateRowsNumber, int expectedValue) {
        if (!(updateRowsNumber >= expectedValue)) {
        	String msg = ResultPrompt.dataStructure(">= " + expectedValue, updateRowsNumber);
            throw new DbException(msg);
        }
	}
	
	/**
     * 确认批量更新每组参数所影响的行数，是否 <b>全部都等于</b> 同一个预期值
     * <p>
     * 若不是预期值，那么将会抛出一个{@linkplain DbException}
     * @param updateRowsNumberArray	每组参数更新所影响的行数数组
     * @param expectedValue 预期值
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
	
    /**
     * 同 {@linkplain DbQuery#queryForJson(String, JSONObject)} 的安全查询结果获取
     * @param list {@linkplain DbQuery#queryForList(String, JSONObject)} 查询结果
     * @return JSON数据
     */
    public JSONObject resultToJson(List<JSONObject> list) {
    	int size = list.size();
    	int expectedValue = 1;
    	if (size != expectedValue) {
    		if (size > expectedValue) {
    			String msg = ResultPrompt.dataStructure(expectedValue, size);
    			log.warn(msg);
    		}
    		
    		return null;
    	}
    	
    	return list.get(0);
	}
    
    /**
     * 同 {@linkplain DbQuery#queryForObject(String, JSONObject, Class)} 的安全查询结果获取
     * @param <T> 泛型
     * @param list {@linkplain DbQuery#queryForList(String, JSONObject, Class)} 查询结果
     * @return POJO对象
     */
    public <T> T resultToObject(List<T> list) {
    	int size = list.size();
    	int expectedValue = 1;
    	if (size != expectedValue) {
    		if (size > expectedValue) {
    			String msg = ResultPrompt.dataStructure(expectedValue, size);
    			log.warn(msg);
    		}
    		
    		return null;
    	}
    	
    	return list.get(0);
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
     * @param paramJson 参数
     * @param conditions where条件（对应paramJson key）
     * @return whereSql
     */
	protected String paramToWhereSql(JSONObject paramJson, String... conditions) {
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
    	StringBuffer whereSql = new StringBuffer();
    	if (jdbcProperties.isEnableDeleteQueryFilter()) {
    		whereSql.append(" WHERE ").append(DbConstant.FIELD_DEFINITION_DELETE_TIME)
			.append(" = ").append(DbConstant.FIELD_DEFAULT_VALUE_DELETE_TIME);
    	} else {
    		whereSql.append(" WHERE 1 = 1 ");
    	}
    	
		paramJson.keySet().forEach(condition -> {
			paramToWhereSql(whereSql, paramJson, condition);
		});
		
		return whereSql.toString();
    }
    
    // ParamValidate
    
	/**
	 * 参数验证
	 * @param tableName 表名
	 */
    protected void paramValidate(String tableName) {
		if (StringUtils.isEmpty(tableName)) {
			throw new DbException("表名不能为空");
		}
	}
	
	/**
	 * 参数验证
	 * @param tableName 表名
	 * @param whereSql 条件sql
	 */
    protected void paramValidate(String tableName, String whereSql) {
		if (StringUtils.isEmpty(tableName)) {
			throw new DbException("表名不能为空");
		}
		if (StringUtils.isEmpty(tableName)) {
			throw new DbException("whereSql不能为空");
		}
	}
    
	/**
	 * 参数验证
	 * 
	 * @param tableName 表名
	 * @param id 主键ID
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
	 * @param tableName 表名
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
	 * @param tableName 表名
	 * @param id 主键ID
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
	 * @param tableName 表名
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
	 * @param tableName 表名
	 * @param paramJson 参数
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
	 * 参数美化（对SpringJDBC不支持的类型进行转换）
	 * <p>JSONObject 转 JsonString</p>
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
			Object value = entry.getValue();
			if (value == null) {
				return;
			}

			// 2. 参数美化
			String key = entry.getKey();
			Class<?> valueClass = value.getClass();
			String formatKey = key;
			Object formatValue = value;
			if (valueClass == JSONObject.class) {
				formatValue = ((JSONObject) value).toJSONString();
			}

			// 3. 布尔值映射识别
			boolean enableBooleanMapRecognition = jdbcProperties.isEnableBooleanMapRecognition();
			boolean isBoolean = BooleanUtil.isBoolean(valueClass);
			boolean isStrBoolean = false;
			if (!isBoolean && valueClass == String.class) {
				isStrBoolean = StrUtil.equalsAnyIgnoreCase((String) value, "true", "false");
			}
			if (enableBooleanMapRecognition && isBoolean || isStrBoolean) {
				if (!StrUtil.startWith(key, IS_PREFIX, true, true)) {
					String aliasFormat = String.format(IS_PREFIX_FORMAT, PropertyNamingStrategy.SnakeCase.translate(key));
					formatKey = jdbcProperties.getDatabaseFieldNamingStrategy().getPropertyNamingStrategy().translate(aliasFormat);
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

}
