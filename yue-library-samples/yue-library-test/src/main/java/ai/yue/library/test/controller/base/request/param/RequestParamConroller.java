package ai.yue.library.test.controller.base.request.param;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.constant.TestEnum;
import ai.yue.library.test.ipo.ParamParseIPO;
import ai.yue.library.test.ipo.ValidationIPO;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author	ylyue
 * @since	2019年8月2日
 */
@RestController
@RequestMapping("/requestParam")
public class RequestParamConroller {
	
	// Json
	
	@GetMapping
	public Result<?> get(JSONObject paramJson) {
		return R.success(paramJson);
	}
	
	@PostMapping
	public Result<?> post(JSONObject paramJson) {
		return R.success(paramJson);
	}
	
	@PostMapping("/requestParam")
	public Result<?> requestParam(@RequestParam JSONObject paramJson) {
		return R.success(paramJson);
	}
	
	@PutMapping
	public Result<?> put(JSONObject paramJson) {
		return R.success(paramJson);
	}

	@DeleteMapping
	public Result<?> del(JSONObject paramJson) {
		return R.success(paramJson);
	}
	
	// JavaBean
	
	@GetMapping("/2")
	public Result<?> get2(ValidationIPO validationIPO) {
		return R.success(validationIPO);
	}
	
	@PostMapping("/2")
	public Result<?> post2(ValidationIPO validationIPO) {
		return R.success(validationIPO);
	}
	
	@PutMapping("/2")
	public Result<?> put2(ValidationIPO validationIPO) {
		return R.success(validationIPO);
	}
	
	@DeleteMapping("/2")
	public Result<?> del2(ValidationIPO validationIPO) {
		return R.success(validationIPO);
	}
	
	// 普通类型
	
	@GetMapping("/get3")
	public Result<?> get3(Long id, boolean flag, String str, TestEnum testEnum) {
		JSONObject paramJson = new JSONObject();
		paramJson.put("id", id);
		paramJson.put("flag", flag);
		paramJson.put("str", str);
		paramJson.put("testEnum", testEnum);
		return R.success(paramJson);
	}
	
	@PostMapping("/post3")
	public Result<?> post3(Long id, boolean flag, String str, TestEnum testEnum) {
		JSONObject paramJson = new JSONObject();
		paramJson.put("id", id);
		paramJson.put("flag", flag);
		paramJson.put("str", str);
		paramJson.put("testEnum", testEnum);
		return R.success(paramJson);
	}
	
	// List AND Array
	
	@PostMapping("/list1")
	public Result<?> list1(String str, boolean bool, List<Long> ids) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("str", str);
		jsonObject.put("bool", bool);
		jsonObject.put("ids", ids);
		return R.success(jsonObject);
	}
	
	@PostMapping("/list2")
	public Result<?> list2(List<JSONObject> ids) {
		return R.success(ids);
	}
	
	@PostMapping("/list3")
	public Result<?> list3(JSONObject[] ids) {
		return R.success(ids);
	}
	
	@PostMapping("/list4")
	public Result<?> list4(String[] ids) {
		return R.success(ids);
	}
	
	@PostMapping("/list5")
	public Result<?> list5(@Valid List<ValidationIPO> validationIPOs) {
		return R.success(validationIPOs);
	}

	// 其它

	@GetMapping("/paramParseIPO")
	public Result<?> paramParseIPO(ParamParseIPO paramParseIPO) {
		return R.success(paramParseIPO);
	}

}
