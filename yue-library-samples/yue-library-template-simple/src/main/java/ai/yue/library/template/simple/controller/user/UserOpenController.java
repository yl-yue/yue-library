package ai.yue.library.template.simple.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.ResultInfo;
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
	 * 
	 * @param paramJson
	 * @return
	 */
	@PostMapping("/post")
	public Result<?> post(@RequestParam JSONObject paramJson) {
		return ResultInfo.success(paramJson);
	}
	
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
