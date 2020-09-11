package ai.yue.library.test.controller.web;

import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.util.ParamUtils;
import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.ResultInfo;
import ai.yue.library.web.util.servlet.ServletUtils;

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
		ServletInputStream inputStream = request.getInputStream();
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(body);
		jsonArray.add(body2);
		jsonArray.add(body);
		jsonArray.add(param);
		jsonArray.add(param2);
		jsonArray.add(inputStream);
		return ResultInfo.success(jsonArray);
	}
	
}
