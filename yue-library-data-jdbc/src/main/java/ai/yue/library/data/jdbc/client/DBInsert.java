package ai.yue.library.data.jdbc.client;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.exception.DBException;
import ai.yue.library.base.util.ListUtils;
import ai.yue.library.base.util.MapUtils;
import ai.yue.library.base.util.StringUtils;
import ai.yue.library.base.view.ResultErrorPrompt;
import ai.yue.library.data.jdbc.constant.DBUpdateEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>SQL优化型数据库操作</h2>
 * Created by sunJinChuan on 2016/6/6
 * @since 0.0.1
 */
@Slf4j
class DBInsert {
	
	JdbcTemplate jdbcTemplate;
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	// Insert
	
	/**
	 * 向表中插入一条数据，主键默认为id时使用。
	 * @param tableName 表名
	 * @param paramMap 参数
	 * @return 返回主键值
	 */
	@Transactional
	public Long insert(String tableName, Map<String, Object> paramMap) {
		paramValidate(tableName, paramMap);
		// 1. 创建JdbcInsert实例
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
		simpleJdbcInsert.setTableName(tableName); // 设置表名
		simpleJdbcInsert.setGeneratedKeyName("id");	// 设置主键名，添加成功后返回主键的值
		
		// 2. 设置ColumnNames
		List<String> keys = MapUtils.keyList(paramMap);
		List<String> columnNames = ListUtils.toList(jdbcTemplate.queryForList("desc " + tableName), "Field");
		List<String> insertColumn = ListUtils.keepSameValue(keys, columnNames);
		simpleJdbcInsert.setColumnNames(insertColumn);
		
		return simpleJdbcInsert.executeAndReturnKey(paramMap).longValue();
	}
	
	/**
	 * 向表中批量插入数据，主键默认为id时使用。
	 * @param tableName 表名
	 * @param paramMap 参数
	 */
	@Transactional
	public void insertBatch(String tableName, Map<String, Object>[] paramMaps) {
		paramValidate(tableName, paramMaps);
		// 1. 创建JdbcInsert实例
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
		simpleJdbcInsert.setTableName(tableName); // 设置表名
		simpleJdbcInsert.setGeneratedKeyName("id");	// 设置主键名
		
		// 2. 设置ColumnNames
		List<String> keys = MapUtils.keyList(paramMaps[0]);
		List<String> columnNames = ListUtils.toList(jdbcTemplate.queryForList("desc " + tableName), "Field");
		List<String> insertColumn = ListUtils.keepSameValue(keys, columnNames);
		simpleJdbcInsert.setColumnNames(insertColumn);
        int updateRowsNumber = simpleJdbcInsert.executeBatch(paramMaps).length;
        
        if (updateRowsNumber != paramMaps.length) {
        	throw new DBException(ResultErrorPrompt.INSERT_ERROR_BATCH);
        }
	}
	
	// InsertOrUpdate
	
