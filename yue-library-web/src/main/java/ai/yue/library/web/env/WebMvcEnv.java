package ai.yue.library.web.env;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.view.Result;
import ai.yue.library.base.webenv.WebEnv;
import ai.yue.library.web.util.RequestParamUtils;
import ai.yue.library.web.util.servlet.ServletUtils;

/**
 * @author	ylyue
 * @since	2020年4月16日
 */
@Component
public class WebMvcEnv implements WebEnv {
	
	@Override
	public void resultResponse(Result<?> result) {
		HttpServletResponse response = ServletUtils.getResponse();
		response.setContentType("application/json; charset=utf-8");
		PrintWriter writer;
		try {
			writer = response.getWriter();
			writer.print(JSONObject.toJSONString(result));
			writer.close();
			response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public JSONObject getParam() {
		return RequestParamUtils.getParam();
	}

	@Override
	public <T> T getParam(Class<T> clazz) {
		return RequestParamUtils.getParam(clazz);
	}
	
}
