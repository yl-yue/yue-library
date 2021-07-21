package ai.yue.library.template.boot.controller.user;

import ai.yue.library.base.annotation.api.version.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.yue.library.template.boot.service.user.UserJsonService;

/**
 * 用户CRUD JSON示例（公开接口）
 *
 * @author	ylyue
 * @since	2020年2月13日
 */
@ApiVersion(1.2)
@RestController
@RequestMapping("/open/{version}/userJson")
public class OpenUserJsonController {

	@Autowired
	UserJsonService demoUserService;
	
	// 公开访问接口
	
}
