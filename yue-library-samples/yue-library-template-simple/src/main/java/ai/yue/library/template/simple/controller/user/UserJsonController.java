package ai.yue.library.template.simple.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.view.Result;
import ai.yue.library.template.simple.service.user.UserJsonService;

/**
 * @author	ylyue
 * @since	2020年2月13日
 */
@RestController
@RequestMapping("/userJson")
public class UserJsonController {

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
