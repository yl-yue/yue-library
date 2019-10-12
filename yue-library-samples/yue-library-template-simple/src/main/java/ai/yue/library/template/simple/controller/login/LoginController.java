package ai.yue.library.template.simple.controller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.ResultInfo;
import ai.yue.library.data.redis.client.User;
import ai.yue.library.template.simple.dataobject.UserDO;
import ai.yue.library.template.simple.service.user.UserService;

/**
 * @author	ylyue
 * @since	2019年9月25日
 */
@RestController
@RequestMapping("/open")
public class LoginController {

	@Autowired
	User user;
	@Autowired
	UserService userService;
	
	/**
	 * 注册
	 * 
	 * @param paramJson
	 * @return
	 */
	@PostMapping("/register")
	public Result<?> register(@RequestParam JSONObject paramJson) {
		return userService.register(paramJson);
	}
	
	/**
	 * 登录
	 * 
	 * @param cellphone
	 * @param password
	 * @return
	 */
	@PostMapping("/login")
	public Result<?> login(@RequestParam("cellphone") String cellphone, @RequestParam("password") String password) {
		// 1. 确认用户
		var loginResult = userService.login(cellphone, password);
		loginResult.successValidate();
		UserDO userDO = loginResult.getData(UserDO.class);
		
		// 2. 登录
		user.login(userDO);
		
		// 3. 返回结果
		return ResultInfo.success(userDO);
	}
	
}
