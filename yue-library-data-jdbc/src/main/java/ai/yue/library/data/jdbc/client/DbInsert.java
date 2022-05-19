package ai.yue.library.data.jdbc.client;

import ai.yue.library.base.exception.DbException;
import ai.yue.library.base.util.ListUtils;
import ai.yue.library.base.util.MapUtils;
import ai.yue.library.base.util.ObjectUtils;
import ai.yue.library.base.util.StringUtils;
import ai.yue.library.base.view.ResultPrompt;
import ai.yue.library.data.jdbc.config.properties.JdbcProperties;
import ai.yue.library.data.jdbc.constant.CrudEnum;
import ai.yue.library.data.jdbc.constant.DbConstant;
import ai.yue.library.data.jdbc.constant.DbUpdateEnum;
import ai.yue.library.data.jdbc.provider.FillDataProvider;
import com.alibaba.fastjson.JSONObject;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>SQL优化型数据库操作-插入数据</b>
 * <p>有序主键：用于性能优化、分表分库、查询路由、分页优化、排序、增量获取等</p>
 * <p>无序主键：用于业务关联、多库多环境的数据唯一标识、追根溯源</p>
 *
 * @author ylyue
 * @since  2016/6/6 on 0.0.1
 */
class DbInsert extends DbDelete {
	
	// Insert

	/**
	 * 初始化-插入源
	 *
	 * @param tableName 表名
	 * @param paramJson 参数
	 * @return 插入源
	 */
	private SimpleJdbcInsert initSimpleJdbcInsert(String tableName, JSONObject paramJson) {
		// 1. 参数验证
		paramValidate(tableName, paramJson);

		// 2. 创建JdbcInsert实例
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(getJdbcTemplate());
		// 设置表名
		simpleJdbcInsert.setTableName(tableName);
		// 设置主键名，添加成功后返回主键的值
		simpleJdbcInsert.setGeneratedKeyName(DbConstant.FIELD_DEFINITION_ID);
		
		// 3. 设置ColumnNames
		List<String> keys = MapUtils.keyList(paramJson);
		List<String> columnNames = ListUtils.toList(getMetaData(tableName).getColumnNames());
		List<String> insertColumn = ListUtils.keepSameValue(keys, (List<String>) dialect.getWrapper().wrap(columnNames));
		simpleJdbcInsert.setColumnNames(insertColumn);
		
		// 4. 返回插入源
		return simpleJdbcInsert;
	}

	/**
	 * 初始化-插入源和参数
	 *
	 * @param tableName 表名
	 * @param paramJson 参数
	 * @return 插入源
	 */
	private SimpleJdbcInsert initSimpleJdbcInsertAndParam(String tableName, JSONObject paramJson) {
		// 1. 移除空对象
		MapUtils.removeEmpty(paramJson);

		// 2. 插入源初始化
		paramFormat(paramJson);
		dataEncrypt(tableName, paramJson);
		dataAudit(tableName, CrudEnum.C, paramJson);
		paramJson.putAll(FillDataProvider.getInsertParamJson(getJdbcProperties(), tableName));
		tableName = dialect.getWrapper().wrap(tableName);
		JSONObject wrap = dialect.getWrapper().wrap(paramJson);
		paramJson.clear();
		paramJson.putAll(wrap);
		SimpleJdbcInsert simpleJdbcInsert = initSimpleJdbcInsert(tableName, paramJson);

		// 3. 返回插入源
		return simpleJdbcInsert;
	}

	/**
	 * <b>插入一条数据，返回有序主键值</b>
	 * <p>有序主键数据库字段名：{@value DbConstant#FIELD_DEFINITION_ID}</p>
	 * <p>有序主键数据库字段值：大整数；推荐单表时数据库自增、分布式时雪花自增</p>
	 *
	 * @param tableName 表名
	 * @param paramJson 参数
	 * @return 返回有序主键值
	 */
	@Transactional
	public Long insert(String tableName, JSONObject paramJson) {
		// 返回有序主键值
		return initSimpleJdbcInsertAndParam(tableName, paramJson).executeAndReturnKey(paramJson).longValue();
	}

