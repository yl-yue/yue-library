package ai.yue.library.template.boot.controller;

import ai.yue.library.base.annotation.api.version.ApiVersion;
import ai.yue.library.template.boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户CRUD实体示例（公开接口）
 *
 * @author	ylyue
 * @since	2019年9月25日
 */
@ApiVersion(1.2)
@RestController
@RequestMapping("/open/{version}/user")
public class OpenUserController {

	@Autowired
	UserService userService;
	
	// 公开访问接口
	
}
