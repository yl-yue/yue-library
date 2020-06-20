package ai.yue.library.data.jdbc.client;

import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.exception.DbException;
import ai.yue.library.base.util.MapUtils;
import ai.yue.library.base.view.ResultPrompt;
import ai.yue.library.data.jdbc.constant.DbConstant;
import ai.yue.library.data.jdbc.constant.DbUpdateEnum;

/**
 * <h2>SQL优化型数据库操作</h2>
 * Created by sunJinChuan on 2016/6/6
 * @since 0.0.1
 */
class DbDelete extends DbUpdate {
	
	// Delete
	
	private String deleteSqlBuild(String tableName, JSONObject paramJson) {
		// 1. 参数验证
		paramValidate(tableName, paramJson);
		
		// 2. 生成SQL
		StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM ");
		sql.append(dialect.getWrapper().wrap(tableName));
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
		paramJson.put(dialect.getWrapper().wrap(DbConstant.PRIMARY_KEY), id);
		String sql = deleteSqlBuild(tableName, paramJson);
		
		// 3. 执行删除
        int updateRowsNumber = namedParameterJdbcTemplate.update(sql, paramJson);
        
        // 4. 确认影响的数据条数
        if (updateRowsNumber != 1) {
        	throw new DbException(ResultPrompt.DELETE_ERROR);
        }
    }
	
	/**
	 * 删除-安全的
     * <p>数据删除前会先进行条数确认
     * 
     * @param tableName	表名
     * @param id     	主键id
     */
    public void deleteSafe(String tableName, Long id) {
		// 1. 确认数据
    	JSONObject data = getById(tableName, id);
		if (data == null || data.isEmpty()) {
			throw new DbException("执行单行删除命令失败，数据结构异常，可能原因是：数据不存在或存在多条数据", true);
		}
		
		// 2. 删除数据
		delete(tableName, id);
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
		String sql = deleteSqlBuild(tableName, paramJson);
		paramJson = dialect.getWrapper().wrap(paramJson);
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
		String sql = deleteSqlBuild(tableName, paramJsons[0]);
		
		// 2. 执行
		paramJsons = dialect.getWrapper().wrap(paramJsons);
        int[] updateRowsNumberArray = namedParameterJdbcTemplate.batchUpdate(sql, paramJsons);
        
        // 3. 确认影响行数
        for (int updateRowsNumber : updateRowsNumberArray) {
			if (updateRowsNumber > 1) {
				throw new DbException(ResultPrompt.DELETE_BATCH_ERROR);
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
	
	// Delete Logic
	
	private String deleteLogicSqlBuild(String tableName, JSONObject paramJson) {
		// 1. 参数验证
		paramValidate(tableName, paramJson);
		
		// 2. 生成SQL
		String[] conditions = new String[paramJson.size()];
		conditions = MapUtils.keyList(paramJson).toArray(conditions);
		paramJson.put(DbConstant.FIELD_DEFINITION_DELETE_TIME, System.currentTimeMillis());
		
		// 3. 返回结果
		return dialect.updateSqlBuild(tableName, paramJson, conditions, DbUpdateEnum.NORMAL);
	}
	
	/**
	 * 删除-逻辑的
	 * <p>数据非真实删除，而是更改 {@value DbConstant#FIELD_DEFINITION_DELETE_TIME} 字段值为 true，代表数据已删除
     * 
     * @param tableName	表名
     * @param id     	主键id
     */
	@Transactional
    public void deleteLogic(String tableName, Long id) {
    	// 1. 参数验证
		paramValidate(tableName, id);
		
		// 2. 获得SQL
		JSONObject paramJson = new JSONObject();
		paramJson.put(dialect.getWrapper().wrap(DbConstant.PRIMARY_KEY), id);
		String sql = deleteLogicSqlBuild(tableName, paramJson);
		
		// 3. 执行删除
        int updateRowsNumber = namedParameterJdbcTemplate.update(sql, paramJson);
        
        // 4. 确认影响的数据条数
        if (updateRowsNumber != 1) {
        	throw new DbException(ResultPrompt.DELETE_ERROR);
        }
    }
	
	/**
	 * 删除-逻辑的
	 * <p>数据非真实删除，而是更改 {@value DbConstant#FIELD_DEFINITION_DELETE_TIME} 字段值为 true，代表数据已删除
     * 
     * @param tableName		表名
     * @param paramJson		条件
     * @return 删除所影响的行数
     */
	@Transactional
	public long deleteLogic(String tableName, JSONObject paramJson) {
		String sql = deleteLogicSqlBuild(tableName, paramJson);
		paramJson = dialect.getWrapper().wrap(paramJson);
		return (long) namedParameterJdbcTemplate.update(sql, paramJson);
	}
	
	/**
	 * 删除-批量-逻辑的
	 * <p>数据非真实删除，而是更改 {@value DbConstant#FIELD_DEFINITION_DELETE_TIME} 字段值为 true，代表数据已删除
	 * <p>一组条件对应一条数据，并且每组条件都采用相同的key
     * 
     * @param tableName		表名
     * @param paramJsons	条件数组
     */
	@Transactional
    public void deleteBatchLogic(String tableName, JSONObject[] paramJsons) {
		// 1. 获得SQL
		String sql = deleteLogicSqlBuild(tableName, paramJsons[0]);
		
		// 2. 执行
		paramJsons = dialect.getWrapper().wrap(paramJsons);
        int[] updateRowsNumberArray = namedParameterJdbcTemplate.batchUpdate(sql, paramJsons);
        
        // 3. 确认影响行数
        for (int updateRowsNumber : updateRowsNumberArray) {
			if (updateRowsNumber > 1) {
				throw new DbException(ResultPrompt.DELETE_BATCH_ERROR);
			}
		}
    }
	
}
