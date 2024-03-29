package ai.yue.library.test.controller.data.mybatis;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 性能测试
 *
 * @author	ylyue
 * @since	2020年2月21日
 */
@RestController
@RequestMapping("/performance")
public class PerformanceController {

//    @Autowired
//    UserService userService;

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

//    @PutMapping("/updateBatchById")
//    public Result<?> updateBatchById() {
//        List<User> userList = userService.getServiceImpl().list();
//        List<User> updateUserList = new ArrayList<>();
//        for (User user : userList) {
//            User updateUser = new User();
//            updateUser.setId(user.getId());
//            updateUser.setIsTempUser(false);
//            updateUserList.add(updateUser);
//        }
//        return userService.updateBatchById(updateUserList);
//    }

}