	/**
	 * 插入一条数据，返回无序主键值
	 * <p>数据库字段名：{@value DbConstant#FIELD_DEFINITION_UUID}，可在 application.yml 或 {@linkplain JdbcProperties} Bean 中重新自定义配置字段名</p>
	 * <p>数据库字段值：字符串；推荐UUID5、无符号、32位</p>
	 *
	 * @param tableName 表名
	 * @param paramJson 参数
	 * @return 返回无序主键值
	 */
	@Transactional
	public String insertAndReturnUuid(String tableName, JSONObject paramJson) {
		Long id = insert(tableName, paramJson);
		String uuid = paramJson.getString(getJdbcProperties().getFieldDefinitionUuid());
		if (StringUtils.isBlank(uuid)) {
			JSONObject result = getById(tableName, id);
			return result.getString(getJdbcProperties().getFieldDefinitionUuid());
		}

		return uuid;
	}

	/**
	 * <b>插入一条数据，返回多个字段值</b>
	 * <p>依赖有序主键规则</p>
	 *
	 * @param tableName        表名
	 * @param paramJson        参数
	 * @param returnFieldNames 需要返回的字段名称（如：{@value DbConstant#FIELD_DEFINITION_ID} 或 {@linkplain JdbcProperties#getFieldDefinitionUuid()}）
	 * @return 返回字段名对应的值（表中必须存在这些字段）
	 */
	@Transactional
	public JSONObject insertAndReturnFields(String tableName, JSONObject paramJson, String... returnFieldNames) {
		// 从请求参数中取
		Long id = insert(tableName, paramJson);
		JSONObject result = new JSONObject();
		List<String> needQueryField = new ArrayList<>();
		for (String returnFieldName : returnFieldNames) {
			Object returnFieldValue = paramJson.get(returnFieldName);
			if (ObjectUtils.isEmpty(returnFieldValue)) {
				needQueryField.add(returnFieldName);
			} else {
				result.put(returnFieldName, returnFieldValue);
			}
		}

		// 从结果中取
		if (needQueryField.isEmpty() == false) {
			JSONObject queryResult = getById(tableName, id);
			for (String returnFieldName : needQueryField) {
				result.put(returnFieldName, queryResult.get(returnFieldName));
			}
		}

		// 返回结果
		return result;
	}

	/**
	 * <b>插入一条数据，无需返回</b>
	 * 
	 * @param tableName 表名
	 * @param paramJson 参数
	 */
	@Transactional(rollbackFor = {RuntimeException.class, Error.class})
	public void insertNotReturn(String tableName, JSONObject paramJson) {
		// 执行插入数据，无需返回
		initSimpleJdbcInsertAndParam(tableName, paramJson).execute(paramJson);
	}

	/**
	 * <b>插入一条数据，并自动递增 <i>sort_idx</i></b>
	 *
	 * <blockquote>
	 * <b>使用条件：</b>
	 * <pre>1. id 必须为有序主键</pre>
	 * <pre>2. sort_idx 默认为排序字段（可配置），类型为 int unsigned 。DDL示例：<code>`sort_idx` int(8) UNSIGNED NOT NULL COMMENT '排序索引'</code></pre>
	 * </blockquote>
	 *
	 * @param tableName  表名
	 * @param paramJson  插入数据
	 * @param uniqueKeys 同sort_idx字段组合的唯一约束keys（表中不建议建立sort_idx字段的唯一约束，但可以建立普通索引，以便于提高查询性能），<b>可选参数</b>
	 * @return 返回主键值
	 */
	@Transactional
	public Long insertWithSortIdxAutoIncrement(String tableName, JSONObject paramJson, @Nullable String... uniqueKeys) {
		// 1. 参数验证
		paramValidate(tableName, paramJson);
		tableName = dialect.getWrapper().wrap(tableName);
		String fieldDefinitionSortIdxWrapped = dialect.getWrapper().wrap(getJdbcProperties().getFieldDefinitionSortIdx());
		paramFormat(paramJson);
		dataEncrypt(tableName, paramJson);

		// 2. 组装最大sort_idx值查询SQL
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT " + fieldDefinitionSortIdxWrapped + " FROM ");
		sql.append(tableName);
		sql.append(paramToWhereSql(paramJson, uniqueKeys));
		sql.append(" ORDER BY " + fieldDefinitionSortIdxWrapped + " DESC LIMIT 1");

		// 3. 查询最大sort_idx值
		paramJson = dialect.getWrapper().wrap(paramJson);
		Long sort_idx = queryForObject(sql.toString(), paramJson, Long.class);
		sort_idx = sort_idx == null ? 1L : sort_idx++;

		// 4. put sort_idx值
		paramJson.put(fieldDefinitionSortIdxWrapped, sort_idx);

		// 5. 执行
		dataAudit(tableName, CrudEnum.C, paramJson);
		paramJson.putAll(FillDataProvider.getInsertParamJson(getJdbcProperties(), tableName));
		SimpleJdbcInsert simpleJdbcInsert = initSimpleJdbcInsert(tableName, paramJson);
		return simpleJdbcInsert.executeAndReturnKey(paramJson).longValue();
	}
	
