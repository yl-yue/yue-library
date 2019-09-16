package ai.yue.library.data.jdbc.client;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.exception.DBException;
import ai.yue.library.base.util.MapUtils;
import ai.yue.library.base.util.StringUtils;
import ai.yue.library.base.view.ResultPrompt;
import cn.hutool.core.util.ArrayUtil;
import lombok.var;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>SQL优化型数据库操作</h2>
 * Created by sunJinChuan on 2016/6/6
 * @since 0.0.1
 */
@Slf4j
class DBBase {
	
	protected JdbcTemplate jdbcTemplate;
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
    // 方法
	
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
     * <p>若不是预期值，同时 updateRowsNumber &gt; 0 那么将会抛出一个 {@linkplain DBException}
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
        throw new DBException(msg);
	}
	
    /**
     * 判断更新所影响的行数是否 <b>大于等于</b> 预期值
     * <p>
     * 若不是预期结果，同时 updateRowsNumber &lt; expectedValue 那么将会抛出一个{@linkplain DBException}
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
        throw new DBException(msg);
	}
	
	/**
     * 判断更新所影响的行数是否 <b>等于</b> 预期值
     * <p>
     * 若不是预期值，那么将会抛出一个{@linkplain DBException}
     * @param updateRowsNumber	更新所影响的行数
     * @param expectedValue		预期值
     */
	public void updateAndExpectedEqual(long updateRowsNumber, int expectedValue) {
        if (updateRowsNumber != expectedValue) {
    		String msg = ResultPrompt.dataStructure(expectedValue, updateRowsNumber);
        	throw new DBException(msg);
        }
	}
	
    /**
     * 判断更新所影响的行数是否 <b>大于等于</b> 预期值
     * <p>
     * 若不是预期结果，那么将会抛出一个{@linkplain DBException}
     * @param updateRowsNumber	更新所影响的行数
     * @param expectedValue		预期值
     */
	public void updateAndExpectedGreaterThanEqual(long updateRowsNumber, int expectedValue) {
        if (!(updateRowsNumber >= expectedValue)) {
        	String msg = ResultPrompt.dataStructure(">= " + expectedValue, updateRowsNumber);
            throw new DBException(msg);
        }
	}
	
	/**
     * 确认批量更新每组参数所影响的行数，是否 <b>全部都等于</b> 同一个预期值
     * <p>
     * 若不是预期值，那么将会抛出一个{@linkplain DBException}
     * @param updateRowsNumberArray	每组参数更新所影响的行数数组
     * @param expectedValue 预期值
     */
	public void updateBatchAndExpectedEqual(int[] updateRowsNumberArray, int expectedValue) {
		for (int updateRowsNumber : updateRowsNumberArray) {
			if (updateRowsNumber != expectedValue) {
				String msg = ResultPrompt.UPDATE_BATCH_ERROR;
				msg += ResultPrompt.dataStructure(expectedValue, updateRowsNumber);
				throw new DBException(msg);
			}
		}
	}
	
    /**
     * 同 {@linkplain DBQuery#queryForJson(String, JSONObject)} 的安全查询结果获取
     * @param list {@linkplain DBQuery#queryForList(String, JSONObject)} 查询结果
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
     * 同 {@linkplain DBQuery#queryForObject(String, JSONObject, Class)} 的安全查询结果获取
     * @param <T> 泛型
     * @param list {@linkplain DBQuery#queryForList(String, JSONObject, Class)} 查询结果
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
    
	private synchronized void paramToWhereSql(StringBuffer whereSql, final JSONObject paramJson,
			final String condition) {
		whereSql.append(" AND ");
		whereSql.append(condition);
		var value = paramJson.get(condition);
		if (null == value) {
			whereSql.append(" IS :");
			whereSql.append(condition);
		} else if (value instanceof List) {
			whereSql.append(" IN (:");
			whereSql.append(condition);
			whereSql.append(") ");
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
    	whereSql.append(" WHERE 1 = 1 ");
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
			throw new DBException("表名不能为空");
		}
	}
	
	/**
	 * 参数验证
	 * @param tableName 表名
	 * @param whereSql 条件sql
	 */
    protected void paramValidate(String tableName, String whereSql) {
		if (StringUtils.isEmpty(tableName)) {
			throw new DBException("表名不能为空");
		}
		if (StringUtils.isEmpty(tableName)) {
			throw new DBException("whereSql不能为空");
		}
	}
    
	/**
	 * 参数验证
	 * @param tableName 表名
	 * @param id 主键ID
	 */
    protected void paramValidate(String tableName, Long id) {
		if (StringUtils.isEmpty(tableName)) {
			throw new DBException("表名不能为空");
		}
		if (null == id) {
			throw new DBException("参数id不能为空");
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
			throw new DBException("表名不能为空");
		}
		if (null == id) {
			throw new DBException("参数id不能为空");
		}
		if (StringUtils.isEmptys(fieldName)) {
			throw new DBException("fieldName不能为空");
		}
	}
	
	/**
	 * 参数验证
	 * @param tableName 表名
	 * @param paramJson 参数
	 */
    protected void paramValidate(String tableName, JSONObject paramJson) {
		if (StringUtils.isEmpty(tableName)) {
			throw new DBException("表名不能为空");
		}
		if (MapUtils.isEmpty(paramJson)) {
			throw new DBException("参数不能为空");
		}
	}
	
	/**
	 * 参数验证
	 * @param tableName 表名
	 * @param paramJsons 参数数组
	 */
    protected void paramValidate(String tableName, JSONObject[] paramJsons) {
		if (StringUtils.isEmpty(tableName)) {
			throw new DBException("表名不能为空");
		}
		if (MapUtils.isEmptys(paramJsons)) {
			throw new DBException("参数不能为空");
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
			throw new DBException("表名不能为空");
		}
		if (MapUtils.isEmpty(paramJson)) {
			throw new DBException("参数不能为空");
		}
        if (StringUtils.isEmptys(conditions) || !MapUtils.isKeys(paramJson, conditions)) {
        	throw new DBException("更新条件不能为空");
        }
	}
	
}
