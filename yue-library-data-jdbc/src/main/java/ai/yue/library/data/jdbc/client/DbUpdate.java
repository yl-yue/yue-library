package ai.yue.library.data.jdbc.client;

import ai.yue.library.base.exception.DbException;
import ai.yue.library.base.util.ListUtils;
import ai.yue.library.base.util.MapUtils;
import ai.yue.library.base.util.StringUtils;
import ai.yue.library.data.jdbc.config.properties.JdbcProperties;
import ai.yue.library.data.jdbc.constant.CrudEnum;
import ai.yue.library.data.jdbc.constant.DbConstant;
import ai.yue.library.data.jdbc.constant.DbExpectedEnum;
import ai.yue.library.data.jdbc.constant.DbUpdateEnum;
import ai.yue.library.data.jdbc.provider.FillDataProvider;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <h2>SQL优化型数据库操作</h2>
 * Created by sunJinChuan on 2016/6/6
 * @since 0.0.1
 */
class DbUpdate extends DbQuery {
	
	// Spring Update

	/**
	 * 更新或插入数据，主键默认为id时使用。
	 * <p>注意：此方法不支持数据审计、数据填充、数据脱敏、逻辑删除</p>
	 *
	 * @param sql         更新或插入SQL
	 * @param paramSource 更新所用到的参数：{@linkplain MapSqlParameterSource}，{@linkplain BeanPropertySqlParameterSource}
	 * @return 自动生成的键(可能由JDBC insert语句返回)或更新的主键id值。
	 */
	@Transactional
	public Long update(String sql, SqlParameterSource paramSource) {
		GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
		long updateRowsNumber = getNamedParameterJdbcTemplate().update(sql, paramSource, generatedKeyHolder);
		int expectedValue = 1;
		updateAndExpectedEqual(updateRowsNumber, expectedValue);
		return generatedKeyHolder.getKey().longValue();
	}
	
	/**
	 * 更新数据<br>
     * 同 {@linkplain NamedParameterJdbcTemplate#update(String, Map)}<br>
	 * <p>注意：此方法暂不支持数据审计、数据填充、逻辑删除</p>
     *
     * @param sql			要执行的更新SQL
     * @param paramJson		更新所用到的参数
	 * @return 受影响的行数
     */
	@Transactional
	public long update(String sql, JSONObject paramJson) {
		// 参数美化
		paramFormat(paramJson);

		// 数据脱敏
		dataEncryptExtractTable(sql, paramJson);

		// 执行
		return getNamedParameterJdbcTemplate().update(sql, paramJson);
	}

	/**
	 * 更新数据<br>
	 * <p>注意：此方法暂不支持数据审计、数据填充、逻辑删除</p>
	 *
	 * <ul>
	 *     <li>对 {@linkplain NamedParameterJdbcTemplate#update(String, Map)} 方法的增强实现</li>
	 *     <li>将会对更新所影响的行数进行预期判断，若结果不符合预期值：<b>expectedValue</b>，那么此处便会抛出一个 {@linkplain DbException}</li>
	 * </ul>
	 *
	 * @param sql            要执行的更新SQL
	 * @param paramJson      更新所用到的参数
	 * @param expectedValue  更新所影响的行数预期值
	 * @param dbExpectedEnum 预期值确认方式
	 */
	@Transactional
	public void update(String sql, JSONObject paramJson, int expectedValue, DbExpectedEnum dbExpectedEnum) {
		long updateRowsNumber = update(sql, paramJson);
		if (DbExpectedEnum.EQ == dbExpectedEnum) {
			updateAndExpectedEqual(updateRowsNumber, expectedValue);
		} else if (DbExpectedEnum.GE == dbExpectedEnum) {
			updateAndExpectedGreaterThanEqual(updateRowsNumber, expectedValue);
		}
	}

