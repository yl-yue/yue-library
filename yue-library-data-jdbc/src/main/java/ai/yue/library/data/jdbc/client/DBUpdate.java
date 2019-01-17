package ai.yue.library.data.jdbc.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.exception.DBException;
import ai.yue.library.base.util.ListUtils;
import ai.yue.library.base.util.MapUtils;
import ai.yue.library.base.util.StringUtils;
import ai.yue.library.base.view.ResultErrorPrompt;
import ai.yue.library.data.jdbc.constant.DBExpectedValueModeEnum;
import ai.yue.library.data.jdbc.constant.DBUpdateEnum;
import cn.hutool.core.util.ArrayUtil;

/**
 * <h2>SQL优化型数据库操作</h2>
 * Created by sunJinChuan on 2016/6/6
 * @since 0.0.1
 */
class DBUpdate extends DBQuery {
	
	// Update

    private String updateSql(String tableName, JSONObject paramJSON, String[] conditions, DBUpdateEnum dBUpdateEnum) {
		paramValidate(tableName, paramJSON, conditions);
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE ");
        sql.append(tableName);
        sql.append(" SET ");
        
        Set<String> keys = paramJSON.keySet();
		Iterator<String> it = keys.iterator();
		while (it.hasNext()) {
			String key = it.next();
			// 排除更新条件
			if (!ArrayUtil.contains(conditions, key)) {
				sql.append(key);
				sql.append(" = ");
				if (dBUpdateEnum == DBUpdateEnum.递增) {// 递增更新
					sql.append(key);
					sql.append(" + :");
				} else if (dBUpdateEnum == DBUpdateEnum.递减 // 递减更新
						|| dBUpdateEnum == DBUpdateEnum.递减_无符号) {// 递减-无符号更新
					sql.append(key);
					sql.append(" - :");
				} else {// 正常更新
					sql.append(":");
				}
				sql.append(key);
				sql.append(", ");
			}
		}
		sql = StringUtils.deleteLastEqualString(sql, ", ");
		String whereSql = paramToWhereSql(paramJSON, conditions);
		sql.append(whereSql);
        
		if (dBUpdateEnum == DBUpdateEnum.递减_无符号) {// 递减-无符号更新
			List<String> updateKeys = MapUtils.keyList(paramJSON);
			for (String key : updateKeys) {
				// 排除更新条件
				if (!ArrayUtil.contains(conditions, key)) {
					sql.append(" AND ");
					sql.append(key);
					sql.append(" >= :");
					sql.append(key);
				}
			}
		}
        
        return sql.toString();
	}
	
	/**
     * <b>绝对</b>条件更新优化SQL<br><br>
     * <b>相对</b>条件更新优化SQL可参照如下编写：<br>
     * <code>UPDATE table</code><br>
     * <code>SET paramNumber1 = paramNumber1 - 1, ...</code><br>
     * <code>WHERE</code><br>
     * <code>id = :id</code><br>
     * <code>AND</code><br>
     * <code>paramNumber1 > 0</code><br>
     * <code>AND ...</code>
     * @param tableName    	表名
     * @param paramJSON      更新所用到的参数（where条件参数不会用于set值的更新）
     * @param conditions	作为更新条件的参数名，对应paramJSON内的key（注意：作为条件的参数，将不会用于字段值的更新）
     * @return
     */
	@Transactional
    public Long update(String tableName, JSONObject paramJSON, String[] conditions) {
		String sql = updateSql(tableName, paramJSON, conditions, DBUpdateEnum.正常);
        return (long) namedParameterJdbcTemplate.update(sql, paramJSON);
    }
	
	/**
     * <b>绝对</b>条件更新优化SQL<br><br>
     * @param tableName    	表名
     * @param paramJSON      更新所用到的参数
     * @param conditions	作为更新条件的参数名，对应paramJSON内的key（注意：作为条件的参数，将不会用于字段值的更新）
     * @param dBUpdateEnum	更新类型 {@linkplain DBUpdateEnum}
     * @return
     */
	@Transactional
    public Long update(String tableName, JSONObject paramJSON, String[] conditions, DBUpdateEnum dBUpdateEnum) {
		String sql = updateSql(tableName, paramJSON, conditions, dBUpdateEnum);
        return (long) namedParameterJdbcTemplate.update(sql, paramJSON);
	}
	
