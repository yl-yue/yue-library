package ai.yue.library.data.jdbc.client;

import ai.yue.library.base.exception.DbException;
import ai.yue.library.base.util.MapUtils;
import ai.yue.library.base.util.ObjectUtils;
import ai.yue.library.base.view.ResultPrompt;
import ai.yue.library.data.jdbc.config.properties.JdbcProperties;
import ai.yue.library.data.jdbc.constant.CrudEnum;
import ai.yue.library.data.jdbc.constant.DbConstant;
import ai.yue.library.data.jdbc.constant.DbUpdateEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.Nullable;
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
		sql.append(paramToWhereSqlNotDeleteWhere(paramJson));

		// 3. 返回SQL
		return sql.toString();
	}

	private void deleteByPk(String tableName, Object pkValue) {
		// 1. 参数验证
		paramValidate(tableName);
		if (ObjectUtils.isNull(pkValue)) {
			throw new DbException("删除条件不能为null");
		}

		// 2. 确认数据
		JSONObject data = null;
		String pkName = null;
		try {
			if (pkValue instanceof Long) {
				data = getById(tableName, (Long) pkValue);
				pkName = DbConstant.FIELD_DEFINITION_ID;
			} else if (pkValue instanceof String) {
				data = getByUuid(tableName, (String) pkValue);
				pkName = getJdbcProperties().getFieldDefinitionUuid();
			}
		} catch (Exception e) {
			data = null;
		}
		if (data == null || data.isEmpty()) {
			throw new DbException("执行单行删除命令失败，数据结构异常，可能原因是：数据不存在或存在多条数据", true);
		}

		// 3. 获得SQL
		JSONObject paramJson = new JSONObject();
		paramJson.put(pkName, pkValue);
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
		return getNamedParameterJdbcTemplate().update(sql, paramJson);
	}

	/**
	 * 删除-By有序主键
	 * <p>数据库字段名：{@value DbConstant#FIELD_DEFINITION_ID}</p>
	 * <p>数据库字段值：大整数；推荐单表时数据库自增、分布式时雪花自增</p>
	 * <p>数据删除前会先进行条数确认</p>
	 * <p><code style="color:red">依赖于接口传入 {@value DbConstant#FIELD_DEFINITION_ID} 参数时慎用此方法</code>，避免有序主键被遍历风险，造成数据越权行为。推荐使用 {@link #deleteByUuid(String, String)}</p>
     * 
     * @param tableName	表名
     * @param id     	有序主键
     */
	@Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public void deleteById(String tableName, Long id) {
		deleteByPk(tableName, id);
    }

	/**
	 * 删除-By无序主键
	 * <p>数据库字段名：{@value DbConstant#FIELD_DEFINITION_UUID}，可在 application.yml 或 {@linkplain JdbcProperties} Bean 中重新自定义配置字段名</p>
	 * <p>数据库字段值：字符串；推荐UUID5、无符号、32位</p>
	 * <p>数据删除前会先进行条数确认</p>
	 *
	 * @param tableName 表名
	 * @param uuid      无序主键
	 */
	@Transactional(rollbackFor = {RuntimeException.class, Error.class})
	public void deleteByUuid(String tableName, String uuid) {
		deleteByPk(tableName, uuid);
	}

	/**
	 * 删除-批量
	 * <p>一组条件对应一条数据，并且每组条件都采用相同的key
     * 
     * @param tableName		表名
     * @param paramJsons	条件数组
     */
	@Transactional(rollbackFor = {RuntimeException.class, Error.class})
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
	@Transactional(rollbackFor = {RuntimeException.class, Error.class})
	public void deleteBatchNotParamFormat(String tableName, JSONObject[] paramJsons) {
		// 1. 获得SQL
		String sql = deleteSqlBuild(tableName, paramJsons[0]);

		// 2. 数据加密
		dataEncrypt(tableName, paramJsons);

		// 3. 执行
		int[] updateRowsNumberArray = getNamedParameterJdbcTemplate().batchUpdate(sql, paramJsons);

		// 4. 确认影响行数
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
	@Transactional(rollbackFor = {RuntimeException.class, Error.class})
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
	@Transactional(rollbackFor = {RuntimeException.class, Error.class})
	public int[] deleteBatchNotParamFormat2(String sql, JSONObject[] paramJsons) {
		// 1. 数据加密
		dataEncryptExtractTable(sql, paramJsons);

		// 2. 执行
		return getNamedParameterJdbcTemplate().batchUpdate(sql, paramJsons);
	}

	// Delete Logic
	
	private String deleteLogicSqlBuild(String tableName, JSONObject paramJson, @Nullable JSONObject auditUpdateJson) {
		// 1. 参数验证
		paramValidate(tableName, paramJson);
		
		// 2. 获得更新条件（逻辑删除条件）
		String[] conditions = new String[paramJson.size()];
		conditions = paramJson.keySet().toArray(conditions);

		// 3. 处理更新所用到的参数（包括更新条件与被更新的值）
		if (MapUtils.isEmpty(auditUpdateJson)) {
			// 未开启审计
			paramJson.put(getJdbcProperties().getFieldDefinitionDeleteTime(), System.currentTimeMillis());
		} else {
			// 逻辑删除审计
			paramJson.putAll(auditUpdateJson);
		}

		// 4. 返回生成SQL
		return updateSqlBuild(tableName, paramJson, conditions, DbUpdateEnum.NORMAL);
	}

	private void deleteLogicByUk(String tableName, Object pkValue) {
		// 1. 参数验证
		paramValidate(tableName);
		if (ObjectUtils.isNull(pkValue)) {
			throw new DbException("删除条件不能为null");
		}

		// 2. 确认数据
		String pkName = null;
		if (pkValue instanceof Long) {
			pkName = DbConstant.FIELD_DEFINITION_ID;
		} else if (pkValue instanceof String) {
			pkName = getJdbcProperties().getFieldDefinitionUuid();
		}

		// 3. 获得SQL
		JSONObject paramJson = new JSONObject();
		paramJson.put(pkName, pkValue);
		JSONObject auditParam = new JSONObject();
		dataAudit(tableName, CrudEnum.D, auditParam);
		String sql = deleteLogicSqlBuild(tableName, paramJson, auditParam);

		// 4. 执行删除
		int updateRowsNumber = getNamedParameterJdbcTemplate().update(sql, paramJson);

		// 5. 确认影响的数据条数
		if (updateRowsNumber != 1) {
			throw new DbException(ResultPrompt.DELETE_ERROR);
		}
	}

	/**
	 * 逻辑删除
	 * <p>数据非真实删除，而是更改 {@link JdbcProperties().getFieldDefinitionDeleteTime()} 字段值为时间戳，代表数据已删除
	 *
	 * @param tableName 表名
	 * @param paramJson 条件
	 * @return 删除所影响的行数
	 */
	@Transactional(rollbackFor = {RuntimeException.class, Error.class})
	public long deleteLogic(String tableName, JSONObject paramJson) {
		paramFormat(paramJson);
		dataEncrypt(tableName, paramJson);
		JSONObject auditParam = new JSONObject();
		dataAudit(tableName, CrudEnum.D, auditParam);
		String sql = deleteLogicSqlBuild(tableName, paramJson, auditParam);
		return getNamedParameterJdbcTemplate().update(sql, paramJson);
	}

	/**
	 * 逻辑删除-By有序主键
	 * <p>数据非真实删除，而是更改 {@link JdbcProperties().getFieldDefinitionDeleteTime()} 字段值为时间戳，代表数据已删除</p>
	 * <p><code style="color:red">依赖于接口传入 {@value DbConstant#FIELD_DEFINITION_ID} 参数时慎用此方法</code>，避免有序主键被遍历风险，造成数据越权行为。推荐使用 {@link #deleteLogicByUuid(String, String)}</p>
     * 
     * @param tableName	表名
     * @param id     	主键id
     */
	@Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public void deleteLogicById(String tableName, Long id) {
		deleteLogicByUk(tableName, id);
    }

	/**
	 * 逻辑删除-By无序主键
	 * <p>数据非真实删除，而是更改 {@link JdbcProperties().getFieldDefinitionDeleteTime()} 字段值为时间戳，代表数据已删除
	 *
	 * @param tableName 表名
	 * @param uuid      无序主键
	 */
	@Transactional(rollbackFor = {RuntimeException.class, Error.class})
	public void deleteLogicByUuid(String tableName, String uuid) {
		deleteLogicByUk(tableName, uuid);
	}

	/**
	 * 逻辑删除-批量
	 * <p>数据非真实删除，而是更改 {@link JdbcProperties().getFieldDefinitionDeleteTime()} 字段值为时间戳，代表数据已删除
	 * <p>一组条件对应一条数据，并且每组条件都采用相同的key
     * 
     * @param tableName		表名
     * @param paramJsons	条件数组
     */
	@Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public void deleteBatchLogic(String tableName, JSONObject[] paramJsons) {
		for (JSONObject paramJson : paramJsons) {
			paramFormat(paramJson);
		}

		deleteBatchLogicNotParamFormat(tableName, paramJsons);
    }

	/**
	 * 逻辑删除-批量（不调用 {@link #paramFormat(JSONObject)} 方法）
	 * <p>数据非真实删除，而是更改 {@link JdbcProperties().getFieldDefinitionDeleteTime()} 字段值为时间戳，代表数据已删除
	 * <p>一组条件对应一条数据，并且每组条件都采用相同的key
	 *
	 * @param tableName		表名
	 * @param paramJsons	条件数组
	 */
	@Transactional(rollbackFor = {RuntimeException.class, Error.class})
	public void deleteBatchLogicNotParamFormat(String tableName, JSONObject[] paramJsons) {
		// 1. 处理数据审计更新值
		JSONObject[] auditUpdateJsons = new JSONObject[paramJsons.length];
		for (int i = 0; i < auditUpdateJsons.length; i++) {
			auditUpdateJsons[i] = new JSONObject();
		}
		dataAudit(tableName, CrudEnum.D, auditUpdateJsons);

		// 2. 获得逻辑删除执行SQL
		String sql = deleteLogicSqlBuild(tableName, paramJsons[0], auditUpdateJsons[0]);

		// 3. 处理剩余的更新参数
		if (MapUtils.isNotEmpty(auditUpdateJsons[0])) {
			for (int i = 1; i < paramJsons.length; i++) {
				paramJsons[i].putAll(auditUpdateJsons[i]);
			}
		}

		// 4. 处理数据加密
		dataEncrypt(tableName, paramJsons);

		// 5. 执行
		int[] updateRowsNumberArray = getNamedParameterJdbcTemplate().batchUpdate(sql, paramJsons);

		// 6. 确认影响行数
		for (int updateRowsNumber : updateRowsNumberArray) {
			if (updateRowsNumber > 1) {
				throw new DbException(ResultPrompt.DELETE_BATCH_ERROR);
			}
		}
	}

}
