package ai.yue.library.test.spring.jdbc.controller.data.jdbc;

import ai.yue.library.base.util.ListUtils;
import ai.yue.library.base.util.MapUtils;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.spring.jdbc.dataobject.jdbc.UserDO;
import ai.yue.library.test.spring.jdbc.ipo.Page;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterDisposer;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author	ylyue
 * @since	2020年2月21日
 */
@RestController
@RequestMapping("/test/jdbc")
public class JdbcController {

	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	String tableName = "user";

	@PostMapping("/insertAndReturnKeyHolder")
	public Result<?> insertAndReturnKeyHolder(UserDO userDO) {
		JSONObject paramJson = MapUtils.toSnakeCase(userDO);
		MapUtils.removeEmpty(paramJson);

		// 2. 创建JdbcInsert实例
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
		// 设置表名
		simpleJdbcInsert.setTableName(tableName);
		// 设置主键名，添加成功后返回主键的值
//		simpleJdbcInsert.setGeneratedKeyNames("id", "create_time", "cellphone");
		simpleJdbcInsert.setGeneratedKeyNames("create_time");

		// 3. 设置ColumnNames
		List<String> keys = MapUtils.keyList(paramJson);
		List<String> columnNames = ListUtils.toList(getMetaData(tableName).getColumnNames());
		List<String> insertColumn = ListUtils.keepSameValue(keys, columnNames);
		simpleJdbcInsert.setColumnNames(insertColumn);


		// 2. 插入源初始化
		KeyHolder keyHolder = simpleJdbcInsert.executeAndReturnKeyHolder(paramJson);

//		KeyHolder keyHolder = db.insertAndReturnKeyHolder("user", paramJson, "cellphone");
//		System.out.println(JSONObject.toJSONString(keyHolder));
//		return R.success(keyHolder.getKeyAs(String.class));
		return R.success(keyHolder);
	}

	@PostMapping("/insertSql")
	public Result<?> insertSql(UserDO userDO) {
		String sql = "insert into user (cellphone, role, user_status) values (:cellphone, :role, :user_status)";
		JSONObject paramJson = MapUtils.toSnakeCase(userDO);

		GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
		String[] keyColumnNames = {"id", "create_time", "cellphone"};
		generatedKeyHolder.getKeyList().add(new JSONObject().fluentPut("cellphone", "cellphone").fluentPut("create_time", "create_time"));
		int update = namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(paramJson), generatedKeyHolder);
//		Map<String, ?> paramMap = paramJson;
//		Map<String, Object> execute = namedParameterJdbcTemplate.execute(sql, paramMap, ps -> {
//			ResultSet resultSet = ps.getResultSet();
//			System.out.println("resultSet: " + resultSet);
//			Map<String, Object> stringObjectMap = new ColumnMapRowMapper().mapRow(resultSet, 0);
//			System.out.println(stringObjectMap);
//			return stringObjectMap;
//			return null;
//		});
//		System.out.println(execute);
//		jdbcTemplate.update()


		System.out.println(update);
		return R.success(generatedKeyHolder);
//		return R.success(db.update(sql, new MapSqlParameterSource(paramJson), "id", "cellphone"));
	}

//	@GetMapping("/getMetaData")
//	public Result<?> getMetaData() {
//		SqlRowSetMetaData metaData = db.getMetaData("user");
//		int columnCount = metaData.getColumnCount();
//		for (int i = 1; i <= columnCount; i++) {
//			Map<String,String> fieldMap = new HashMap<String,String>();
//			fieldMap.put("ColumnName", metaData.getColumnName(i));
//			fieldMap.put("ColumnType", String.valueOf(metaData.getColumnType(i)));
//			fieldMap.put("ColumnTypeName", metaData.getColumnTypeName(i));
//			fieldMap.put("CatalogName", metaData.getCatalogName(i));
//			fieldMap.put("ColumnClassName", metaData.getColumnClassName(i));
//			fieldMap.put("ColumnLabel", metaData.getColumnLabel(i));
//			fieldMap.put("Precision", String.valueOf(metaData.getPrecision(i)));
//			fieldMap.put("Scale", String.valueOf(metaData.getScale(i)));
//			fieldMap.put("SchemaName", metaData.getSchemaName(i));
//			fieldMap.put("TableName", metaData.getTableName(i));
//			fieldMap.put("SchemaName", metaData.getSchemaName(i));
//			System.out.println(fieldMap);
//		}
//
//		return R.success(metaData);
//	}

	/**
	 * 获得表的元数据
	 * <p>检索元数据，即此行集合的列的数字、类型和属性。
	 *
	 * @param tableName 表名
	 * @return Spring的SqlRowSet的元数据接口
	 */
	public SqlRowSetMetaData getMetaData(String tableName) {
		StringBuffer sql = new StringBuffer("SELECT * FROM ").append(tableName).append(" LIMIT 0,0 ");
		return namedParameterJdbcTemplate.queryForRowSet(sql.toString(), Page.builder().page(0L).limit(0).build().toParamJson()).getMetaData();
	}

}
