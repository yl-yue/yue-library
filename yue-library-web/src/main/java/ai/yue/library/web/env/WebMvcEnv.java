package ai.yue.library.web.env;

import ai.yue.library.base.view.Result;
import ai.yue.library.base.webenv.WebEnv;
import ai.yue.library.web.util.ServletUtils;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author	ylyue
 * @since	2020年4月16日
 */
@Slf4j
@Component
public class WebMvcEnv implements WebEnv {
	
	@Override
	public void resultResponse(Result<?> result) {
		HttpServletResponse response = ServletUtils.getResponse();
		response.setStatus(result.getCode());
		response.setContentType("application/json; charset=utf-8");
		PrintWriter writer;
		try {
			writer = response.getWriter();
			writer.print(JSONObject.toJSONString(result));
			writer.close();
			response.flushBuffer();
		} catch (IOException e) {
			log.error("写入响应结果失败", e);
		}
	}

	@Override
	public JSONObject getParam() {
		return ServletUtils.getParamToJson();
	}

	@Override
	public <T> T getParam(Class<T> clazz) {
		return ServletUtils.getParamToJavaBean(clazz);
	}
	
}
