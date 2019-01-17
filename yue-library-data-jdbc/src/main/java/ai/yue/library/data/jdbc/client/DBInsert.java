package ai.yue.library.data.jdbc.client;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.exception.DBException;
import ai.yue.library.base.util.ListUtils;
import ai.yue.library.base.util.MapUtils;
import ai.yue.library.base.util.StringUtils;
import ai.yue.library.base.view.ResultErrorPrompt;
import ai.yue.library.data.jdbc.constant.DBUpdateEnum;

/**
 * <h2>SQL优化型数据库操作</h2>
 * Created by sunJinChuan on 2016/6/6
 * @since 0.0.1
 */
class DBInsert extends DBDelete {
	
	// Insert
	
	/**
	 * 向表中插入一条数据，主键默认为id时使用。
	 * @param tableName 表名
	 * @param paramJSON 参数
	 * @return 返回主键值
	 */
	@Transactional
	public Long insert(String tableName, JSONObject paramJSON) {
		// 1. 参数验证
		paramValidate(tableName, paramJSON);
		
		// 2. 创建JdbcInsert实例
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
		simpleJdbcInsert.setTableName(tableName); // 设置表名
		simpleJdbcInsert.setGeneratedKeyName("id");	// 设置主键名，添加成功后返回主键的值
		
		// 3. 设置ColumnNames
		List<String> keys = MapUtils.keyList(paramJSON);
		List<String> columnNames = ListUtils.toList(jdbcTemplate.queryForList("desc " + tableName), "Field");
		List<String> insertColumn = ListUtils.keepSameValue(keys, columnNames);
		simpleJdbcInsert.setColumnNames(insertColumn);
		
		// 4. 执行
		return simpleJdbcInsert.executeAndReturnKey(paramJSON).longValue();
	}
	
	/**
	 * <h1>向表中插入一条数据，并自动递增 <i>sort_idx</i></h1>
	 * 
	 * <blockquote>
	 * <b>使用条件：</b>
	 * <pre>1. id 默认为主键</pre>
	 * <pre>2. sort_idx 默认为排序字段</pre>
	 * </blockquote>
	 * 
	 * @param tableName 表名
	 * @param paramJSON 插入数据
	 * @param uniqueKeys 同sort_idx字段组合的唯一约束keys（表中不建议建立sort_idx字段的唯一约束，但可以建立普通索引，以便于提高查询性能），<b>可选参数</b>
	 * @return 返回主键值
	 */
	@Transactional
	public Long insertWithSortIdxAutoIncrement(String tableName, JSONObject paramJSON, @Nullable String... uniqueKeys) {
		// 1. 参数验证
		paramValidate(tableName, paramJSON);
		
		// 2. 组装最大sort_idx值查询SQL
		int sort_idx = 1;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT sort_idx FROM ");
		sql.append(tableName);
		String whereSql = paramToWhereSql(paramJSON, uniqueKeys);
		sql.append(whereSql);
		sql.append(" ORDER BY sort_idx DESC LIMIT 1");
		
		// 3. 查询最大sort_idx值
		JSONObject result = queryForJSON(sql.toString(), paramJSON);
		if (result != null) {
			sort_idx = result.getInteger("sort_idx") + 1;
		}
		
		// 4. put sort_idx值
		paramJSON.put("sort_idx", sort_idx);
		
		// 5. 执行
		return insert(tableName, paramJSON);
	}
	
	/**
	 * 向表中批量插入数据，主键默认为id时使用。
	 * @param tableName 表名
	 * @param paramJSONs 参数
	 */
	@Transactional
	public void insertBatch(String tableName, JSONObject[] paramJSONs) {
		// 1. 参数验证
		paramValidate(tableName, paramJSONs);
		
		// 2. 创建JdbcInsert实例
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
		simpleJdbcInsert.setTableName(tableName); // 设置表名
		simpleJdbcInsert.setGeneratedKeyName("id");	// 设置主键名
		
		// 3. 设置ColumnNames
		List<String> keys = MapUtils.keyList(paramJSONs[0]);
		List<String> columnNames = ListUtils.toList(jdbcTemplate.queryForList("desc " + tableName), "Field");
		List<String> insertColumn = ListUtils.keepSameValue(keys, columnNames);
		simpleJdbcInsert.setColumnNames(insertColumn);
		
		// 4. 执行
        int updateRowsNumber = simpleJdbcInsert.executeBatch(paramJSONs).length;
        
        // 5. 确认插入条数
        if (updateRowsNumber != paramJSONs.length) {
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
     * @param paramJSON		插入或更新所用到的参数
     * @param conditions	更新条件（对应paramJSON内的key值）
     * @param dBUpdateEnum	更新类型 {@linkplain DBUpdateEnum}
     * @return
     */
    @Transactional
    public Long insert_or_update(String tableName, JSONObject paramJSON, String[] conditions, DBUpdateEnum dBUpdateEnum) {
        paramValidate(tableName, paramJSON, conditions);
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO ");
        sql.append(tableName);
        sql.append(" (");
        Set<String> keys = paramJSON.keySet();
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
        return (long) namedParameterJdbcTemplate.update(sql.toString(), paramJSON);
    }
	
}
