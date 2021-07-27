package ai.yue.library.data.jdbc.client;

import ai.yue.library.base.exception.DbException;
import ai.yue.library.base.util.MapUtils;
import ai.yue.library.base.util.ObjectUtils;
import ai.yue.library.base.view.ResultPrompt;
import ai.yue.library.data.jdbc.constant.DbConstant;
import ai.yue.library.data.jdbc.constant.DbUpdateEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

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
		sql.append(paramToWhereSql(paramJson));
		
		// 3. 返回结果
		return sql.toString();
	}

	private void deleteByUk(String tableName, Object uk) {
		// 1. 参数验证
		paramValidate(tableName);
		if (ObjectUtils.isNull(uk)) {
			throw new DbException("删除条件不能为null");
		}

		// 2. 确认数据
		JSONObject data = null;
		String key = null;
		try {
			if (uk instanceof Long) {
				data = getById(tableName, (Long) uk);
				key = DbConstant.PRIMARY_KEY;
			} else if (uk instanceof String) {
				data = getByBusinessUk(tableName, (String) uk);
				key = getJdbcProperties().getBusinessUk();
			}
		} catch (Exception e) {
			data = null;
		}
		if (data == null || data.isEmpty()) {
			throw new DbException("执行单行删除命令失败，数据结构异常，可能原因是：数据不存在或存在多条数据", true);
		}

		// 3. 获得SQL
		JSONObject paramJson = new JSONObject();
		paramJson.put(key, uk);
		String sql = deleteSqlBuild(tableName, paramJson);

		// 4. 执行删除
		int updateRowsNumber = getNamedParameterJdbcTemplate().update(sql, paramJson);

		// 5. 确认影响的数据条数
		if (updateRowsNumber != 1) {
			throw new DbException(ResultPrompt.DELETE_ERROR);
		}
	}

	/**
	 * 删除
	 * <p>数据删除前会先进行条数确认
	 * <p><code style="color:red">依赖于接口传入 {@value DbConstant#PRIMARY_KEY} 参数时慎用此方法</code>，避免有序主键被遍历风险，造成数据越权行为。推荐使用 {@link #deleteByBusinessUk(String, String)}</p>
     * 
     * @param tableName	表名
     * @param id     	主键id
     */
	@Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public void delete(String tableName, Long id) {
		deleteByUk(tableName, id);
    }
	
	/**
	 * 删除-通过表业务键
	 * <p>数据删除前会先进行条数确认
	 * <p>默认业务键为key
	 * <p>业务键值推荐使用UUID5
	 *
	 * @param tableName    表名
	 * @param businessUkValue 业务键的唯一值
	 */
	@Transactional(rollbackFor = {RuntimeException.class, Error.class})
	public void deleteByBusinessUk(String tableName, String businessUkValue) {
		deleteByUk(tableName, businessUkValue);
	}

	/**
	 * 删除
     * 
     * @param tableName		表名
     * @param paramJson		条件
     * @return 删除所影响的行数
     */
	@Transactional(rollbackFor = {RuntimeException.class, Error.class})
	public long delete(String tableName, JSONObject paramJson) {
		paramFormat(paramJson);
		dataEncrypt(tableName, paramJson);
		String sql = deleteSqlBuild(tableName, paramJson);
		return (long) getNamedParameterJdbcTemplate().update(sql, paramJson);
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
		for (JSONObject paramJson : paramJsons) {
			paramFormat(paramJson);
		}

		deleteBatchNotParamFormat(tableName, paramJsons);
    }

	/**
	 * <b>删除-批量</b>
	 * <p>不调用 {@link #paramFormat(JSONObject)} 方法</p>
	 * <p>一组条件对应一条数据，并且每组条件都采用相同的key</p>
	 *
	 * @param tableName		表名
	 * @param paramJsons	条件数组
	 */
	@Transactional
	public void deleteBatchNotParamFormat(String tableName, JSONObject[] paramJsons) {
		// 1. 获得SQL
		String sql = deleteSqlBuild(tableName, paramJsons[0]);

		// 2. 执行
		dataEncrypt(tableName, paramJsons);
		int[] updateRowsNumberArray = getNamedParameterJdbcTemplate().batchUpdate(sql, paramJsons);

		// 3. 确认影响行数
		for (int updateRowsNumber : updateRowsNumberArray) {
			if (updateRowsNumber > 1) {
				throw new DbException(ResultPrompt.DELETE_BATCH_ERROR);
			}
		}
	}

	/**
	 * <b>删除-批量</b>
	 * <p>同 {@linkplain NamedParameterJdbcTemplate#batchUpdate(String, Map[])}</p>
	 * <p>示例：<code>DELETE FROM table WHERE id = :id</code></p>
	 *
	 * @param sql        要执行的删除SQL
	 * @param paramJsons 删除所用到的条件数组
	 * @return 一个数组，其中包含受批处理中每个更新影响的行数
	 */
	@Transactional
	public int[] deleteBatch2(String sql, JSONObject[] paramJsons) {
		for (JSONObject paramJson : paramJsons) {
			paramFormat(paramJson);
		}

		return deleteBatchNotParamFormat2(sql, paramJsons);
	}

	/**
	 * <b>删除-批量</b>
	 * <p>不调用 {@link #paramFormat(JSONObject)} 方法</p>
	 * <p>同 {@linkplain NamedParameterJdbcTemplate#batchUpdate(String, Map[])}</p>
	 * <p>示例：<code>DELETE FROM table WHERE id = :id</code></p>
	 *
	 * @param sql        要执行的删除SQL
	 * @param paramJsons 删除所用到的条件数组
	 * @return 一个数组，其中包含受批处理中每个更新影响的行数
	 */
	@Transactional
	public int[] deleteBatchNotParamFormat2(String sql, JSONObject[] paramJsons) {
		return getNamedParameterJdbcTemplate().batchUpdate(sql, paramJsons);
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
		return updateSqlBuild(tableName, paramJson, conditions, DbUpdateEnum.NORMAL);
	}

	private void deleteLogicByUk(String tableName, Object uk) {
		// 1. 参数验证
		paramValidate(tableName);
		if (ObjectUtils.isNull(uk)) {
			throw new DbException("删除条件不能为null");
		}

		// 2. 确认数据
		String key = null;
		if (uk instanceof Long) {
			key = DbConstant.PRIMARY_KEY;
		} else if (uk instanceof String) {
			key = getJdbcProperties().getBusinessUk();
		}

		// 3. 获得SQL
		JSONObject paramJson = new JSONObject();
		paramJson.put(key, uk);
		String sql = deleteLogicSqlBuild(tableName, paramJson);

		// 4. 执行删除
		int updateRowsNumber = getNamedParameterJdbcTemplate().update(sql, paramJson);

		// 5. 确认影响的数据条数
		if (updateRowsNumber != 1) {
			throw new DbException(ResultPrompt.DELETE_ERROR);
		}
	}

	/**
	 * 删除-逻辑的
	 * <p>数据非真实删除，而是更改 {@value DbConstant#FIELD_DEFINITION_DELETE_TIME} 字段值为时间戳，代表数据已删除
	 * <p><code style="color:red">依赖于接口传入 {@value DbConstant#PRIMARY_KEY} 参数时慎用此方法</code>，避免有序主键被遍历风险，造成数据越权行为。推荐使用 {@link #deleteLogicByBusinessUk(String, String)}</p>
     * 
     * @param tableName	表名
     * @param id     	主键id
     */
	@Transactional
    public void deleteLogic(String tableName, Long id) {
		deleteLogicByUk(tableName, id);
    }

	/**
	 * 删除-逻辑的
	 * <p>数据非真实删除，而是更改 {@value DbConstant#FIELD_DEFINITION_DELETE_TIME} 字段值为时间戳，代表数据已删除
	 *
	 * @param tableName    表名
	 * @param businessUkValue 业务键的唯一值
	 */
	@Transactional
	public void deleteLogicByBusinessUk(String tableName, String businessUkValue) {
		deleteLogicByUk(tableName, businessUkValue);
	}

	/**
	 * 删除-逻辑的
	 * <p>数据非真实删除，而是更改 {@value DbConstant#FIELD_DEFINITION_DELETE_TIME} 字段值为时间戳，代表数据已删除
     * 
     * @param tableName		表名
     * @param paramJson		条件
     * @return 删除所影响的行数
     */
	@Transactional
	public long deleteLogic(String tableName, JSONObject paramJson) {
		paramFormat(paramJson);
		dataEncrypt(tableName, paramJson);
		String sql = deleteLogicSqlBuild(tableName, paramJson);
		return getNamedParameterJdbcTemplate().update(sql, paramJson);
	}
	
	/**
	 * 删除-批量-逻辑的
	 * <p>数据非真实删除，而是更改 {@value DbConstant#FIELD_DEFINITION_DELETE_TIME} 字段值为时间戳，代表数据已删除
	 * <p>一组条件对应一条数据，并且每组条件都采用相同的key
     * 
     * @param tableName		表名
     * @param paramJsons	条件数组
     */
	@Transactional
    public void deleteBatchLogic(String tableName, JSONObject[] paramJsons) {
		for (JSONObject paramJson : paramJsons) {
			paramFormat(paramJson);
		}

		deleteBatchLogicNotParamFormat(tableName, paramJsons);
    }

	/**
	 * 删除-批量-逻辑的（不调用 {@link #paramFormat(JSONObject)} 方法）
	 * <p>数据非真实删除，而是更改 {@value DbConstant#FIELD_DEFINITION_DELETE_TIME} 字段值为时间戳，代表数据已删除
	 * <p>一组条件对应一条数据，并且每组条件都采用相同的key
	 *
	 * @param tableName		表名
	 * @param paramJsons	条件数组
	 */
	@Transactional
	public void deleteBatchLogicNotParamFormat(String tableName, JSONObject[] paramJsons) {
		// 1. 获得SQL
		String sql = deleteLogicSqlBuild(tableName, paramJsons[0]);

		// 2. 执行
		dataEncrypt(tableName, paramJsons);
		int[] updateRowsNumberArray = getNamedParameterJdbcTemplate().batchUpdate(sql, paramJsons);

		// 3. 确认影响行数
		for (int updateRowsNumber : updateRowsNumberArray) {
			if (updateRowsNumber > 1) {
				throw new DbException(ResultPrompt.DELETE_BATCH_ERROR);
			}
		}
	}

}
