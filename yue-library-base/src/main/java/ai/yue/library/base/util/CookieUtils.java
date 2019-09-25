package ai.yue.library.base.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ai.yue.library.base.util.servlet.ServletUtils;

/**
 * Cookie工具类
 * 
 * @deprecated 请使用 {@linkplain ServletUtils}
 * @author  ylyue
 * @version 创建时间：2017年10月8日
 */
@Deprecated
public class CookieUtils {
	
    /**
     * 设置
     * @param key 设置的key
     * @param value 设置的值
     * @param timeout 超时时间（单位：秒）
     */
    public static void set(String key, String value, int timeout) {
		HttpServletResponse response = HttpUtils.getResponse();
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setMaxAge(timeout);
        response.addCookie(cookie);
    }
    
    /**
     * 获取cookie
     * @param key cookie的key
     * @return Cookie
     */
	public static Cookie get(String key) {
        Map<String, Cookie> cookieMap = readCookieMap();
        if (cookieMap.containsKey(key)) {
            return cookieMap.get(key);
        }else {
            return null;
        }
    }
	
    /**
     * 将cookie封装成Map
     * @return Map
     */
    private static Map<String, Cookie> readCookieMap() {
    	HttpServletRequest request = HttpUtils.getRequest();
        Map<String, Cookie> cookieMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie: cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        
        return cookieMap;
    }
    
}
