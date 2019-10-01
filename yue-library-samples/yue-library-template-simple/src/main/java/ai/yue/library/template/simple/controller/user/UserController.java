package ai.yue.library.template.simple.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.view.Result;
import ai.yue.library.template.simple.service.user.UserService;

/**
 * @author	ylyue
 * @since	2019年9月25日
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;
	
	/**
	 * 插入数据
	 * 
	 * @param paramJson
	 * @return
	 */
	@PostMapping("/insert")
	public Result<?> insert(@RequestParam JSONObject paramJson) {
		return userService.insert(paramJson);
	}
	
	/**
	 * 单个
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/get")
	public Result<?> get(@RequestParam("id") Long id) {
		return userService.get(id);
	}
	
	/**
	 * 分页
	 * 
	 * @param paramJson
	 * @return
	 */
	@GetMapping("/page")
	public Result<?> page(JSONObject paramJson) {
		return userService.page(paramJson);
	}
	
	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/delete")
	public Result<?> delete(@RequestParam("id") Long id) {
		return userService.delete(id);
	}
	
}
