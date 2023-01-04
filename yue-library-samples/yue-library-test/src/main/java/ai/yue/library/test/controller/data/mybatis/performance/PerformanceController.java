//package ai.yue.library.test.controller.data.jdbc.performance;
//
//import ai.yue.library.base.convert.Convert;
//import ai.yue.library.base.view.R;
//import ai.yue.library.base.view.Result;
//import ai.yue.library.data.jdbc.client.Db;
//import ai.yue.library.test.dataobject.jdbc.BasePersonDO;
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.core.date.TimeInterval;
//import cn.hutool.core.lang.Console;
//import com.alibaba.fastjson.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * 性能测试
// *
// * @author	ylyue
// * @since	2020年2月21日
// */
//@RestController
//@RequestMapping("/performance")
//public class PerformanceController {
//
//	@Autowired
//	Db db;
//	@Autowired
//	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
//
//	// 查询性能测试
//
//	@GetMapping("/performance")
//	public Result<?> performance(JSONObject paramJson) {
//		String sql =
//				"SELECT\n" +
//				"	* \n" +
//				"FROM\n" +
//				"	base_person_performance \n" +
//				"WHERE\n" +
//				"	id > 223150 \n" +
//				"	LIMIT 10000";
//
//		TimeInterval timer = DateUtil.timer();
//		List<Map<String, Object>> maps = namedParameterJdbcTemplate.queryForList(sql, paramJson);
//		Console.log("{}条map数据查询耗时：{}", maps.size(), timer.intervalRestart());
//
//		List<JSONObject> jsons = db.queryForList(sql, paramJson);
//		Console.log("{}条Json数据查询耗时：{}", jsons.size(), timer.intervalRestart());
//
//		List<BasePersonDO> springOriginalConvert = namedParameterJdbcTemplate.query(sql, paramJson, org.springframework.jdbc.core.BeanPropertyRowMapper.newInstance(BasePersonDO.class));
//		Console.log("{}条JavaBean数据查询，使用Spring原生转换耗时：{}", springOriginalConvert.size(), timer.intervalRestart());
//
//		List<BasePersonDO> javaBeans = db.queryForList(sql, paramJson, BasePersonDO.class);
//		Console.log("{}条JavaBean数据查询，使用yue-library Convert转换耗时：{}", javaBeans.size(), timer.intervalRestart());
//
//		List<BasePersonDO> javaBeanList = Convert.toList(jsons, BasePersonDO.class);
//		Console.log("{}条Json数据转换JavaBean耗时：{}", javaBeanList.size(), timer.intervalRestart());
//		return R.success(javaBeanList.size());
//	}
//
//}
