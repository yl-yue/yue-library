package ai.yue.library.test.controller.web;

import ai.yue.library.base.util.ParamUtils;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.web.util.ServletUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * @author	ylyue
 * @since	2020年8月4日
 */
@RestController
@RequestMapping("/repeatedlyReadServletRequest")
public class RepeatedlyReadServletRequestController {

	@PostMapping
	public Result<?> test(HttpServletRequest request) throws IOException {
		String body = ServletUtils.getBody(request);
		String body2 = ServletUtils.getBody(request);
		JSONObject param = ParamUtils.getParam();
		JSONObject param2 = ParamUtils.getParam();
		Map<String, String> paramMap = ServletUtils.getParamMap(request);
		ServletInputStream inputStream = request.getInputStream();
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(body);
		jsonArray.add(body2);
		jsonArray.add(param);
		jsonArray.add(param2);
		jsonArray.add(paramMap);
		jsonArray.add(inputStream);
		return R.success(jsonArray);
	}
	
}
