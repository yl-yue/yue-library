package ai.yue.library.template.simple.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.yue.library.base.view.Result;
import ai.yue.library.template.simple.service.user.UserService;

/**
 * @author	ylyue
 * @since	2019年9月25日
 */
@RestController
@RequestMapping("/open/user")
public class UserOpenController {

	@Autowired
	UserService userService;
	
	/**
	 * 列表-全部
	 * 
	 * @return
	 */
	@GetMapping("/listAll")
	public Result<?> listAll() {
		return userService.listAll();
	}
	
}
