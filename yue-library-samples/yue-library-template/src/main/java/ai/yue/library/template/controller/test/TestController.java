package ai.yue.library.template.controller.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.ResultInfo;

/**
 * @author  孙金川
 * @version 创建时间：2019年6月25日
 */
@RestController
@RequestMapping("/test")
public class TestController {

	/**
	 * @return
	 */
	@GetMapping("/get")
	public Result<?> get() {
		// String - JSONObject
		String json = "{\r\n" + 
				"    \"key\": 666\r\n" + 
				"}";
		JSONObject result = Convert.convert(json, JSONObject.class);
		Convert.toObject(json, JSONObject.class);
		System.out.println(result);
		return ResultInfo.success();
	}
	
	/**
	 * @return
	 */
	@GetMapping("/get2")
	public Result<?> get2() {
		// String - JSONObject
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("key", 666666);
		String str = jsonObject.toJSONString();
		JSONObject result = Convert.convert(str, JSONObject.class);
		System.out.println(result);
		return ResultInfo.success();
	}
	
}
