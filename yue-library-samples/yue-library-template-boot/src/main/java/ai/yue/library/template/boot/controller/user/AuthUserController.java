package ai.yue.library.template.boot.controller.user;

import ai.yue.library.base.annotation.api.version.ApiVersion;
import ai.yue.library.base.view.Result;
import ai.yue.library.template.boot.ipo.user.UserIPO;
import ai.yue.library.template.boot.service.user.UserService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户CRUD实体示例（认证接口），接口规范见：<a href="https://ylyue.cn/#/%E8%A7%84%E7%BA%A6/%E6%8E%A5%E5%8F%A3%E8%B4%A8%E6%A3%80%E6%A0%87%E5%87%86">接口质检标准</a>
 *
 * @author	ylyue
 * @since	2019年9月25日
 */
@ApiVersion(1.2)
@RestController
@RequestMapping("/auth/{version}/user")
public class AuthUserController {

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
