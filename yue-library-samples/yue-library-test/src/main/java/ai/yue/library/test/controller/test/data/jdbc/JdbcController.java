package ai.yue.library.test.controller.test.data.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.ResultInfo;
import ai.yue.library.test.dao.test.data.jdbc.JdbcDAO;
import ai.yue.library.test.dataobject.example.data.jdbc.UserDO;

/**
 * @author	ylyue
 * @since	2020年2月21日
 */
@RestController
@RequestMapping("/test/jdbc")
public class JdbcController {

	@Autowired
	JdbcDAO jdbcDAO;
	
	/**
	 * 
	 * @param userDO
	 * @return
	 */
	@PostMapping("/insert")
	public Result<?> insert(UserDO userDO) {
		return ResultInfo.success(jdbcDAO.insert(userDO));
	}
	
}