    /**
     * <b>插入或更新优化SQL（表中必须存在数据唯一性约束）</b><br>
     * <p>&nbsp;说明：若唯一约束数据不存在便插入数据，若唯一约束数据已存在便更新数据</p><br>
     * <b>SQL示例：</b></br>
     * <code>INSERT INTO table (param1, param2, ...)</code><br>
     * <code>VALUES</code><br>
     * <code>(:param1, :param2, ...)</code><br>
     * <code>ON DUPLICATE KEY UPDATE</code><br>
     * <code>condition = condition + :condition, ...</code>
     * @param tableName		表名
     * @param paramMap		插入或更新所用到的参数
     * @param conditions	更新条件（对应paramMap内的key值）
     * @param dBUpdateEnum	更新类型 {@linkplain DBUpdateEnum}
     * @return
     */
    @Transactional
    public Long insert_or_update(String tableName, Map<String, Object> paramMap, String[] conditions, DBUpdateEnum dBUpdateEnum) {
        paramValidate(tableName, paramMap, conditions);
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO ");
        sql.append(tableName);
        sql.append(" (");
        Set<String> keys = paramMap.keySet();
        Iterator<String> it = keys.iterator();
        Iterator<String> iterator = keys.iterator();
        
        while (it.hasNext()) {
			String key = it.next();
			sql.append(key);
			if(it.hasNext()) {
				sql.append(", ");
			}
		}
        sql.append(") VALUES (");
        
        while (iterator.hasNext()) {
        	String key = iterator.next();
    		sql.append(":");
    		sql.append(key);
    		if(iterator.hasNext()) {
    			sql.append(", ");
    		}
		}
        sql.append(") ON DUPLICATE KEY UPDATE ");
        
    	for (String condition : conditions) {
    		sql.append(condition);
    		sql.append(" = ");
    		if (dBUpdateEnum == DBUpdateEnum.正常) {// 正常更新
    			sql.append(":" + condition);
    		}else {
    			sql.append(condition);
    			if (dBUpdateEnum == DBUpdateEnum.递增) {// 递增更新
    				sql.append(" + :");
    			}else {// 递减更新
    				sql.append(" - :");
    			}
    			sql.append(condition);
    		}
			sql.append(", ");
    	}
    	sql = StringUtils.deleteLastEqualString(sql, ", ");
        return (long) namedParameterJdbcTemplate.update(sql.toString(), paramMap);
    }
	
    // 方法
    
	/**
     * 判断更新所影响的行数是否 <b>等于</b> 预期值
     * <p>
     * 若不是预期值，那么将会抛出一个{@linkplain DBException}
     * @param updateRowsNumber	更新所影响的行数
     * @param expectedValue		预期值
     */
	public void updateAndExpectedEqual(long updateRowsNumber, int expectedValue) {
        if (updateRowsNumber != expectedValue) {
    		String msg = ResultErrorPrompt.dataStructure(expectedValue, updateRowsNumber);
        	throw new DBException(msg);
        }
	}
    
