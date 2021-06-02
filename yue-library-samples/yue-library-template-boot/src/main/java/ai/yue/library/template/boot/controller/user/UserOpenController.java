package ai.yue.library.template.boot.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.yue.library.template.boot.service.user.UserService;

/**
 * @author	ylyue
 * @since	2019年9月25日
 */
@RestController
@RequestMapping("/open/user")
public class UserOpenController {

	@Autowired
	UserService userService;
	
	// 公开访问接口
	
}