	/**
	 * 批量插入数据，主键必须为有序 {@value DbConstant#FIELD_DEFINITION_ID}
	 *
	 * @param tableName 表名
	 * @param paramJsons 参数
	 */
	public void insertBatch(String tableName, JSONObject[] paramJsons) {
		// 1. 参数验证
		paramValidate(tableName, paramJsons);

		// 2. 参数美化
		for (JSONObject paramJson : paramJsons) {
			paramFormat(paramJson);
		}

		// 3. 执行
		insertBatchNotParamFormat(tableName, paramJsons);
	}

	/**
	 * 批量插入数据，主键必须为有序 {@value DbConstant#FIELD_DEFINITION_ID}（不调用 {@link #paramFormat(JSONObject)} 方法）。
	 *
	 * @param tableName 表名
	 * @param paramJsons 参数
	 */
	@Transactional(rollbackFor = {RuntimeException.class, Error.class})
	public void insertBatchNotParamFormat(String tableName, JSONObject[] paramJsons) {
		// 1. 参数验证
		paramValidate(tableName, paramJsons);

		// 2. 插入源初始化
		dataEncrypt(tableName, paramJsons);
		dataAudit(tableName, CrudEnum.C, paramJsons);
		for (JSONObject paramJson : paramJsons) {
			paramJson.putAll(FillDataProvider.getInsertParamJson(getJdbcProperties(), tableName));
		}
		tableName = dialect.getWrapper().wrap(tableName);
		paramJsons = dialect.getWrapper().wrap(paramJsons);
		SimpleJdbcInsert simpleJdbcInsert = initSimpleJdbcInsert(tableName, paramJsons[0]);

		// 3. 执行
		int updateRowsNumber = simpleJdbcInsert.executeBatch(paramJsons).length;

		// 4. 确认插入条数
		if (updateRowsNumber != paramJsons.length) {
			throw new DbException(ResultPrompt.INSERT_BATCH_ERROR);
		}
	}

	// InsertOrUpdate

	/**
	 * <b>插入或更新</b><br>
	 * <i>表中必须存在数据唯一性约束</i>
	 * <p>更新触发条件：此数据若存在唯一性约束则更新，否则便执行插入数据
	 * <p><b>MySQL执行示例：</b><br>
	 * <code>INSERT INTO table (param1, param2, ...)</code><br>
	 * <code>VALUES</code><br>
	 * <code>(:param1, :param2, ...)</code><br>
	 * <code>ON DUPLICATE KEY UPDATE</code><br>
	 * <code>condition = condition + :condition, ...</code>
	 *
	 * @param tableName    表名
	 * @param paramJson    插入或更新所用到的参数
	 * @param conditions   更新条件（对应paramJson内的key值）
	 * @param dBUpdateEnum 更新类型 {@linkplain DbUpdateEnum}
	 * @return 受影响的行数
	 */
    @Transactional
    public Long insertOrUpdate(String tableName, JSONObject paramJson, String[] conditions, DbUpdateEnum dBUpdateEnum) {
    	return dialect.insertOrUpdate(tableName, paramJson, conditions, dBUpdateEnum);
    }
	
}
