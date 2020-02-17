package ai.yue.library.template.simple.controller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.yue.library.base.view.Result;
import ai.yue.library.data.redis.client.User;

/**
 * @author	ylyue
 * @since	2019年9月25日
 */
@RestController
public class LoginController {

	@Autowired
	User user;
	
	/**
	 * 登出
	 *
	 * @return
	 */
	@GetMapping("/logout")
	public Result<?> logout() {
		return user.logout();
	}
	
}