    /**
     * 判断更新所影响的行数是否 <b>等于</b> 预期值
     * <p>
     * 若不是预期值，同时 updateRowsNumber > 0 那么将会抛出一个{@linkplain DBException}
     * @param updateRowsNumber	更新所影响的行数
     * @param expectedValue		预期值
     * @return
     */
	public boolean isUpdateAndExpectedEqual(long updateRowsNumber, int expectedValue) {
        if (updateRowsNumber == expectedValue) {
        	return true;
        }
        if (updateRowsNumber == 0) {
        	return false;
        }
        
        String msg = ResultErrorPrompt.dataStructure(expectedValue, updateRowsNumber);
        throw new DBException(msg);
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
        	String msg = ResultErrorPrompt.dataStructure(">= " + expectedValue, updateRowsNumber);
            throw new DBException(msg);
        }
	}
	
    /**
     * 判断更新所影响的行数是否 <b>大于等于</b> 预期值
     * <p>
     * 若不是预期结果，同时 updateRowsNumber < expectedValue 那么将会抛出一个{@linkplain DBException}
     * @param updateRowsNumber	更新所影响的行数
     * @param expectedValue		预期值
     * @return
     */
	public boolean isUpdateAndExpectedGreaterThanEqual(long updateRowsNumber, int expectedValue) {
        if (updateRowsNumber >= expectedValue) {
        	return true;
        }
        if (updateRowsNumber == 0) {
        	return false;
        }
        
        String msg = ResultErrorPrompt.dataStructure(">= " + expectedValue, updateRowsNumber);
        throw new DBException(msg);
	}
	
    /**
     * <b>绝对查询参数whereSql化</b>
     * <p>
     * <i>已对NULL值进行特殊处理（IS NULL）</i><br><br>
     * 
     * 结果示例：<br>
     * <code>WHERE 1 = 1</code><br>
     * <code>AND</code><br>
     * <code>param1 = :param1</code><br>
     * <code>AND</code><br>
     * <code>param2 = :param2</code><br>
     * <code>AND ...</code>
     * @param paramMap
     * @return
     */
    public String paramToWhereSql(Map<String, Object> paramMap) {
    	StringBuffer sql = new StringBuffer();
    	sql.append(" WHERE 1 = 1 ");
		paramMap.keySet().forEach(condition -> {
			sql.append(" AND ");
			sql.append(condition);
			if (null == paramMap.get(condition)) {
				sql.append(" IS :");
			}else {
				sql.append(" = :");
			}
			sql.append(condition);
		});
		return sql.toString();
    }
    
    /**
     * 同 {@linkplain #queryForMap(String, Map)} 的安全查询结果获取
     * @param list {@linkplain #queryForList(String, Map)} 查询结果
     * @return
     */
    public JSONObject resultToJSON(List<Map<String, Object>> list) {
    	int size = list.size();
    	int expectedValue = 1;
    	if (size != expectedValue) {
    		if (size > expectedValue) {
    			String msg = ResultErrorPrompt.dataStructure(expectedValue, size);
    			log.warn(msg);
    		}
    		
    		return null;
    	}
    	
    	return new JSONObject(list.get(0));
	}
    
    /**
     * 同 {@linkplain #queryForObject(String, Map, Class)} 的安全查询结果获取
     * @param <T>
     * @param list {@linkplain #queryForList(String, Map, Class)} 查询结果
     * @return
     */
    public <T> T resultToObject(List<T> list) {
    	int size = list.size();
    	int expectedValue = 1;
    	if (size != expectedValue) {
    		if (size > expectedValue) {
    			String msg = ResultErrorPrompt.dataStructure(expectedValue, size);
    			log.warn(msg);
    		}
    		
    		return null;
    	}
    	
    	return list.get(0);
	}
    
    // ParamValidate
    
	/**
	 * 参数验证
	 * @param tableName
	 */
	void paramValidate(String tableName) {
		if (StringUtils.isEmpty(tableName)) {
			throw new DBException("表名不能为空");
		}
	}
	
	/**
	 * 参数验证
	 * @param tableName
	 * @param whereSql
	 */
    void paramValidate(String tableName, String whereSql) {
		if (StringUtils.isEmpty(tableName)) {
			throw new DBException("表名不能为空");
		}
		if (StringUtils.isEmpty(tableName)) {
			throw new DBException("whereSql不能为空");
		}
	}
    
	/**
	 * 参数验证
	 * @param tableName
	 * @param id
	 */
	void paramValidate(String tableName, Long id) {
		if (StringUtils.isEmpty(tableName)) {
			throw new DBException("表名不能为空");
		}
		if (null == id) {
			throw new DBException("参数id不能为空");
		}
	}
    
	/**
	 * 参数验证
	 * @param tableName
	 * @param id
	 * @param fieldName
	 */
	void paramValidate(String tableName, Long id, String[] fieldName) {
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
	 * @param tableName
	 * @param paramMap
	 */
	void paramValidate(String tableName, Map<String, Object> paramMap) {
		if (StringUtils.isEmpty(tableName)) {
			throw new DBException("表名不能为空");
		}
		if (paramMap.isEmpty()) {
			throw new DBException("参数不能为空");
		}
	}
	
	/**
	 * 参数验证
	 * @param tableName
	 * @param paramMaps
	 */
	void paramValidate(String tableName, Map<String, Object>[] paramMaps) {
		if (StringUtils.isEmpty(tableName)) {
			throw new DBException("表名不能为空");
		}
		if (MapUtils.isEmptys(paramMaps)) {
			throw new DBException("参数不能为空");
		}
	}
	
	/**
	 * 参数验证
	 * @param tableName
	 * @param paramMap
	 * @param conditions
	 */
	void paramValidate(String tableName, Map<String, Object> paramMap, String[] conditions) {
		if (StringUtils.isEmpty(tableName)) {
			throw new DBException("表名不能为空");
		}
		if (paramMap.isEmpty()) {
			throw new DBException("参数不能为空");
		}
        if (StringUtils.isEmptys(conditions) || !MapUtils.isKeys(paramMap, conditions)) {
        	throw new DBException("更新条件不能为空");
        }
	}
	
}
