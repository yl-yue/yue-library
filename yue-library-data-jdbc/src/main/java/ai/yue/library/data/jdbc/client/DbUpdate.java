package ai.yue.library.data.jdbc.client;

import ai.yue.library.base.exception.DbException;
import ai.yue.library.base.util.ListUtils;
import ai.yue.library.base.util.MapUtils;
import ai.yue.library.base.util.StringUtils;
import ai.yue.library.data.jdbc.constant.DbConstant;
import ai.yue.library.data.jdbc.constant.DbExpectedEnum;
import ai.yue.library.data.jdbc.constant.DbUpdateEnum;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.var;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <h2>SQL优化型数据库操作</h2>
 * Created by sunJinChuan on 2016/6/6
 * @since 0.0.1
 */
@SuppressWarnings("deprecation")
class DbUpdate extends DbQuery {
	
	// Spring Update

	/**
	 * 更新或插入数据<br>
	 * 同 {@linkplain NamedParameterJdbcTemplate#update(String, SqlParameterSource, KeyHolder)}<br>
	 *
	 * @param sql         要执行的更新SQL
	 * @param paramSource 更新所用到的参数：{@linkplain MapSqlParameterSource}，{@linkplain BeanPropertySqlParameterSource}
	 * @return 自动生成的键(可能由JDBC insert语句返回)或更新的主键id值。
	 */
	@Transactional
	public KeyHolder update(String sql, SqlParameterSource paramSource, KeyHolder generatedKeyHolder) {
		getNamedParameterJdbcTemplate().update(sql, paramSource, generatedKeyHolder);
		return generatedKeyHolder;
	}

	/**
	 * 更新或插入数据，主键默认为id时使用。
	 *
	 * @param sql         更新或插入SQL
	 * @param paramSource 更新所用到的参数：{@linkplain MapSqlParameterSource}，{@linkplain BeanPropertySqlParameterSource}
	 * @return 自动生成的键(可能由JDBC insert语句返回)或更新的主键id值。
	 */
	@Transactional
	public Long update(String sql, SqlParameterSource paramSource) {
		GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
		long updateRowsNumber = getNamedParameterJdbcTemplate().update(sql.toString(), paramSource, generatedKeyHolder);
		int expectedValue = 1;
		updateAndExpectedEqual(updateRowsNumber, expectedValue);
		return generatedKeyHolder.getKey().longValue();
	}
	
	/**
	 * 更新数据<br>
     * 同 {@linkplain NamedParameterJdbcTemplate#update(String, Map)}<br>
     *
     * @param sql			要执行的更新SQL
     * @param paramJson		更新所用到的参数
	 * @return 受影响的行数
     */
	@Transactional
	public long update(String sql, JSONObject paramJson) {
		paramFormat(paramJson);
		aopBefore(sql, null,paramJson);
		return getNamedParameterJdbcTemplate().update(sql, paramJson);
	}

	/**
	 * 更新数据<br>
	 *
	 * <ul>
	 *     <li>对 {@linkplain NamedParameterJdbcTemplate#update(String, Map)} 方法的增强实现</li>
	 *     <li>将会对更新所影响的行数进行预期判断，若结果不符合预期值：<b>expectedValue</b>，那么此处便会抛出一个 {@linkplain DbException}</li>
	 * </ul>
	 *
	 * @param sql            要执行的更新SQL
	 * @param paramJson      更新所用到的参数
	 * @param expectedValue  更新所影响的行数预期值
	 * @param dBExpectedEnum 预期值确认方式
	 */
	@Transactional
	public void update(String sql, JSONObject paramJson, int expectedValue, DbExpectedEnum dBExpectedEnum) {
		paramFormat(paramJson);
		aopBefore(sql, null, paramJson);
		int updateRowsNumber = getNamedParameterJdbcTemplate().update(sql, paramJson);
		if (DbExpectedEnum.EQ == dBExpectedEnum) {
			updateAndExpectedEqual(updateRowsNumber, expectedValue);
		} else if (DbExpectedEnum.GE == dBExpectedEnum) {
			updateAndExpectedGreaterThanEqual(updateRowsNumber, expectedValue);
		}
	}

	/**
	 * 对多组参数进行批量更新处理<br>
	 * 同 {@linkplain NamedParameterJdbcTemplate#batchUpdate(String, Map[])}<br>
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
	 *
	 * @param sql        要执行的更新SQL
	 * @param paramJsons 更新所用到的参数数组（不调用 {@link #paramFormat(JSONObject)} 方法）
	 * @return 一个数组，其中包含受批处理中每个更新影响的行数
	 */
	@Transactional
	public int[] updateBatchNotParamFormat(String sql, JSONObject[] paramJsons) {
		aopBefore(sql, null, paramJsons);
		return getNamedParameterJdbcTemplate().batchUpdate(sql, paramJsons);
	}

