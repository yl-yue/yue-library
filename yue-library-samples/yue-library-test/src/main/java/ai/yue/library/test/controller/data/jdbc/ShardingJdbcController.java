package ai.yue.library.test.controller.data.jdbc;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.jdbc.client.Db;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @author	ylyue
 * @since	2020年2月21日
 */
@RestController
@RequestMapping("/test/shardingJdbc")
public class ShardingJdbcController {

	@Autowired
	Db db;

	public StringBuffer deleteLastEqualString(StringBuffer sb, CharSequence suffix) {
		int end = sb.length();
		int start = end - suffix.length();
		String condition = sb.substring(start, end);
		if (suffix.equals(condition)) {
			return sb.delete(start, end);
		}
		
		return sb;
	}
	
	/**
	 * 
	 * @param paramJson
	 * @return
	 */
	@GetMapping("/get")
	public Result<?> get(JSONObject paramJson) {
		String sql = "SELECT cellphone, id_card FROM base_person";
		sql += db.paramToWhereSql(paramJson);
		
//		String sql = "SELECT key cellphone, id_card FROM base_person WHERE cellphone LIKE 1852314631%";
		return R.success(db.queryForList(sql, paramJson));
	}
	
	/**
	 * 插入数据
	 * @param paramJson
	 * @return
	 */
	@PostMapping("/insertBasePerson")
	public Long insertBasePerson(JSONObject paramJson) {
		StringBuffer sql = new StringBuffer("INSERT INTO base_person (");
		
		Set<String> keySet = paramJson.keySet();
		for (String key : keySet) {
			sql.append(key + ",");
		}
		
		
		sql = deleteLastEqualString(sql, ",");
		sql.append(") VALUES (");
		
		for (String key : keySet) {
			sql.append(":" + key + ",");
		}
		
		sql = deleteLastEqualString(sql, ",");
		sql.append(")");
		KeyHolder update = db.update(sql.toString(), new MapSqlParameterSource(paramJson), new GeneratedKeyHolder());
		return update.getKey().longValue();
//		db.update(sql, paramJson);
//		return db.insert("base_person", paramJson);
	}
	
}
