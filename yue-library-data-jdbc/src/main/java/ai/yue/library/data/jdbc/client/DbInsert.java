package ai.yue.library.data.jdbc.client;

import ai.yue.library.base.exception.DbException;
import ai.yue.library.base.util.ListUtils;
import ai.yue.library.base.util.MapUtils;
import ai.yue.library.base.view.ResultPrompt;
import ai.yue.library.data.jdbc.constant.DbConstant;
import ai.yue.library.data.jdbc.constant.DbUpdateEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <h2>SQL优化型数据库操作</h2>
 * Created by sunJinChuan on 2016/6/6
 * @since 0.0.1
 */
class DbInsert extends DbDelete {
	
	// Insert

	/**
	 * 插入源初始化
	 * 
	 * @param tableName
	 * @param paramJson
	 * @return
	 */
	private SimpleJdbcInsert insertInit(String tableName, JSONObject paramJson) {
		// 1. 参数验证
		paramValidate(tableName, paramJson);
		
		// 2. 创建JdbcInsert实例
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
		// 设置表名
		simpleJdbcInsert.setTableName(tableName);
		// 设置主键名，添加成功后返回主键的值
		simpleJdbcInsert.setGeneratedKeyName(DbConstant.PRIMARY_KEY);
		
		// 3. 设置ColumnNames
		List<String> keys = MapUtils.keyList(paramJson);
		List<String> columnNames = ListUtils.toList(getMetaData(tableName).getColumnNames());
		List<String> insertColumn = ListUtils.keepSameValue(keys, (List<String>) dialect.getWrapper().wrap(columnNames));
		simpleJdbcInsert.setColumnNames(insertColumn);
		
		// 4. 返回结果
		return simpleJdbcInsert;
	}

	/**
	 * 向表中插入一条数据，主键默认为id时使用。
	 * 
	 * @param tableName 表名
	 * @param paramJson 参数
	 * @return 返回主键值
	 */
	@Transactional
	public Long insert(String tableName, JSONObject paramJson) {
		// 1. 移除空对象
		MapUtils.removeEmpty(paramJson);
		
		// 2. 插入源初始化
		paramFormat(paramJson);
		tableName = dialect.getWrapper().wrap(tableName);
		paramJson = dialect.getWrapper().wrap(paramJson);
		SimpleJdbcInsert simpleJdbcInsert = insertInit(tableName, paramJson);

		// 3. 执行
		return simpleJdbcInsert.executeAndReturnKey(paramJson).longValue();
	}

	/**
	 * 向表中插入一条数据
	 * 
	 * @param tableName 表名
	 * @param paramJson 参数
	 */
	@Transactional
	public void insertNotReturn(String tableName, JSONObject paramJson) {
		// 1. 移除空对象
		MapUtils.removeEmpty(paramJson);
		
		// 2. 插入源初始化
		paramFormat(paramJson);
		tableName = dialect.getWrapper().wrap(tableName);
		paramJson = dialect.getWrapper().wrap(paramJson);
		SimpleJdbcInsert simpleJdbcInsert = insertInit(tableName, paramJson);

		// 3. 执行
		simpleJdbcInsert.execute(paramJson);
	}
	
	/**
	 * <h1>向表中插入一条数据，并自动递增 <i>sort_idx</i></h1>
	 * 
	 * <blockquote>
	 * <b>使用条件：</b>
	 * <pre>1. id 默认为主键</pre>
	 * <pre>2. sort_idx 默认为排序字段，类型为 int unsigned 。DDL示例：<code>`sort_idx` tinyint(2) UNSIGNED NOT NULL COMMENT '排序-索引'</code></pre>
	 * </blockquote>
	 * 
	 * @param tableName 表名
	 * @param paramJson 插入数据
	 * @param uniqueKeys 同sort_idx字段组合的唯一约束keys（表中不建议建立sort_idx字段的唯一约束，但可以建立普通索引，以便于提高查询性能），<b>可选参数</b>
	 * @return 返回主键值
	 */
	@Transactional
	public Long insertWithSortIdxAutoIncrement(String tableName, JSONObject paramJson, @Nullable String... uniqueKeys) {
		// 1. 参数验证
		paramValidate(tableName, paramJson);
		tableName = dialect.getWrapper().wrap(tableName);
		String sortFieldName = "sort_idx";
		String sortFieldNameWrapped = dialect.getWrapper().wrap(sortFieldName);
		paramFormat(paramJson);

		// 2. 组装最大sort_idx值查询SQL
		int sort_idx = 1;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT " + sortFieldNameWrapped + " FROM ");
		sql.append(tableName);
		String whereSql = paramToWhereSql(paramJson, uniqueKeys);
		sql.append(whereSql);
		sql.append(" ORDER BY " + sortFieldNameWrapped + " DESC LIMIT 1");
		
		// 3. 查询最大sort_idx值
		paramJson = dialect.getWrapper().wrap(paramJson);
		JSONObject result = queryForJson(sql.toString(), paramJson);
		if (result != null) {
			sort_idx = result.getInteger(sortFieldName) + 1;
		}
		
		// 4. put sort_idx值
		paramJson.put(sortFieldNameWrapped, sort_idx);
		
		// 5. 执行
		return insert(tableName, paramJson);
	}
	
	/**
	 * 向表中批量插入数据，主键默认为id时使用。
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
	 * 向表中批量插入数据，主键默认为id时使用（不调用 {@link #paramFormat(JSONObject)} 方法）。
	 *
	 * @param tableName 表名
	 * @param paramJsons 参数
	 */
	@Transactional
	public void insertBatchNotParamFormat(String tableName, JSONObject[] paramJsons) {
		// 1. 参数验证
		paramValidate(tableName, paramJsons);

		// 2. 插入源初始化
		tableName = dialect.getWrapper().wrap(tableName);
		paramJsons = dialect.getWrapper().wrap(paramJsons);
		SimpleJdbcInsert simpleJdbcInsert = insertInit(tableName, paramJsons[0]);

		// 3. 执行
		int updateRowsNumber = simpleJdbcInsert.executeBatch(paramJsons).length;

		// 4. 确认插入条数
		if (updateRowsNumber != paramJsons.length) {
			throw new DbException(ResultPrompt.INSERT_BATCH_ERROR);
		}
	}

	// InsertOrUpdate
	
    /**
     * <h2>插入或更新</h2>
     * <i>表中必须存在数据唯一性约束</i>
     * <p>更新触发条件：此数据若存在唯一性约束则更新，否则便执行插入数据
     * <p><b>MySQL执行示例：</b><br>
     * <code>INSERT INTO table (param1, param2, ...)</code><br>
     * <code>VALUES</code><br>
     * <code>(:param1, :param2, ...)</code><br>
     * <code>ON DUPLICATE KEY UPDATE</code><br>
     * <code>condition = condition + :condition, ...</code>
     * @param tableName		表名
     * @param paramJson		插入或更新所用到的参数
     * @param conditions	更新条件（对应paramJson内的key值）
     * @param dBUpdateEnum	更新类型 {@linkplain DbUpdateEnum}
     * @return 受影响的行数
     */
    @Transactional
    public Long insertOrUpdate(String tableName, JSONObject paramJson, String[] conditions, DbUpdateEnum dBUpdateEnum) {
    	return dialect.insertOrUpdate(tableName, paramJson, conditions, dBUpdateEnum);
    }
	
}
