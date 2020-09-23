package ai.yue.library.test.webflux.controller.base.request.param;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.R;

/**
 * @author	ylyue
 * @since	2019年8月2日
 */
@RestController
@RequestMapping("/requestParam")
public class RequestParamConroller {

	@PostMapping
	public Result<?> post(JSONObject paramJson) {
		return R.success(paramJson);
	}
	
}