	/**
	 * 对多组参数进行批量更新处理<br>
	 * 同 {@linkplain NamedParameterJdbcTemplate#batchUpdate(String, Map[])}<br>
	 * <p>注意：此方法暂不支持数据审计、数据填充、逻辑删除</p>
	 *
	 * @param sql        要执行的更新SQL
	 * @param paramJsons 更新所用到的参数数组
	 * @return 一个数组，其中包含受批处理中每个更新影响的行数
	 */
	@Transactional
	public int[] updateBatch(String sql, JSONObject[] paramJsons) {
		for (JSONObject paramJson : paramJsons) {
			paramFormat(paramJson);
		}

		return updateBatchNotParamFormat(sql, paramJsons);
	}

	/**
	 * 对多组参数进行批量更新处理<br>
	 * 同 {@linkplain NamedParameterJdbcTemplate#batchUpdate(String, Map[])}<br>
	 * <p>注意：此方法暂不支持数据审计、数据填充、逻辑删除</p>
	 *
	 * @param sql        要执行的更新SQL
	 * @param paramJsons 更新所用到的参数数组（不调用 {@link #paramFormat(JSONObject)} 方法）
	 * @return 一个数组，其中包含受批处理中每个更新影响的行数
	 */
	@Transactional(rollbackFor = {RuntimeException.class, Error.class})
	public int[] updateBatchNotParamFormat(String sql, JSONObject[] paramJsons) {
		// 数据脱敏
		dataEncryptExtractTable(sql, paramJsons);

		// 执行更新
		return getNamedParameterJdbcTemplate().batchUpdate(sql, paramJsons);
	}

	// Update