	/**
     * <b>绝对</b>条件更新优化SQL<br><br>
     * @param tableName    		表名
     * @param paramJSON      	更新所用到的参数
     * @param conditions		作为更新条件的参数名，对应paramJSON内的key（注意：作为条件的参数，将不会用于字段值的更新）
     * @param dBUpdateEnum		更新类型 {@linkplain DBUpdateEnum}
     * @param expectedValue				更新所影响的行数预期值
     * @param dBExpectedValueModeEnum	预期值确认方式
     */
	@Transactional
    public void update(String tableName, JSONObject paramJSON, String[] conditions, DBUpdateEnum dBUpdateEnum
    		, int expectedValue, DBExpectedValueModeEnum dBExpectedValueModeEnum) {
		String sql = updateSql(tableName, paramJSON, conditions, dBUpdateEnum);
		
		int updateRowsNumber = namedParameterJdbcTemplate.update(sql, paramJSON);
		if (DBExpectedValueModeEnum.等于 == dBExpectedValueModeEnum) {
			updateAndExpectedEqual(updateRowsNumber, expectedValue);
		} else if (DBExpectedValueModeEnum.大于等于 == dBExpectedValueModeEnum) {
			updateAndExpectedGreaterThanEqual(updateRowsNumber, expectedValue);
		}
	}
	
	/**
     * 同 {@linkplain NamedParameterJdbcTemplate#update(String, Map)}<br>
     * 指定SQL语句以创建预编译执行SQL和绑定更新参数
     * <blockquote>
     * 	<p>
     * 		将会对更新所影响的行数进行预期判断，若结果不符合预期值：{@linkplain expectedValue}，那么此处便会抛出一个 {@linkplain DBException}
     * 	</p>
     * </blockquote>
     * @param sql						要执行的更新SQL
     * @param paramJSON					更新所用到的参数
     * @param expectedValue				更新所影响的行数预期值
     * @param dBExpectedValueModeEnum	预期值确认方式
     */
	@Transactional
	public void update(String sql, JSONObject paramJSON, int expectedValue, DBExpectedValueModeEnum dBExpectedValueModeEnum) {
		int updateRowsNumber = namedParameterJdbcTemplate.update(sql, paramJSON);
		if (DBExpectedValueModeEnum.等于 == dBExpectedValueModeEnum) {
			updateAndExpectedEqual(updateRowsNumber, expectedValue);
		} else if (DBExpectedValueModeEnum.大于等于 == dBExpectedValueModeEnum) {
			updateAndExpectedGreaterThanEqual(updateRowsNumber, expectedValue);
		}
	}

    private String updateByIdSql(String tableName, JSONObject paramJSON, DBUpdateEnum dBUpdateEnum) {
		paramValidate(tableName, paramJSON);
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE ");
        sql.append(tableName);
        sql.append(" SET ");
        
        Set<String> keys = paramJSON.keySet();
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
			String key = it.next();
			// 排除更新条件
			if (!key.equals("id")) {
				sql.append(key).append(" = ");
				if (dBUpdateEnum == DBUpdateEnum.递增) {// 递增更新
					sql.append(key);
					sql.append(" + :");
				} else if (dBUpdateEnum == DBUpdateEnum.递减 // 递减更新
						|| dBUpdateEnum == DBUpdateEnum.递减_无符号) {// 递减-无符号更新
					sql.append(key);
					sql.append(" - :");
				} else {// 正常更新
					sql.append(":");
				}
				sql.append(key);
				sql.append(", ");
			}
		}
        sql = StringUtils.deleteLastEqualString(sql, ", ");
        sql.append(" WHERE id = :id ");
        
