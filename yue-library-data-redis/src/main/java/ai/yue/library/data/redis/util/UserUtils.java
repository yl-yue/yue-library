package ai.yue.library.data.redis.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.constant.TokenConstant;
import ai.yue.library.base.exception.LoginException;
import ai.yue.library.base.util.CookieUtils;
import ai.yue.library.data.redis.client.Redis;

/**
 * @author  孙金川
 * @version 创建时间：2018年4月24日
 */
public class UserUtils {
	
	/**
	 * 获得用户ID
	 * @param request
	 * @param redis
	 * @return
	 */
	public static Long getUserId(HttpServletRequest request, Redis redis) {
		try {
			// 1. 查询cookie
			Cookie cookie = CookieUtils.get(request, TokenConstant.COOKIE_TOKEN_KEY);
			String token = "";
	        if (null == cookie) {
	        	token = request.getHeader(TokenConstant.COOKIE_TOKEN_KEY);
	        }else {
	        	token = cookie.getValue();
	        }
			// 2. 查询redis
	        String tokenValue = redis.get(String.format(TokenConstant.REDIS_TOKEN_PREFIX, token));
			return JSONObject.parseObject(tokenValue).getLong("user_id");
		}catch (Exception e) {
			throw new LoginException(e.getMessage());
		}
	}
	
	/**
	 * 获得用户相关信息
	 * @param <T>
	 * @param request
	 * @param redis
	 * @param clazz
	 * @return
	 */
	public static <T> T getUser(HttpServletRequest request, Redis redis, Class<T> clazz) {
		try {
			// 1. 查询cookie
			Cookie cookie = CookieUtils.get(request, TokenConstant.COOKIE_TOKEN_KEY);
			String token = "";
	        if (null == cookie) {
	        	token = request.getHeader(TokenConstant.COOKIE_TOKEN_KEY);
	        }else {
	        	token = cookie.getValue();
	        }
			// 2. 查询redis
	        String tokenValue = redis.get(String.format(TokenConstant.REDIS_TOKEN_PREFIX, token));
			T t = JSONObject.parseObject(tokenValue, clazz);
			if (null == t) {
				throw new LoginException(null);
			}
			return t;
		}catch (Exception e) {
			throw new LoginException(e.getMessage());
		}
	}
	
}