	// Update

	/**
	 * 构建绝对条件更新优化SQL
	 *
	 * @param tableName    表名
	 * @param paramJson    更新所用到的参数
	 * @param conditions   作为更新条件的参数名，对应paramJson内的key（注意：作为条件的参数，将不会用于字段值的更新）
	 * @param dBUpdateEnum 更新类型 {@linkplain DbUpdateEnum}
	 * @return 绝对条件更新优化SQL
	 */
    protected String updateSqlBuild(String tableName, JSONObject paramJson, String[] conditions, DbUpdateEnum dBUpdateEnum) {
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
				if (dBUpdateEnum == DbUpdateEnum.INCREMENT) {// 递增更新
					sql.append(dialect.getWrapper().wrap(key));
					sql.append(" + :");
				} else if (dBUpdateEnum == DbUpdateEnum.DECR // 递减更新
						|| dBUpdateEnum == DbUpdateEnum.DECR_UNSIGNED) {// 递减-无符号更新
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

		if (dBUpdateEnum == DbUpdateEnum.DECR_UNSIGNED) {// 递减-无符号更新
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
		aopBefore(null, tableName, paramJson);
		String sql = updateSqlBuild(tableName, paramJson, conditions, DbUpdateEnum.NORMAL);
        return (long) getNamedParameterJdbcTemplate().update(sql, paramJson);
    }

	/**
	 * <b>绝对</b>条件更新优化SQL<br><br>
	 *
	 * @param tableName    表名
	 * @param paramJson    更新所用到的参数
	 * @param conditions   作为更新条件的参数名，对应paramJson内的key（注意：作为条件的参数，将不会用于字段值的更新）
	 * @param dBUpdateEnum 更新类型 {@linkplain DbUpdateEnum}
	 * @return 受影响的行数
	 */
	@Transactional
    public Long update(String tableName, JSONObject paramJson, String[] conditions, DbUpdateEnum dBUpdateEnum) {
		paramFormat(paramJson);
		aopBefore(null, tableName, paramJson);
		String sql = updateSqlBuild(tableName, paramJson, conditions, dBUpdateEnum);
        return (long) getNamedParameterJdbcTemplate().update(sql, paramJson);
	}

	/**
	 * <b>绝对</b>条件更新优化SQL<br><br>
	 *
	 * @param tableName      表名
	 * @param paramJson      更新所用到的参数
	 * @param conditions     作为更新条件的参数名，对应paramJson内的key（注意：作为条件的参数，将不会用于字段值的更新）
	 * @param dBUpdateEnum   更新类型 {@linkplain DbUpdateEnum}
	 * @param expectedValue  更新所影响的行数预期值
	 * @param dBExpectedEnum 预期值确认方式
	 */
	@Transactional
    public void update(String tableName, JSONObject paramJson, String[] conditions, DbUpdateEnum dBUpdateEnum
    		, int expectedValue, DbExpectedEnum dBExpectedEnum) {
		paramFormat(paramJson);
		aopBefore(null, tableName, paramJson);
		String sql = updateSqlBuild(tableName, paramJson, conditions, dBUpdateEnum);
		int updateRowsNumber = getNamedParameterJdbcTemplate().update(sql, paramJson);
		if (DbExpectedEnum.EQ == dBExpectedEnum) {
			updateAndExpectedEqual(updateRowsNumber, expectedValue);
		} else if (DbExpectedEnum.GE == dBExpectedEnum) {
			updateAndExpectedGreaterThanEqual(updateRowsNumber, expectedValue);
		}
	}
	
	/**
	 * 更新-ById
	 * <p>根据表中主键ID进行更新
	 * <p><code style="color:red">依赖于接口传入 {@value DbConstant#PRIMARY_KEY} 参数时慎用此方法</code>，避免有序主键被遍历风险，造成数据越权行为。推荐使用 {@link #updateByBusinessUk(String, JSONObject)}</p>
	 *
     * @param tableName		表名
     * @param paramJson		更新所用到的参数（包含主键ID字段）
     */
	@Transactional
    public void updateById(String tableName, JSONObject paramJson) {
		updateById(tableName, paramJson, DbUpdateEnum.NORMAL);
    }
	
	/**
	 * 更新-ById
	 * <p>根据表中主键ID进行更新
	 * <p><code style="color:red">依赖于接口传入 {@value DbConstant#PRIMARY_KEY} 参数时慎用此方法</code>，避免有序主键被遍历风险，造成数据越权行为。推荐使用 {@link #updateByBusinessUk(String, JSONObject, DbUpdateEnum)}</p>
	 *
     * @param tableName		表名
     * @param paramJson		更新所用到的参数（包含主键ID字段）
     * @param dBUpdateEnum	更新类型 {@linkplain DbUpdateEnum}
     */
	@Transactional
    public void updateById(String tableName, JSONObject paramJson, DbUpdateEnum dBUpdateEnum) {
		String[] conditions = { DbConstant.PRIMARY_KEY };
		paramFormat(paramJson);
		aopBefore(null, tableName, paramJson);
		String sql = updateSqlBuild(tableName, paramJson, conditions, dBUpdateEnum);
		int updateRowsNumber = getNamedParameterJdbcTemplate().update(sql, paramJson);
        int expectedValue = 1;
		updateAndExpectedEqual(updateRowsNumber, expectedValue);
    }
	
	/**
	 * 批量更新-ById
	 * <p>根据表中主键ID进行批量更新
	 * <p><code style="color:red">依赖于接口传入 {@value DbConstant#PRIMARY_KEY} 参数时慎用此方法</code>，避免有序主键被遍历风险，造成数据越权行为。推荐使用 {@link #updateByBusinessUk(String, JSONObject[], DbUpdateEnum)}</p>
	 *
     * @param tableName    	表名
     * @param paramJsons	更新所用到的参数数组（包含主键ID字段）
     * @param dBUpdateEnum	更新类型 {@linkplain DbUpdateEnum}
     */
	@Transactional
    public void updateById(String tableName, JSONObject[] paramJsons, DbUpdateEnum dBUpdateEnum) {
		for (JSONObject paramJson : paramJsons) {
			paramFormat(paramJson);
		}

		updateByIdNotParamFormat(tableName, paramJsons, dBUpdateEnum);
    }

	/**
	 * 批量更新-ById（不调用 {@link #paramFormat(JSONObject)} 方法）
	 * <p>根据表中主键ID进行批量更新
	 * <p><code style="color:red">依赖于接口传入 {@value DbConstant#PRIMARY_KEY} 参数时慎用此方法</code>，避免有序主键被遍历风险，造成数据越权行为。推荐使用 {@link #updateByBusinessUkNotParamFormat(String, JSONObject[], DbUpdateEnum)}</p>
	 *
	 * @param tableName    	表名
	 * @param paramJsons	更新所用到的参数数组（包含主键ID字段）
	 * @param dBUpdateEnum	更新类型 {@linkplain DbUpdateEnum}
	 */
	@Transactional
	public void updateByIdNotParamFormat(String tableName, JSONObject[] paramJsons, DbUpdateEnum dBUpdateEnum) {
		String[] conditions = { DbConstant.PRIMARY_KEY };
		aopBefore(null, tableName, paramJsons);
		String sql = updateSqlBuild(tableName, paramJsons[0], conditions, dBUpdateEnum);
		int[] updateRowsNumberArray = getNamedParameterJdbcTemplate().batchUpdate(sql, paramJsons);
		int expectedValue = 1;
		updateBatchAndExpectedEqual(updateRowsNumberArray, expectedValue);
	}

	/**
	 * 更新-By业务键
	 * <p>根据表中业务键进行更新
	 * <p>默认业务键为key
	 * <p>业务键值推荐使用UUID5
	 * 
     * @param tableName		表名
     * @param paramJson		更新所用到的参数（包含业务键字段）
     */
	@Transactional
    public void updateByBusinessUk(String tableName, JSONObject paramJson) {
		updateByBusinessUk(tableName, paramJson, DbUpdateEnum.NORMAL);
    }
	
	/**
	 * 更新-By业务键
	 * <p>根据表中业务键进行更新
	 * <p>默认业务键为key
	 * <p>业务键值推荐使用UUID5
	 * 
     * @param tableName		表名
     * @param paramJson		更新所用到的参数（包含业务键字段）
     * @param dBUpdateEnum	更新类型 {@linkplain DbUpdateEnum}
     */
	@Transactional
    public void updateByBusinessUk(String tableName, JSONObject paramJson, DbUpdateEnum dBUpdateEnum) {
		String[] conditions = { getJdbcProperties().getBusinessUk() };
		paramFormat(paramJson);
		aopBefore(null, tableName, paramJson);
		String sql = updateSqlBuild(tableName, paramJson, conditions, dBUpdateEnum);
		int updateRowsNumber = getNamedParameterJdbcTemplate().update(sql, paramJson);
        int expectedValue = 1;
		updateAndExpectedEqual(updateRowsNumber, expectedValue);
    }
	
	/**
	 * 批量更新-By业务键
	 * <p>根据表中业务键进行批量更新
	 * <p>默认业务键为key
	 * <p>业务键值推荐使用UUID5
	 * 
     * @param tableName    	表名
     * @param paramJsons	更新所用到的参数数组（包含业务键字段）
     * @param dBUpdateEnum	更新类型 {@linkplain DbUpdateEnum}
     */
	@Transactional
    public void updateByBusinessUk(String tableName, JSONObject[] paramJsons, DbUpdateEnum dBUpdateEnum) {
		for (JSONObject paramJson : paramJsons) {
			paramFormat(paramJson);
		}

		updateByBusinessUkNotParamFormat(tableName, paramJsons, dBUpdateEnum);
    }

	/**
	 * 批量更新-By业务键（不调用 {@link #paramFormat(JSONObject)} 方法）
	 * <p>根据表中业务键进行批量更新
	 * <p>默认业务键为key
	 * <p>业务键值推荐使用UUID5
	 *
	 * @param tableName    	表名
	 * @param paramJsons	更新所用到的参数数组（包含业务键字段）
	 * @param dBUpdateEnum	更新类型 {@linkplain DbUpdateEnum}
	 */
	@Transactional
	public void updateByBusinessUkNotParamFormat(String tableName, JSONObject[] paramJsons, DbUpdateEnum dBUpdateEnum) {
		String[] conditions = { getJdbcProperties().getBusinessUk() };
		aopBefore(null, tableName, paramJsons);
		String sql = updateSqlBuild(tableName, paramJsons[0], conditions, dBUpdateEnum);
		int[] updateRowsNumberArray = getNamedParameterJdbcTemplate().batchUpdate(sql, paramJsons);
		int expectedValue = 1;
		updateBatchAndExpectedEqual(updateRowsNumberArray, expectedValue);
	}

	/**
	 * <b>更新-排序</b><br>
	 * <i>使用限制：见</i> {@linkplain DbInsert#insertWithSortIdxAutoIncrement(String, JSONObject, String...)}
	 *
	 * @param tableName  表名
	 * @param id         主键ID
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
		long sortIdx = sortJson.getLongValue(DbConstant.FIELD_DEFINITION_SORT_IDX);
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
		paramJson.put(DbConstant.FIELD_DEFINITION_SORT_IDX, updateSortList);
		List<JSONObject> list = list(tableName, paramJson);
		
		// 5. 组装跟随移动参数到参数列表
		JSONArray paramJsonArray = new JSONArray();
		for (JSONObject actionJSON : list) {
			Long actionId = actionJSON.getLong(DbConstant.PRIMARY_KEY);
			Long actionSort = actionJSON.getLong(DbConstant.FIELD_DEFINITION_SORT_IDX);
			if (isASC) {
				actionSort -= 1;
			} else {
				actionSort += 1;
			}
			JSONObject actionParamJSON = new JSONObject();
			actionParamJSON.put(DbConstant.PRIMARY_KEY, actionId);
			actionParamJSON.put(DbConstant.FIELD_DEFINITION_SORT_IDX, actionSort);
			paramJsonArray.add(actionParamJSON);
		}
		
		// 6. 添加排序更新参数到参数列表
		JSONObject updateSortParam = new JSONObject();
		updateSortParam.put(DbConstant.PRIMARY_KEY, id);
		updateSortParam.put(DbConstant.FIELD_DEFINITION_SORT_IDX, updateSortIdx);
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
     * @param dBUpdateEnum	更新类型 {@linkplain DbUpdateEnum}
     */
    public void updateBatch(String tableName, JSONObject[] paramJsons, String[] conditions, DbUpdateEnum dBUpdateEnum) {
		for (JSONObject paramJson : paramJsons) {
			paramFormat(paramJson);
		}

		updateBatchNotParamFormat(tableName, paramJsons, conditions, dBUpdateEnum);
	}

	/**
	 * 更新-批量（不调用 {@link #paramFormat(JSONObject)} 方法）
	 * <p>一组条件对应一条数据，并且每组条件都采用相同的key
	 *
	 * @param tableName    	表名
	 * @param paramJsons	更新所用到的参数数组
	 * @param conditions	作为更新条件的参数名，对应paramJson内的key（注意：作为条件的参数，将不会用于字段值的更新）
	 * @param dBUpdateEnum	更新类型 {@linkplain DbUpdateEnum}
	 */
	@Transactional(rollbackFor = {RuntimeException.class, Error.class})
	public void updateBatchNotParamFormat(String tableName, JSONObject[] paramJsons, String[] conditions, DbUpdateEnum dBUpdateEnum) {
		// 1. 获得SQL
		String sql = updateSqlBuild(tableName, paramJsons[0], conditions, dBUpdateEnum);
		aopBefore(sql, tableName, paramJsons);

		// 2. 执行
		int[] updateRowsNumberArray = getNamedParameterJdbcTemplate().batchUpdate(sql, paramJsons);

		// 3. 确认影响行数
		int expectedValue = 1;
		updateBatchAndExpectedEqual(updateRowsNumberArray, expectedValue);
	}

}