	/**
	 * 构建绝对条件更新优化SQL
	 *
	 * @param tableName    表名
	 * @param paramJson    更新所用到的参数
	 * @param conditions   作为更新条件的参数名，对应paramJson内的key（注意：作为条件的参数，将不会用于字段值的更新）
	 * @param dbUpdateEnum 更新类型 {@linkplain DbUpdateEnum}
	 * @return 绝对条件更新优化SQL
	 */
    protected String updateSqlBuild(String tableName, JSONObject paramJson, String[] conditions, DbUpdateEnum dbUpdateEnum) {
		paramValidate(tableName, paramJson, conditions);
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE ");
		sql.append(dialect.getWrapper().wrap(tableName));
		sql.append(" SET ");

		Set<String> keys = paramJson.keySet();
		Iterator<String> it = keys.iterator();
		while (it.hasNext()) {
			String key = it.next();
			// 排除更新条件
			if (!ArrayUtil.contains(conditions, key)) {
				sql.append(dialect.getWrapper().wrap(key));
				sql.append(" = ");
				if (dbUpdateEnum == DbUpdateEnum.INCREMENT) {// 递增更新
					sql.append(dialect.getWrapper().wrap(key));
					sql.append(" + :");
				} else if (dbUpdateEnum == DbUpdateEnum.DECR // 递减更新
						|| dbUpdateEnum == DbUpdateEnum.DECR_UNSIGNED) {// 递减-无符号更新
					sql.append(dialect.getWrapper().wrap(key));
					sql.append(" - :");
				} else {// 正常更新
					sql.append(":");
				}
				sql.append(key);
				sql.append(", ");
			}
		}
		sql = new StringBuffer(StringUtils.deleteLastEqualString(sql, ", "));
		String whereSql = paramToWhereSql(paramJson, conditions);
		sql.append(whereSql);

		// 递减-无符号更新（避免被减成负数）
		if (dbUpdateEnum == DbUpdateEnum.DECR_UNSIGNED) {
			List<String> updateKeys = MapUtils.keyList(paramJson);
			for (String key : updateKeys) {
				// 排除更新条件
				if (!ArrayUtil.contains(conditions, key)) {
					sql.append(" AND ");
					sql.append(dialect.getWrapper().wrap(key));
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
	 * <code>paramNumber1 &gt; 0</code><br>
	 * <code>AND ...</code>
	 *
	 * @param tableName  表名
	 * @param paramJson  更新所用到的参数（where条件参数不会用于set值的更新）
	 * @param conditions 作为更新条件的参数名，对应paramJson内的key（注意：作为条件的参数，将不会用于字段值的更新）
	 * @return 受影响的行数
	 */
	@Transactional
    public Long update(String tableName, JSONObject paramJson, String[] conditions) {
		paramFormat(paramJson);
		dataEncrypt(tableName, paramJson);
		dataAudit(tableName, CrudEnum.U, paramJson);
		paramJson.putAll(FillDataProvider.getUpdateParamJson(getJdbcProperties(), tableName));
		String sql = updateSqlBuild(tableName, paramJson, conditions, DbUpdateEnum.NORMAL);
        return (long) getNamedParameterJdbcTemplate().update(sql, paramJson);
    }

	/**
	 * <b>绝对</b>条件更新优化SQL<br><br>
	 *
	 * @param tableName    表名
	 * @param paramJson    更新所用到的参数
	 * @param conditions   作为更新条件的参数名，对应paramJson内的key（注意：作为条件的参数，将不会用于字段值的更新）
	 * @param dbUpdateEnum 更新类型 {@linkplain DbUpdateEnum}
	 * @return 受影响的行数
	 */
	@Transactional
    public Long update(String tableName, JSONObject paramJson, String[] conditions, DbUpdateEnum dbUpdateEnum) {
		paramFormat(paramJson);
		dataEncrypt(tableName, paramJson);
		dataAudit(tableName, CrudEnum.U, paramJson);
		paramJson.putAll(FillDataProvider.getUpdateParamJson(getJdbcProperties(), tableName));
		String sql = updateSqlBuild(tableName, paramJson, conditions, dbUpdateEnum);
        return (long) getNamedParameterJdbcTemplate().update(sql, paramJson);
	}

	/**
	 * <b>绝对</b>条件更新优化SQL<br><br>
	 *
	 * @param tableName      表名
	 * @param paramJson      更新所用到的参数
	 * @param conditions     作为更新条件的参数名，对应paramJson内的key（注意：作为条件的参数，将不会用于字段值的更新）
	 * @param dbUpdateEnum   更新类型 {@linkplain DbUpdateEnum}
	 * @param expectedValue  更新所影响的行数预期值
	 * @param dbExpectedEnum 预期值确认方式
	 */
	@Transactional
    public void update(String tableName, JSONObject paramJson, String[] conditions, DbUpdateEnum dbUpdateEnum
    		, int expectedValue, DbExpectedEnum dbExpectedEnum) {
		paramFormat(paramJson);
		dataEncrypt(tableName, paramJson);
		dataAudit(tableName, CrudEnum.U, paramJson);
		paramJson.putAll(FillDataProvider.getUpdateParamJson(getJdbcProperties(), tableName));
		String sql = updateSqlBuild(tableName, paramJson, conditions, dbUpdateEnum);
		int updateRowsNumber = getNamedParameterJdbcTemplate().update(sql, paramJson);
		if (DbExpectedEnum.EQ == dbExpectedEnum) {
			updateAndExpectedEqual(updateRowsNumber, expectedValue);
		} else if (DbExpectedEnum.GE == dbExpectedEnum) {
			updateAndExpectedGreaterThanEqual(updateRowsNumber, expectedValue);
		}
	}

	/**
	 * 更新-By有序主键
	 * <p>有序主键数据库字段名：{@value DbConstant#FIELD_DEFINITION_ID}</p>
	 * <p>有序主键数据库字段值：大整数；推荐单表时数据库自增、分布式时雪花自增</p>
	 * <p>使用有序主键作为更新条件进行更新数据</p>
	 * <p><code style="color:red">依赖于接口传入 {@value DbConstant#FIELD_DEFINITION_ID} 参数时慎用此方法</code>，避免有序主键被遍历风险，造成数据越权行为。推荐使用 {@link #updateByUuid(String, JSONObject)}</p>
	 *
	 * @param tableName 表名
	 * @param paramJson 更新所用到的参数（包含有序主键字段）
	 */
	@Transactional
    public void updateById(String tableName, JSONObject paramJson) {
		updateById(tableName, paramJson, DbUpdateEnum.NORMAL);
    }

	/**
	 * 更新-By有序主键
	 * <p>有序主键数据库字段名：{@value DbConstant#FIELD_DEFINITION_ID}</p>
	 * <p>有序主键数据库字段值：大整数；推荐单表时数据库自增、分布式时雪花自增</p>
	 * <p>使用有序主键作为更新条件进行更新数据</p>
	 * <p><code style="color:red">依赖于接口传入 {@value DbConstant#FIELD_DEFINITION_ID} 参数时慎用此方法</code>，避免有序主键被遍历风险，造成数据越权行为。推荐使用 {@link #updateByUuid(String, JSONObject, DbUpdateEnum)}</p>
	 *
	 * @param tableName    表名
	 * @param paramJson    更新所用到的参数（包含有序主键字段）
	 * @param dbUpdateEnum 更新类型 {@linkplain DbUpdateEnum}
	 */
	@Transactional
    public void updateById(String tableName, JSONObject paramJson, DbUpdateEnum dbUpdateEnum) {
		String[] conditions = { DbConstant.FIELD_DEFINITION_ID };
		paramFormat(paramJson);
		dataEncrypt(tableName, paramJson);
		dataAudit(tableName, CrudEnum.U, paramJson);
		paramJson.putAll(FillDataProvider.getUpdateParamJson(getJdbcProperties(), tableName));
		String sql = updateSqlBuild(tableName, paramJson, conditions, dbUpdateEnum);
		int updateRowsNumber = getNamedParameterJdbcTemplate().update(sql, paramJson);
        int expectedValue = 1;
		updateAndExpectedEqual(updateRowsNumber, expectedValue);
    }

	/**
	 * 批量更新-By有序主键
	 * <p>有序主键数据库字段名：{@value DbConstant#FIELD_DEFINITION_ID}</p>
	 * <p>有序主键数据库字段值：大整数；推荐单表时数据库自增、分布式时雪花自增</p>
	 * <p>使用有序主键作为更新条件进行批量更新数据</p>
	 * <p><code style="color:red">依赖于接口传入 {@value DbConstant#FIELD_DEFINITION_ID} 参数时慎用此方法</code>，避免有序主键被遍历风险，造成数据越权行为。推荐使用 {@link #updateByUuid(String, JSONObject[], DbUpdateEnum)}</p>
	 *
	 * @param tableName    表名
	 * @param paramJsons   更新所用到的参数数组（包含有序主键字段）
	 * @param dbUpdateEnum 更新类型 {@linkplain DbUpdateEnum}
	 */
	@Transactional
    public void updateById(String tableName, JSONObject[] paramJsons, DbUpdateEnum dbUpdateEnum) {
		for (JSONObject paramJson : paramJsons) {
			paramFormat(paramJson);
		}

		updateByIdNotParamFormat(tableName, paramJsons, dbUpdateEnum);
    }

	/**
	 * 批量更新-By有序主键（不调用 {@link #paramFormat(JSONObject)} 方法）
	 * <p>有序主键数据库字段名：{@value DbConstant#FIELD_DEFINITION_ID}</p>
	 * <p>有序主键数据库字段值：大整数；推荐单表时数据库自增、分布式时雪花自增</p>
	 * <p>使用有序主键作为更新条件进行批量更新数据</p>
	 * <p><code style="color:red">依赖于接口传入 {@value DbConstant#FIELD_DEFINITION_ID} 参数时慎用此方法</code>，避免有序主键被遍历风险，造成数据越权行为。推荐使用 {@link #updateByUuidNotParamFormat(String, JSONObject[], DbUpdateEnum)}</p>
	 * 
	 * @param tableName    	表名
	 * @param paramJsons	更新所用到的参数数组（包含有序主键字段）
	 * @param dbUpdateEnum	更新类型 {@linkplain DbUpdateEnum}
	 */
	@Transactional
	public void updateByIdNotParamFormat(String tableName, JSONObject[] paramJsons, DbUpdateEnum dbUpdateEnum) {
		String[] conditions = { DbConstant.FIELD_DEFINITION_ID };
		dataEncrypt(tableName, paramJsons);
		dataAudit(tableName, CrudEnum.U, paramJsons);
		for (JSONObject paramJson : paramJsons) {
			paramJson.putAll(FillDataProvider.getUpdateParamJson(getJdbcProperties(), tableName));
		}
		String sql = updateSqlBuild(tableName, paramJsons[0], conditions, dbUpdateEnum);
		int[] updateRowsNumberArray = getNamedParameterJdbcTemplate().batchUpdate(sql, paramJsons);
		int expectedValue = 1;
		updateBatchAndExpectedEqual(updateRowsNumberArray, expectedValue);
	}

	/**
	 * 更新-By无序主键
	 * <p>数据库字段名：{@value DbConstant#FIELD_DEFINITION_UUID}，可在 application.yml 或 {@linkplain JdbcProperties} Bean 中重新自定义配置字段名</p>
	 * <p>数据库字段值：字符串；推荐UUID5、无符号、32位</p>
	 * <p>使用无序主键作为更新条件进行更新数据</p>
	 *
	 * @param tableName 表名
	 * @param paramJson 更新所用到的参数（包含无序主键字段）
	 */
	@Transactional
    public void updateByUuid(String tableName, JSONObject paramJson) {
		updateByUuid(tableName, paramJson, DbUpdateEnum.NORMAL);
    }
	
	/**
	 * 更新-By无序主键
	 * <p>数据库字段名：{@value DbConstant#FIELD_DEFINITION_UUID}，可在 application.yml 或 {@linkplain JdbcProperties} Bean 中重新自定义配置字段名</p>
	 * <p>数据库字段值：字符串；推荐UUID5、无符号、32位</p>
	 * <p>使用无序主键作为更新条件进行更新数据</p>
	 * 
     * @param tableName		表名
     * @param paramJson		更新所用到的参数（包含无序主键字段）
     * @param dbUpdateEnum	更新类型 {@linkplain DbUpdateEnum}
     */
	@Transactional
    public void updateByUuid(String tableName, JSONObject paramJson, DbUpdateEnum dbUpdateEnum) {
		String[] conditions = { getJdbcProperties().getFieldDefinitionUuid() };
		paramFormat(paramJson);
		dataEncrypt(tableName, paramJson);
		dataAudit(tableName, CrudEnum.U, paramJson);
		paramJson.putAll(FillDataProvider.getUpdateParamJson(getJdbcProperties(), tableName));
		String sql = updateSqlBuild(tableName, paramJson, conditions, dbUpdateEnum);
		int updateRowsNumber = getNamedParameterJdbcTemplate().update(sql, paramJson);
        int expectedValue = 1;
		updateAndExpectedEqual(updateRowsNumber, expectedValue);
    }
	
	/**
	 * 批量更新-By无序主键
	 * <p>数据库字段名：{@value DbConstant#FIELD_DEFINITION_UUID}，可在 application.yml 或 {@linkplain JdbcProperties} Bean 中重新自定义配置字段名</p>
	 * <p>数据库字段值：字符串；推荐UUID5、无符号、32位</p>
	 * <p>使用无序主键作为更新条件进行批量更新数据</p>
	 * 
     * @param tableName    	表名
     * @param paramJsons	更新所用到的参数数组（包含无序主键字段）
     * @param dbUpdateEnum	更新类型 {@linkplain DbUpdateEnum}
     */
	@Transactional
    public void updateByUuid(String tableName, JSONObject[] paramJsons, DbUpdateEnum dbUpdateEnum) {
		for (JSONObject paramJson : paramJsons) {
			paramFormat(paramJson);
		}

		updateByUuidNotParamFormat(tableName, paramJsons, dbUpdateEnum);
    }

	/**
	 * 批量更新-By无序主键（不调用 {@link #paramFormat(JSONObject)} 方法）
	 * <p>数据库字段名：{@value DbConstant#FIELD_DEFINITION_UUID}，可在 application.yml 或 {@linkplain JdbcProperties} Bean 中重新自定义配置字段名</p>
	 * <p>数据库字段值：字符串；推荐UUID5、无符号、32位</p>
	 * <p>使用无序主键作为更新条件进行批量更新数据</p>
	 *
	 * @param tableName    	表名
	 * @param paramJsons	更新所用到的参数数组（包含无序主键字段）
	 * @param dbUpdateEnum	更新类型 {@linkplain DbUpdateEnum}
	 */
	@Transactional
	public void updateByUuidNotParamFormat(String tableName, JSONObject[] paramJsons, DbUpdateEnum dbUpdateEnum) {
		String[] conditions = { getJdbcProperties().getFieldDefinitionUuid() };
		dataEncrypt(tableName, paramJsons);
		dataAudit(tableName, CrudEnum.U, paramJsons);
		for (JSONObject paramJson : paramJsons) {
			paramJson.putAll(FillDataProvider.getUpdateParamJson(getJdbcProperties(), tableName));
		}
		String sql = updateSqlBuild(tableName, paramJsons[0], conditions, dbUpdateEnum);
		int[] updateRowsNumberArray = getNamedParameterJdbcTemplate().batchUpdate(sql, paramJsons);
		int expectedValue = 1;
		updateBatchAndExpectedEqual(updateRowsNumberArray, expectedValue);
	}

	/**
	 * <b>更新-排序</b><br>
	 * <i>使用限制：见</i> {@linkplain DbInsert#insertWithSortIdxAutoIncrement(String, JSONObject, String...)}
	 *
	 * @param tableName  表名
	 * @param id         有序主键
	 * @param move       sort_idx移动位数（值不可等于零，正整数表示：向后移动几位，负整数表示：向前移动几位）
	 * @param uniqueKeys 同sort_idx字段组合的唯一约束keys（表中不建议建立sort_idx字段的唯一约束，但可以建立普通索引，以便于提高查询性能），<b>可选参数</b>
	 */
	@Transactional
	public void updateSort(String tableName, Long id, Integer move, @Nullable String... uniqueKeys) {
		// 1. 参数验证
		paramValidate(tableName);
		if (move == 0) {
			throw new DbException("move <= 0");
		}
		
		// 2. 获得当前排序索引
		JSONObject sortJson = getById(tableName, id);
		long sortIdx = sortJson.getLongValue(getJdbcProperties().getFieldDefinitionSortIdx());
		long updateSortIdx = sortIdx + move;
		if (updateSortIdx < 1) {
			throw new DbException("排序后的索引值不能小于1");
		}
		
		// 3. 确认排序方式
		List<Long> updateSortList = new ArrayList<>();
		boolean isASC = false;
		if (updateSortIdx > sortIdx) {// 升序
			isASC = true;
			for (long i = updateSortIdx; i > sortIdx; i--) {
				updateSortList.add(i);
			}
		} else {// 降序
			for (long i = updateSortIdx; i < sortIdx; i++) {
				updateSortList.add(i);
			}
		}
		
		// 4. 查询需要跟随移动的数据ID
		JSONObject paramJson = new JSONObject();
		if (ArrayUtil.isNotEmpty(uniqueKeys)) {
			for (String uniqueKey : uniqueKeys) {
				Object uniqueValue = sortJson.get(uniqueKey);
				paramJson.put(uniqueKey, uniqueValue);
			}
		}
		paramJson.put(getJdbcProperties().getFieldDefinitionSortIdx(), updateSortList);
		List<JSONObject> list = list(tableName, paramJson);
		
		// 5. 组装跟随移动参数到参数列表
		JSONArray paramJsonArray = new JSONArray();
		for (JSONObject actionJSON : list) {
			Long actionId = actionJSON.getLong(DbConstant.FIELD_DEFINITION_ID);
			Long actionSort = actionJSON.getLong(getJdbcProperties().getFieldDefinitionSortIdx());
			if (isASC) {
				actionSort -= 1;
			} else {
				actionSort += 1;
			}
			JSONObject actionParamJSON = new JSONObject();
			actionParamJSON.put(DbConstant.FIELD_DEFINITION_ID, actionId);
			actionParamJSON.put(getJdbcProperties().getFieldDefinitionSortIdx(), actionSort);
			paramJsonArray.add(actionParamJSON);
		}
		
		// 6. 添加排序更新参数到参数列表
		JSONObject updateSortParam = new JSONObject();
		updateSortParam.put(DbConstant.FIELD_DEFINITION_ID, id);
		updateSortParam.put(getJdbcProperties().getFieldDefinitionSortIdx(), updateSortIdx);
		paramJsonArray.add(updateSortParam);
		
		// 7. 排序更新
		updateById(tableName, ListUtils.toJsons(paramJsonArray), DbUpdateEnum.NORMAL);
	}
	
	/**
	 * 更新-批量
	 * <p>一组条件对应一条数据，并且每组条件都采用相同的key
	 * 
     * @param tableName    	表名
     * @param paramJsons	更新所用到的参数数组
     * @param conditions	作为更新条件的参数名，对应paramJson内的key（注意：作为条件的参数，将不会用于字段值的更新）
     * @param dbUpdateEnum	更新类型 {@linkplain DbUpdateEnum}
     */
    public void updateBatch(String tableName, JSONObject[] paramJsons, String[] conditions, DbUpdateEnum dbUpdateEnum) {
		for (JSONObject paramJson : paramJsons) {
			paramFormat(paramJson);
		}

		updateBatchNotParamFormat(tableName, paramJsons, conditions, dbUpdateEnum);
	}

	/**
	 * 更新-批量（不调用 {@link #paramFormat(JSONObject)} 方法）
	 * <p>一组条件对应一条数据，并且每组条件都采用相同的key
	 *
	 * @param tableName    	表名
	 * @param paramJsons	更新所用到的参数数组
	 * @param conditions	作为更新条件的参数名，对应paramJson内的key（注意：作为条件的参数，将不会用于字段值的更新）
	 * @param dbUpdateEnum	更新类型 {@linkplain DbUpdateEnum}
	 */
	@Transactional(rollbackFor = {RuntimeException.class, Error.class})
	public void updateBatchNotParamFormat(String tableName, JSONObject[] paramJsons, String[] conditions, DbUpdateEnum dbUpdateEnum) {
		// 1. 处理数据审计更新值
		JSONObject[] auditUpdateJsons = new JSONObject[paramJsons.length];
		for (int i = 0; i < auditUpdateJsons.length; i++) {
			auditUpdateJsons[i] = new JSONObject();
		}
		dataAudit(tableName, CrudEnum.U, auditUpdateJsons);

		// 2. 获得更新执行SQL
		String sql = updateSqlBuild(tableName, paramJsons[0], conditions, dbUpdateEnum);

		// 3. 处理剩余的更新参数
		if (MapUtils.isNotEmpty(auditUpdateJsons[0])) {
			for (int i = 1; i < paramJsons.length; i++) {
				paramJsons[i].putAll(auditUpdateJsons[i]);
			}
		}

		// 4. 处理数据加密
		dataEncrypt(tableName, paramJsons);

		// 5. 执行更新
		int[] updateRowsNumberArray = getNamedParameterJdbcTemplate().batchUpdate(sql, paramJsons);

		// 6. 确认影响行数
		int expectedValue = 1;
		updateBatchAndExpectedEqual(updateRowsNumberArray, expectedValue);
	}

}
