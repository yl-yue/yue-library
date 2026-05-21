package ai.yue.library.web.env;

import ai.yue.library.base.view.Result;
import ai.yue.library.base.webenv.WebEnv;
import ai.yue.library.web.util.ServletUtils;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

/**
 * @author	ylyue
 * @since	2020年4月16日
 */
@Slf4j
@Component
public class WebMvcEnv implements WebEnv {
	
	@SneakyThrows
    @Override
	public void resultResponse(Result<?> result) {
		HttpServletResponse response = ServletUtils.getResponse();
        response.setStatus(result.getCode());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(result.toJSONString());
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
