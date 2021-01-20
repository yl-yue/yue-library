package ai.yue.library.test.controller.data.jdbc.performance;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.jdbc.client.Db;
import ai.yue.library.test.dataobject.jdbc.BasePersonDO;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author	ylyue
 * @since	2020年2月21日
 */
@RestController
@RequestMapping("/performance")
public class PerformanceController {

	@Autowired
	Db db;
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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
