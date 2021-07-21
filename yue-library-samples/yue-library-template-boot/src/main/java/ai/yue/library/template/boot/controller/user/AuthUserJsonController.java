package ai.yue.library.template.boot.controller.user;

import ai.yue.library.base.annotation.api.version.ApiVersion;
import ai.yue.library.base.view.Result;
import ai.yue.library.template.boot.service.user.UserJsonService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户CRUD JSON示例（认证接口），接口规范见：<a href="https://ylyue.cn/#/%E8%A7%84%E7%BA%A6/%E6%8E%A5%E5%8F%A3%E8%B4%A8%E6%A3%80%E6%A0%87%E5%87%86">接口质检标准</a>
 *
 * @author	ylyue
 * @since	2020年2月13日
 */
@ApiVersion(1.2)
@RestController
@RequestMapping("/auth/{version}/userJson")
public class AuthUserJsonController {

	@Autowired
	UserJsonService userJsonService;
	
	/**
	 * 插入数据
	 * 
	 * @param paramJson
	 * @return
	 */
	@PostMapping("/insert")
	public Result<?> insert(JSONObject paramJson) {
		return userJsonService.insert(paramJson);
	}
	
	/**
	 * 分页
	 * 
	 * @param paramJson
	 * @return
	 */
	@GetMapping("/page")
	public Result<?> page(JSONObject paramJson) {
		return userJsonService.page(paramJson);
	}
	
}
