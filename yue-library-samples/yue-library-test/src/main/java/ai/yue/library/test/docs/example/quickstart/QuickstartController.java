package ai.yue.library.test.docs.example.quickstart;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 快速开始示例
 *
 * @author	ylyue
 * @since	2020年8月16日
 */
@RestController
@RequestMapping("/quickstart")
public class QuickstartController {

	@GetMapping("/get")
	public Result<?> get(JSONObject paramJson) {
		return R.success(paramJson);
	}
	
}
