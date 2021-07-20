package ai.yue.library.template.boot.controller.user;

import ai.yue.library.base.view.Result;
import ai.yue.library.template.boot.ipo.user.UserIPO;
import ai.yue.library.template.boot.service.user.UserService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
	 * @param userIPO
	 * @return
	 */
	@PostMapping("/insert")
	public Result<?> insert(@Valid UserIPO userIPO) {
		System.out.println(JSONObject.toJSONString(userIPO));
		return userService.insert(userIPO);
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
	 * 列表-全部
	 * 
	 * @return
	 */
	@GetMapping("/listAll")
	public Result<?> listAll() {
		return userService.listAll();
	}
	
}
