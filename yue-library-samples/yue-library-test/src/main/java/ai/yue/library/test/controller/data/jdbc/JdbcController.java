package ai.yue.library.test.controller.data.jdbc;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.jdbc.client.Db;
import ai.yue.library.test.constant.RoleEnum;
import ai.yue.library.test.constant.UserStatusEnum;
import ai.yue.library.test.dao.data.jdbc.JdbcDAO;
import ai.yue.library.test.dataobject.jdbc.BasePersonDO;
import ai.yue.library.test.dataobject.jdbc.UserDO;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.web.bind.annotation.*;

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
	Db db;
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	String tableName = "user";
	
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
		
		return R.success();
	}
	
	/**
	 * 
	 * @param userDO
	 * @return
	 */
	@PostMapping("/insert")
	public Result<?> insert(UserDO userDO) {
		return R.success(jdbcDAO.insert(userDO));
	}
	
//	@PostMapping("/insertSql")
//	public Result<?> insertSql(UserDO userDO) {
//		String sql = "insert into user (cellphone, role, user_status) values (:cellphone, :role, :user_status)";
//		JSONObject paramJson = MapUtils.toSnakeCase(userDO);
//		return R.success(db.insertSql(sql, paramJson));
//	}
	
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
		return R.success();
	}
	
	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/delete")
	public Result<?> delete(@RequestParam("id") Long id) {
		jdbcDAO.delete(id);
		return R.success();
	}
	
	/**
	 * 删除
	 * 
	 * @param deleteParamJson
	 * @return
	 */
	@DeleteMapping("/deleteParamJson")
	public Result<?> delete(JSONObject paramJson) {
		return R.success(db.delete(tableName, paramJson));
	}
	
	// 查询性能测试
	
	@GetMapping("/performance")
	public Result<?> performance(JSONObject paramJson) {
		String sql =
				"SELECT\n" +
				"	* \n" +
				"FROM\n" +
				"	base_person_performance \n" +
				"WHERE\n" +
				"	id > 223150 \n" +
				"	LIMIT 10000";
		
		TimeInterval timer = DateUtil.timer();
		List<BasePersonDO> queryForList = db.queryForList(sql, paramJson, BasePersonDO.class);
    	System.out.println("10000条Json数据耗时：" + timer.intervalRestart());
    	
//		List<JSONObject> queryForList = db.queryForList(sql, paramJson);
//		System.out.println("10000条Json数据耗时：" + timer.intervalRestart());
//		
//		List<BasePersonDO> javaBeanList = Convert.toList(queryForList, BasePersonDO.class);
//		System.out.println("10000条Json数据转换JavaBean耗时：" + timer.intervalRestart());
//		
//		List<BasePersonDO> queryForList2 = namedParameterJdbcTemplate.query(sql, paramJson, BeanPropertyRowMapper.newInstance(BasePersonDO.class));
//		System.out.println("10000条JavaBean数据，原生转换耗时：" + timer.intervalRestart());
//		
//		List<BasePersonDO> queryForList3 = namedParameterJdbcTemplate.query(sql, paramJson, ai.yue.library.data.jdbc.support.BeanPropertyRowMapper.newInstance(BasePersonDO.class));
//		System.out.println("10000条JavaBean数据，增强转换耗时：" + timer.intervalRestart());
		
		return R.success(queryForList.size());
//		return R.success(basePersonDOs);
//		return R.success(basePersonDOs2.size());
	}
	
}
