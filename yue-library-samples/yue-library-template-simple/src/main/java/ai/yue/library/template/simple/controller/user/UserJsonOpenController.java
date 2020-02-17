package ai.yue.library.template.simple.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.yue.library.template.simple.service.user.UserJsonService;

/**
 * @author	ylyue
 * @since	2020年2月13日
 */
@RestController
@RequestMapping("/open/userJson")
public class UserJsonOpenController {

	@Autowired
	UserJsonService demoUserService;
	
	// 公开访问接口
	
}