		if (dBUpdateEnum == DBUpdateEnum.递减_无符号) {// 递减-无符号更新
			List<String> updateKeys = MapUtils.keyList(paramJSON);
			for (String key : updateKeys) {
				// 排除更新条件
				if (!key.equals("id")) {
					sql.append(" AND ");
					sql.append(key);
					sql.append(" >= :");
					sql.append(key);
				}
			}
		}
        
        return sql.toString();
    }
	
	/**
     * 根据表中ID主键进行更新
     * @param tableName    	表名
     * @param paramJSON      更新所用到的参数（包含主键ID字段）
     */
	@Transactional
    public void updateById(String tableName, JSONObject paramJSON) {
		String sql = updateByIdSql(tableName, paramJSON, DBUpdateEnum.正常);
        long updateRowsNumber = (long) namedParameterJdbcTemplate.update(sql, paramJSON);
        
        if (!isUpdateAndExpectedEqual(updateRowsNumber, 1)) {
        	throw new DBException(ResultErrorPrompt.DB_ERROR);
        }
    }
	
	/**
     * 根据表中ID主键进行更新
     * @param tableName    	表名
     * @param paramJSON      更新所用到的参数（包含主键ID字段）
     * @param dBUpdateEnum	更新类型 {@linkplain DBUpdateEnum}
     */
	@Transactional
    public void updateById(String tableName, JSONObject paramJSON, DBUpdateEnum dBUpdateEnum) {
		String sql = updateByIdSql(tableName, paramJSON, dBUpdateEnum);
        long updateRowsNumber = (long) namedParameterJdbcTemplate.update(sql, paramJSON);
        
        if (!isUpdateAndExpectedEqual(updateRowsNumber, 1)) {
        	throw new DBException(ResultErrorPrompt.DB_ERROR);
        }
    }
	
	/**
     * 根据表中ID主键进行更新-批量
     * @param tableName    	表名
     * @param paramJSONs     更新所用到的参数（包含主键ID字段）
     * @param dBUpdateEnum	更新类型 {@linkplain DBUpdateEnum}
     */
	@Transactional
    public void updateById(String tableName, JSONObject[] paramJSONs, DBUpdateEnum dBUpdateEnum) {
		String sql = updateByIdSql(tableName, paramJSONs[0], dBUpdateEnum);
        int updateRowsNumber = namedParameterJdbcTemplate.batchUpdate(sql, paramJSONs).length;
        int expectedValue = paramJSONs.length;
        
        updateAndExpectedEqual(updateRowsNumber, expectedValue);
    }
	
	/**
     * <b>绝对</b> 条件 <b>批量</b> 更新优化SQL<br><br>
     * @param tableName    	表名
     * @param paramJSONs     更新所用到的参数数组
     * @param conditions	作为更新条件的参数名，对应paramJSON内的key（注意：作为条件的参数，将不会用于字段值的更新）
     * @param dBUpdateEnum	更新类型 {@linkplain DBUpdateEnum}
     */
	@Transactional
    public void updateBatch(String tableName, JSONObject[] paramJSONs, String[] conditions, DBUpdateEnum dBUpdateEnum) {
		String sql = updateSql(tableName, paramJSONs[0], conditions, dBUpdateEnum);
        int updateRowsNumber = namedParameterJdbcTemplate.batchUpdate(sql, paramJSONs).length;
        int expectedValue = paramJSONs.length;
        
        updateAndExpectedEqual(updateRowsNumber, expectedValue);
	}
	
	/**
     * 同 {@linkplain NamedParameterJdbcTemplate#batchUpdate(String, Map[])}<br>
     * 指定SQL语句以创建预编译执行SQL和绑定更新参数
     * <blockquote>
     * 	<p>
     * 		将会对每组参数更新所影响的行数进行预期判断，若结果不符合预期值：{@linkplain expectedValue}，那么此处便会抛出一个 {@linkplain DBException}
     * 	</p>
     * </blockquote>
     * @param sql						要执行的更新SQL
     * @param paramJSONs					更新所用到的参数数组
     * @param expectedValue				每组参数更新所影响的行数预期值
     * @param dBExpectedValueModeEnum	预期值确认方式
     */
	@Transactional
	public void updateBatch(String sql, JSONObject[] paramJSONs, int expectedValue, DBExpectedValueModeEnum dBExpectedValueModeEnum) {
		int[] updateRowsNumberArray = namedParameterJdbcTemplate.batchUpdate(sql, paramJSONs);
		if (DBExpectedValueModeEnum.等于 == dBExpectedValueModeEnum) {
			for (int updateRowsNumber : updateRowsNumberArray) {
				updateAndExpectedEqual(updateRowsNumber, expectedValue);
			}
		} else if (DBExpectedValueModeEnum.大于等于 == dBExpectedValueModeEnum) {
			for (int updateRowsNumber : updateRowsNumberArray) {
				updateAndExpectedGreaterThanEqual(updateRowsNumber, expectedValue);
			}
		}
	}
	
	/**
	 * <h1>更新-排序</h1><br>
	 * <i>使用限制：见</i> {@linkplain DBInsert#insertWithSortIdxAutoIncrement(String, Map, String...)}
	 * <p>
	 * @param tableName 表名
	 * @param id 主键ID
	 * @param move sort_idx移动位数（值不可等于零，正整数表示：向后移动几位，负整数表示：向前移动几位）
	 * @param uniqueKeys 同sort_idx字段组合的唯一约束keys（表中不建议建立sort_idx字段的唯一约束，但可以建立普通索引，以便于提高查询性能），<b>可选参数</b>
	 */
	@Transactional
	public void updateSort(String tableName, Long id, Integer move, @Nullable String... uniqueKeys) {
		// 1. 参数验证
		paramValidate(tableName);
		if (move == 0) {
			throw new DBException("move <= 0");
		}
		
		// 2. 获得当前排序索引
		String id_key = "id";
		String sort_idx_key = "sort_idx";
		JSONObject sortJSON = queryById(tableName, id);
		int sortIdx = sortJSON.getInteger(sort_idx_key);
		int updateSortIdx = sortIdx + move;
		if (updateSortIdx < 1) {
			throw new DBException("排序后的索引值不能小于1");
		}
		
		// 3. 确认排序方式
		List<Integer> updateSortList = new ArrayList<>();
		boolean isASC = false;
		if (updateSortIdx > sortIdx) {// 升序
			isASC = true;
			for (int i = updateSortIdx; i > sortIdx; i--) {
				updateSortList.add(i);
			}
		} else {// 降序
			for (int i = updateSortIdx; i < sortIdx; i++) {
				updateSortList.add(i);
			}
		}
		
		// 4. 查询需要跟随移动的数据ID
		JSONObject paramJSON = new JSONObject();
		if (ArrayUtil.isNotEmpty(uniqueKeys)) {
			for (String uniqueKey : uniqueKeys) {
				var uniqueValue = sortJSON.get(uniqueKey);
				paramJSON.put(uniqueKey, uniqueValue);
			}
		}
		paramJSON.put(sort_idx_key, updateSortList);
		List<JSONObject> list = query(tableName, paramJSON);
		
		// 5. 组装跟随移动参数到参数列表
		JSONArray paramJSONArray = new JSONArray();
		for (JSONObject actionJSON : list) {
			Long actionId = actionJSON.getLong(id_key);
			Integer actionSort = actionJSON.getInteger(sort_idx_key);
			if (isASC) {
				actionSort -= 1;
			} else {
				actionSort += 1;
			}
			JSONObject actionParamJSON = new JSONObject();
			actionParamJSON.put(id_key, actionId);
			actionParamJSON.put(sort_idx_key, actionSort);
			paramJSONArray.add(actionParamJSON);
		}
		
		// 6. 添加排序更新参数到参数列表
		JSONObject updateSortParam = new JSONObject();
		updateSortParam.put(id_key, id);
		updateSortParam.put(sort_idx_key, updateSortIdx);
		paramJSONArray.add(updateSortParam);
		
		// 7. 排序更新
		updateById(tableName, ListUtils.toJSONS(paramJSONArray), DBUpdateEnum.正常);
	}
	
}
