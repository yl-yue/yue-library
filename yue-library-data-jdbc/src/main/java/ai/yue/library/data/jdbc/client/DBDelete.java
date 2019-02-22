package ai.yue.library.data.jdbc.client;

import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.exception.DBException;
import ai.yue.library.base.view.ResultErrorPrompt;

/**
 * <h2>SQL优化型数据库操作</h2>
 * Created by sunJinChuan on 2016/6/6
 * @since 0.0.1
 */
class DBDelete extends DBUpdate {
	
	// Delete
	
	private String deleteSql(String tableName, JSONObject paramJson) {
		// 1. 参数验证
		paramValidate(tableName, paramJson);
		
		// 2. 生成SQL
		StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM ");
        sql.append(tableName);
		String whereSql = paramToWhereSql(paramJson);
		sql.append(whereSql);
		
		// 3. 返回结果
		return sql.toString();
	}
	
	/**
	 * 删除
     * 
     * @param tableName	表名
     * @param id     	主键id
     */
	@Transactional
    public void delete(String tableName, Long id) {
    	// 1. 参数验证
		paramValidate(tableName, id);
		
		// 2. 获得SQL
		JSONObject paramJson = new JSONObject();
		paramJson.put("id", id);
		String sql = deleteSql(tableName, paramJson);
		
		// 3. 执行删除
        int updateRowsNumber = namedParameterJdbcTemplate.update(sql, paramJson);
        
        // 4. 确认影响的数据条数
        if (updateRowsNumber != 1) {
        	throw new DBException(ResultErrorPrompt.DELETE_ERROR);
        }
    }
	
	/**
	 * 删除
     * 
     * @param tableName		表名
     * @param paramJson		条件
     * @return 删除所影响的行数
     */
	@Transactional
	public long delete(String tableName, JSONObject paramJson) {
		String sql = deleteSql(tableName, paramJson);
		return (long) namedParameterJdbcTemplate.update(sql, paramJson);
	}
	
	/**
	 * 删除-批量
	 * <p>一组条件对应一条数据，并且每组条件都采用相同的key
     * 
     * @param tableName		表名
     * @param paramJsons	条件数组
     */
	@Transactional
    public void deleteBatch(String tableName, JSONObject[] paramJsons) {
		// 1. 获得SQL
		String sql = deleteSql(tableName, paramJsons[0]);
		
		// 2. 执行
        int[] updateRowsNumberArray = namedParameterJdbcTemplate.batchUpdate(sql, paramJsons);
        
        // 3. 确认影响行数
        for (int updateRowsNumber : updateRowsNumberArray) {
			if (updateRowsNumber > 1) {
				throw new DBException(ResultErrorPrompt.DELETE_BATCH_ERROR);
			}
		}
    }
	
	/**
     * 同 {@linkplain NamedParameterJdbcTemplate#batchUpdate(String, Map[])}<br>
     * <p>指定SQL语句以创建预编译执行SQL和绑定删除参数
     * <p>示例：<code>DELETE FROM table WHERE id = :id</code><br>
     * @param sql			要执行的删除SQL
     * @param paramJsons	删除所用到的条件数组
	 * @return 一个数组，其中包含受批处理中每个更新影响的行数
     */
	@Transactional
	public int[] deleteBatch2(String sql, JSONObject[] paramJsons) {
		return namedParameterJdbcTemplate.batchUpdate(sql, paramJsons);
	}
	
}
