package ai.yue.library.test.controller.base.request.param;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.ResultInfo;
import ai.yue.library.test.constant.TestEnum;
import ai.yue.library.test.ipo.ValidationIPO;

/**
 * @author	ylyue
 * @since	2019年8月2日
 */
@RestController
@RequestMapping("/requestParam")
public class RequestParamConroller {
	
	@GetMapping
	public Result<?> get(JSONObject paramJson) {
		return ResultInfo.success(paramJson);
	}
	
	@PostMapping
	public Result<?> post(JSONObject paramJson) {
		return ResultInfo.success(paramJson);
	}
	
	@PostMapping("/requestParam")
	public Result<?> requestParam(@RequestParam JSONObject paramJson) {
		return ResultInfo.success(paramJson);
	}
	
	@PutMapping
	public Result<?> put(JSONObject paramJson) {
		return ResultInfo.success(paramJson);
	}

	@DeleteMapping
	public Result<?> del(JSONObject paramJson) {
		return ResultInfo.success(paramJson);
	}
	
	
	
	@GetMapping("/2")
	public Result<?> get2(ValidationIPO validationIPO) {
		return ResultInfo.success(validationIPO);
	}
	
	@PostMapping("/2")
	public Result<?> post2(ValidationIPO validationIPO) {
		return ResultInfo.success(validationIPO);
	}
	
	@PutMapping("/2")
	public Result<?> put2(ValidationIPO validationIPO) {
		return ResultInfo.success(validationIPO);
	}
	
	@DeleteMapping("/2")
	public Result<?> del2(ValidationIPO validationIPO) {
		return ResultInfo.success(validationIPO);
	}
	
	
	@GetMapping("/get3")
	public Result<?> get3(Long id, boolean flag, String str, TestEnum testEnum) {
		JSONObject paramJson = new JSONObject();
		paramJson.put("id", id);
		paramJson.put("flag", flag);
		paramJson.put("str", str);
		paramJson.put("testEnum", testEnum);
		return ResultInfo.success(paramJson);
	}
	
	@PostMapping("/post3")
	public Result<?> post3(Long id, boolean flag, String str, TestEnum testEnum) {
		JSONObject paramJson = new JSONObject();
		paramJson.put("id", id);
		paramJson.put("flag", flag);
		paramJson.put("str", str);
		paramJson.put("testEnum", testEnum);
		return ResultInfo.success(paramJson);
	}
	
	@PutMapping("/3")
	public Result<?> put3(ValidationIPO validationIPO) {
		return ResultInfo.success(validationIPO);
	}
	
	@DeleteMapping("/3")
	public Result<?> del3(ValidationIPO validationIPO) {
		return ResultInfo.success(validationIPO);
	}
	
}
