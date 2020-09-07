package ai.yue.library.web.util;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.util.StringUtils;
import ai.yue.library.web.util.servlet.ServletUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 请求参数工具栏
 *
 * @author: liuyang
 * @Date: 2020/6/5
 */
@Slf4j
public class RequestParamUtils {

    /**
     * 工具栏，不允许实例
     */
	private RequestParamUtils() {
	}

    /**
     * 获取请求参数
     *
     * @Author: liuyang
     * @return JSONObject
     */
    public static JSONObject getParam() {
        HttpServletRequest request = ServletUtils.getRequest();
        String contentType = request.getHeader(HttpHeaders.CONTENT_TYPE);
        //判断请求内容类型
        if (StringUtils.isNotEmpty(contentType) && MediaType.MULTIPART_FORM_DATA_VALUE.equals(contentType.split(";")[0])) {
            return getRequestUrlParamJson(request);
		} else {
			// TODO html、xml、JavaScript暂时当做json处理，后序有业务需求时再迭代
			return getRawJson(request);
		}
    }

    /**
     * 获取参数，转换为java对象
     *
     * @Author: liuyang
     * @param clazz java类
     * @return 实例对象
     */
    public static <T> T getParam(Class<T> clazz) {
        JSONObject paramJson = getParam();
        return Convert.toJavaBean(paramJson, clazz);
    }

    /**
     * 获取url参数
     *
     * @Author: liuyang
     * @return JSONObject
     */
    private static JSONObject getRequestUrlParamJson(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            json.put(paramName, request.getParameter(paramName));
        }
        return json;
    }

    /**
     * 获取raw JSON格式
     *
     * @Author: liuyang
     * @return json对象
     */
	private static JSONObject getRawJson(HttpServletRequest request) {
		// 获取url中的参数
		JSONObject json = getRequestUrlParamJson(request);
		String body = null;
		try {
			body = ServletUtils.getBody(request);
		} catch (IllegalStateException e) {
			log.warn("获取body读取流异常: {}", e.getMessage());
		}
		// 将字符串转为json
		if (StringUtils.isNotEmpty(body)) {
			JSONObject tempJson = Convert.toJSONObject(body);
			json.putAll(tempJson);
		}
		
		return json;
	}
    
}
