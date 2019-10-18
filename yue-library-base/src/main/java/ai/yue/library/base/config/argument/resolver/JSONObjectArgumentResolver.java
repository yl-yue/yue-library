package ai.yue.library.base.config.argument.resolver;

import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.alibaba.fastjson.JSONObject;

/**
 * @author	ylyue
 * @since	2019年8月2日
 */
public class JSONObjectArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(JSONObject.class);
	}
	
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		Map<String, String[]> parameterMap = webRequest.getParameterMap();
		JSONObject paramJson = new JSONObject(true);
		parameterMap.forEach((key, values) -> {
			if (values.length > 0) {
				paramJson.put(key, values[0]);
			}
		});
		
		return paramJson;
	}

}
