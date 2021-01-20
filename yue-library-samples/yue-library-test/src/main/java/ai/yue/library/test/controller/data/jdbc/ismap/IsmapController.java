package ai.yue.library.test.controller.data.jdbc.ismap;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.jdbc.client.Db;
import ai.yue.library.test.dao.data.jdbc.JdbcDAO;
import ai.yue.library.test.dataobject.jdbc.UserDO;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @author	ylyue
 * @since	2020年2月21日
 */
@RestController
@RequestMapping("/ismap")
public class IsmapController {

	@Autowired
	JdbcDAO jdbcDAO;
	@Autowired
	Db db;
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	String tableName = "user";
	
	@PostMapping("/insert")
	public Result<?> insert(UserDO userDO) {
		return R.success(jdbcDAO.insert(userDO));
	}

	@DeleteMapping("/deleteParamJson")
	public Result<?> delete(JSONObject paramJson) {
		return R.success(db.delete(tableName, paramJson));
	}

	@PutMapping("/update")
	public Result<?> update(JSONObject paramJson) {
		db.update(tableName, paramJson, new String[]{"id"});
		return R.success(paramJson);
	}

	@GetMapping("/get")
	public Result<?> get(JSONObject paramJson) {
		return R.success(db.get(tableName, paramJson));
	}

}
