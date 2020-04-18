package ai.yue.library.test.webflux.controller.test.other;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;

import ai.yue.library.base.util.MapUtils;
import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.ResultInfo;

/**
 * @author	ylyue
 * @since	2020年3月9日
 */
@RestController
@RequestMapping("/test")
public class TestController {

	/**
	 * 
	 * @param paramJson
	 * @return
	 */
	@GetMapping("/get")
	public Result<?> get(@RequestParam JSONObject paramJson) {
		JSONObject a = MapUtils.toPropertyNamingStrategy(paramJson, PropertyNamingStrategy.SnakeCase);
		System.out.println(a);
		return ResultInfo.success(a);
	}
	
}
