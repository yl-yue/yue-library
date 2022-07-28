package ai.yue.library.test.controller.data.jdbc;

import ai.yue.library.base.util.ListUtils;
import ai.yue.library.base.util.MapUtils;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.jdbc.client.Db;
import ai.yue.library.data.jdbc.client.dialect.Wrapper;
import ai.yue.library.data.jdbc.ipo.PageIPO;
import ai.yue.library.test.constant.RoleEnum;
import ai.yue.library.test.constant.UserStatusEnum;
import ai.yue.library.test.dao.data.jdbc.CloneOneDAO;
import ai.yue.library.test.dao.data.jdbc.CloneTwoDAO;
import ai.yue.library.test.dao.data.jdbc.JdbcDAO;
import ai.yue.library.test.dataobject.jdbc.UserDO;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
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
	JdbcDAO jdbcDAO;
	@Autowired
	CloneOneDAO cloneOneDAO;
	@Autowired
	CloneTwoDAO cloneTwoDAO;
	@Autowired
	Db db;
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	String tableName = "user";

	@PostMapping("/insert")
	public Result<?> insert(UserDO userDO) {
		return R.success(jdbcDAO.insert(userDO));
	}

	@PostMapping("/insertSql")
	public Result<?> insertSql(UserDO userDO) {
		String sql = "insert into user (cellphone, role, user_status) values (:cellphone, :role, :user_status)";
		JSONObject paramJson = MapUtils.toSnakeCase(userDO);
		return R.success(db.update(sql, new MapSqlParameterSource(paramJson)));
	}

	@PostMapping("/insertAndReturnUuid")
	public Result<?> insertAndReturnUuid(JSONObject paramJson) {
		return R.success(db.insertAndReturnUuid("table_example_test2", MapUtils.toSnakeCase(paramJson)));
	}

	@PostMapping("/insertAndReturnKeyHolder")
	public Result<?> insertAndReturnKeyHolder(UserDO userDO) {
		Wrapper wrapper = new Wrapper('`');
		String tableName = wrapper.wrap(this.tableName);
		JSONObject paramJson = MapUtils.toSnakeCase(userDO);
		MapUtils.removeEmpty(paramJson);
		paramJson = wrapper.wrap(paramJson);
		db.paramFormat(paramJson);

		// 2. 创建JdbcInsert实例
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(db.getJdbcTemplate());
		// 设置表名
		simpleJdbcInsert.setTableName(tableName);
		// 设置主键名，添加成功后返回主键的值
		simpleJdbcInsert.setGeneratedKeyNames("id", "create_time", "cellphone");

		// 3. 设置ColumnNames
		List<String> keys = MapUtils.keyList(paramJson);
		List<String> columnNames = ListUtils.toList(db.getMetaData(tableName).getColumnNames());
		List<String> insertColumn = ListUtils.keepSameValue(keys, (List<String>) wrapper.wrap(columnNames));
		simpleJdbcInsert.setColumnNames(insertColumn);


		// 2. 插入源初始化
		KeyHolder keyHolder = simpleJdbcInsert.executeAndReturnKeyHolder(paramJson);

//		KeyHolder keyHolder = db.insertAndReturnKeyHolder("user", paramJson, "cellphone");
//		System.out.println(JSONObject.toJSONString(keyHolder));
//		return R.success(keyHolder.getKeyAs(String.class));
		return R.success(keyHolder);
	}

	@PostMapping("/insertBatch")
	public Result<?> insertBatch() {
		JSONObject[] paramJsons = new JSONObject[3];
		paramJsons[0] = new JSONObject();
		paramJsons[0].put("cellphone", "18523116321");
		paramJsons[0].put("role", RoleEnum.b2b_买家.name());
		paramJsons[0].put("user_status", UserStatusEnum.正常.name());

		paramJsons[1] = new JSONObject();
		paramJsons[1].put("cellphone", "18523116322");
		paramJsons[1].put("role", RoleEnum.b2b_买家.name());
		paramJsons[1].put("user_status", UserStatusEnum.正常.name());

		paramJsons[2] = new JSONObject();
		paramJsons[2].put("cellphone", "18523116323");
		paramJsons[2].put("role", RoleEnum.b2b_买家.name());
		paramJsons[2].put("user_status", UserStatusEnum.正常.name());
		jdbcDAO.insertBatch(paramJsons);
		return R.success(paramJsons);
	}
	
	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/deleteById")
	public Result<?> deleteById(@RequestParam("id") Long id) {
		jdbcDAO.deleteById(id);
		return R.success();
	}
	
	@DeleteMapping("/deleteParamJson")
	public Result<?> delete(JSONObject paramJson) {
		return R.success(db.delete(tableName, paramJson));
	}

	@DeleteMapping("/deleteLogic")
	public Result<?> deleteLogic(JSONObject paramJson) {
		return R.success(db.deleteLogic(tableName, paramJson));
	}

	@GetMapping("/getMetaData")
	public Result<?> getMetaData() {
		SqlRowSetMetaData metaData = db.getMetaData("user");
		int columnCount = metaData.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {
			Map<String,String> fieldMap = new HashMap<String,String>();
			fieldMap.put("ColumnName", metaData.getColumnName(i));
			fieldMap.put("ColumnType", String.valueOf(metaData.getColumnType(i)));
			fieldMap.put("ColumnTypeName", metaData.getColumnTypeName(i));
			fieldMap.put("CatalogName", metaData.getCatalogName(i));
			fieldMap.put("ColumnClassName", metaData.getColumnClassName(i));
			fieldMap.put("ColumnLabel", metaData.getColumnLabel(i));
			fieldMap.put("Precision", String.valueOf(metaData.getPrecision(i)));
			fieldMap.put("Scale", String.valueOf(metaData.getScale(i)));
			fieldMap.put("SchemaName", metaData.getSchemaName(i));
			fieldMap.put("TableName", metaData.getTableName(i));
			fieldMap.put("SchemaName", metaData.getSchemaName(i));
			System.out.println(fieldMap);
		}

		return R.success(metaData);
	}

	@GetMapping("/query")
	public Result<?> query(Long id) {
		// 单个
		System.out.println(db.getById(tableName, id));
		System.out.println(db.getById(tableName, id, UserDO.class));
		System.out.println(db.queryForObject("select id from user where id = 999", null, Long.class));

		// 分页
		PageIPO pageIPO = PageIPO.builder().page(1).limit(10).build();
		System.out.println(db.page(tableName, pageIPO));
		System.out.println(db.page(tableName, pageIPO, UserDO.class));

		// 返回结果
		return R.success(jdbcDAO.getById(id));
	}

	/**
	 * Db深度克隆测试
	 */
	@GetMapping("/dbCloneTest")
	public Result<?> dbCloneTest() {
		int sizeOne = cloneOneDAO.listAll().size();
		int sizeTow = cloneTwoDAO.listAll().size();
		System.out.println(sizeOne);
		System.out.println(sizeTow);
		return R.success();
	}
	
}
