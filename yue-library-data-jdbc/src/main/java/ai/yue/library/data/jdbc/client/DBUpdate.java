package ai.yue.library.data.jdbc.client;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import ai.yue.library.base.exception.DBException;
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
class DBUpdate extends DBDelete {
	
	// Update

    private String updateSql(String tableName, Map<String, Object> paramMap, String[] conditions, DBUpdateEnum dBUpdateEnum) {
		paramValidate(tableName, paramMap, conditions);
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE ");
        sql.append(tableName);
        sql.append(" SET ");
        
        Set<String> keys = paramMap.keySet();
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
		String whereSql = paramToWhereSql(paramMap, conditions);
		sql.append(whereSql);
        
		if (dBUpdateEnum == DBUpdateEnum.递减_无符号) {// 递减-无符号更新
			List<String> updateKeys = MapUtils.keyList(paramMap);
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
     * @param paramMap      更新所用到的参数（where条件参数不会用于set值的更新）
     * @param conditions	作为更新条件的参数名，对应paramMap内的key（注意：作为条件的参数，将不会用于字段值的更新）
     * @return
     */
	@Transactional
    public Long update(String tableName, Map<String, Object> paramMap, String[] conditions) {
		String sql = updateSql(tableName, paramMap, conditions, DBUpdateEnum.正常);
        return (long) namedParameterJdbcTemplate.update(sql, paramMap);
    }
	
	/**
     * <b>绝对</b>条件更新优化SQL<br><br>
     * @param tableName    	表名
     * @param paramMap      更新所用到的参数
     * @param conditions	作为更新条件的参数名，对应paramMap内的key（注意：作为条件的参数，将不会用于字段值的更新）
     * @param dBUpdateEnum	更新类型 {@linkplain DBUpdateEnum}
     * @return
     */
	@Transactional
    public Long update(String tableName, Map<String, Object> paramMap, String[] conditions, DBUpdateEnum dBUpdateEnum) {
		String sql = updateSql(tableName, paramMap, conditions, dBUpdateEnum);
        return (long) namedParameterJdbcTemplate.update(sql, paramMap);
	}
	
	/**
     * <b>绝对</b>条件更新优化SQL<br><br>
     * @param tableName    		表名
     * @param paramMap      	更新所用到的参数
     * @param conditions		作为更新条件的参数名，对应paramMap内的key（注意：作为条件的参数，将不会用于字段值的更新）
     * @param dBUpdateEnum		更新类型 {@linkplain DBUpdateEnum}
     * @param expectedValue				更新所影响的行数预期值
     * @param dBExpectedValueModeEnum	预期值确认方式
     */
	@Transactional
    public void update(String tableName, Map<String, Object> paramMap, String[] conditions, DBUpdateEnum dBUpdateEnum
    		, int expectedValue, DBExpectedValueModeEnum dBExpectedValueModeEnum) {
		String sql = updateSql(tableName, paramMap, conditions, dBUpdateEnum);
		
		int updateRowsNumber = namedParameterJdbcTemplate.update(sql, paramMap);
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
     * @param paramMap					更新所用到的参数
     * @param expectedValue				更新所影响的行数预期值
     * @param dBExpectedValueModeEnum	预期值确认方式
     */
	@Transactional
	public void update(String sql, Map<String, Object> paramMap, int expectedValue, DBExpectedValueModeEnum dBExpectedValueModeEnum) {
		int updateRowsNumber = namedParameterJdbcTemplate.update(sql, paramMap);
		if (DBExpectedValueModeEnum.等于 == dBExpectedValueModeEnum) {
			updateAndExpectedEqual(updateRowsNumber, expectedValue);
		} else if (DBExpectedValueModeEnum.大于等于 == dBExpectedValueModeEnum) {
			updateAndExpectedGreaterThanEqual(updateRowsNumber, expectedValue);
		}
	}

    private String updateByIdSql(String tableName, Map<String, Object> paramMap, DBUpdateEnum dBUpdateEnum) {
		paramValidate(tableName, paramMap);
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE ");
        sql.append(tableName);
        sql.append(" SET ");
        
        Set<String> keys = paramMap.keySet();
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
			List<String> updateKeys = MapUtils.keyList(paramMap);
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
     * @param paramMap      更新所用到的参数（包含主键ID字段）
     */
	@Transactional
    public void updateById(String tableName, Map<String, Object> paramMap) {
		String sql = updateByIdSql(tableName, paramMap, DBUpdateEnum.正常);
        long updateRowsNumber = (long) namedParameterJdbcTemplate.update(sql, paramMap);
        
        if (!isUpdateAndExpectedEqual(updateRowsNumber, 1)) {
        	throw new DBException(ResultErrorPrompt.DB_ERROR);
        }
    }
	
	/**
     * 根据表中ID主键进行更新
     * @param tableName    	表名
     * @param paramMap      更新所用到的参数（包含主键ID字段）
     * @param dBUpdateEnum	更新类型 {@linkplain DBUpdateEnum}
     */
	@Transactional
    public void updateById(String tableName, Map<String, Object> paramMap, DBUpdateEnum dBUpdateEnum) {
		String sql = updateByIdSql(tableName, paramMap, dBUpdateEnum);
        long updateRowsNumber = (long) namedParameterJdbcTemplate.update(sql, paramMap);
        
        if (!isUpdateAndExpectedEqual(updateRowsNumber, 1)) {
        	throw new DBException(ResultErrorPrompt.DB_ERROR);
        }
    }
	
	/**
     * 根据表中ID主键进行更新-批量
     * @param tableName    	表名
     * @param paramMaps     更新所用到的参数（包含主键ID字段）
     * @param dBUpdateEnum	更新类型 {@linkplain DBUpdateEnum}
     */
	@Transactional
    public void updateById(String tableName, Map<String, Object>[] paramMaps, DBUpdateEnum dBUpdateEnum) {
		String sql = updateByIdSql(tableName, paramMaps[0], dBUpdateEnum);
        int updateRowsNumber = namedParameterJdbcTemplate.batchUpdate(sql, paramMaps).length;
        int expectedValue = paramMaps.length;
        
        updateAndExpectedEqual(updateRowsNumber, expectedValue);
    }
	
	/**
     * <b>绝对</b> 条件 <b>批量</b> 更新优化SQL<br><br>
     * @param tableName    	表名
     * @param paramMaps     更新所用到的参数数组
     * @param conditions	作为更新条件的参数名，对应paramMap内的key（注意：作为条件的参数，将不会用于字段值的更新）
     * @param dBUpdateEnum	更新类型 {@linkplain DBUpdateEnum}
     */
	@Transactional
    public void updateBatch(String tableName, Map<String, Object>[] paramMaps, String[] conditions, DBUpdateEnum dBUpdateEnum) {
		String sql = updateSql(tableName, paramMaps[0], conditions, dBUpdateEnum);
        int updateRowsNumber = namedParameterJdbcTemplate.batchUpdate(sql, paramMaps).length;
        int expectedValue = paramMaps.length;
        
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
     * @param paramMaps					更新所用到的参数数组
     * @param expectedValue				每组参数更新所影响的行数预期值
     * @param dBExpectedValueModeEnum	预期值确认方式
     */
	@Transactional
	public void updateBatch(String sql, Map<String, Object>[] paramMaps, int expectedValue, DBExpectedValueModeEnum dBExpectedValueModeEnum) {
		int[] updateRowsNumberArray = namedParameterJdbcTemplate.batchUpdate(sql, paramMaps);
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
	
}
