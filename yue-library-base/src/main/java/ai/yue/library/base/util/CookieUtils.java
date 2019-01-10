package ai.yue.library.base.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * cookie工具类
 * @author  孙金川
 * @version 创建时间：2017年10月8日
 */
public class CookieUtils {

    /**
     * 设置
     * @param response
     * @param key
     * @param value
     * @param timeout
     */
    public static void set(HttpServletResponse response, String key, String value, int timeout) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setMaxAge(timeout);
        response.addCookie(cookie);
    }
    
    /**
     * 获取cookie
     * @param request
     * @param key
     * @return
     */
    public static Cookie get(HttpServletRequest request,
                           String key) {
        Map<String, Cookie> cookieMap = readCookieMap(request);
        if (cookieMap.containsKey(key)) {
            return cookieMap.get(key);
        }else {
            return null;
        }
    }

    /**
     * 将cookie封装成Map
     * @param request
     * @return
     */
    private static Map<String, Cookie> readCookieMap(HttpServletRequest request) {
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
