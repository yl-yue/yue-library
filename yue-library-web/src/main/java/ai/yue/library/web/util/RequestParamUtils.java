package ai.yue.library.web.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.util.ObjectUtils;
import ai.yue.library.web.util.servlet.ServletUtils;

/**
 * 请求参数工具栏
 *
 * @author: liuyang
 * @Date: 2020/6/5
 */
public class RequestParamUtils {

    /**
     * 工具栏，不允许实例
     */
    private RequestParamUtils() {
    }

    /**
     * 获取请求参数
     *
     * @return JSONObject
     */
    public static JSONObject getParam() {
        HttpServletRequest request = ServletUtils.getRequest();
        String contentType = request.getHeader(HttpHeaders.CONTENT_TYPE);
        //判断请求内容类型
		if (MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
			return getRawJson(request);
//		} else if (StringUtils.isNotEmpty(contentType)// 文件上传
//				&& RequestContentTypeConstant.FROM_DATA.equals(contentType.split(";")[0])) {
//			return getMultipartFileJson(request);
		} else {
			return getRequestUrlParamJson(request);
		}
    }
    
    /**
     * 获取参数，转换为java对象
     *
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
     * @return json对象
     */
    private static JSONObject getRawJson(HttpServletRequest request) {
        BufferedReader br = null;
        //获取url中的参数
        JSONObject json = getRequestUrlParamJson(request);
        try {
            //读取字符流
            br = request.getReader();
            String line = br.readLine();
            if (line == null) {
                return null;
            } 

            //不为空则读取转为字符串
            StringBuilder ret = new StringBuilder();
            ret.append(line);
            while ((line = br.readLine()) != null) {
                ret.append('\n').append(line);
            }
            //将字符串转为json
            if (ObjectUtils.isNotNull(json)) {
                JSONObject tempJson = JSONObject.parseObject(ret.toString());
                for (String key : tempJson.keySet()) {
                    json.put(key, tempJson.get(key));
                }
            } else {
                json = JSONObject.parseObject(ret.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        return json;
    }

}
