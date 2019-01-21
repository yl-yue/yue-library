package ai.yue.library.data.redis.client;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.constant.TokenConstant;
import ai.yue.library.base.exception.LoginException;
import ai.yue.library.base.util.CookieUtils;
import lombok.NoArgsConstructor;

/**
 * @author  孙金川
 * @version 创建时间：2018年4月24日
 */
@NoArgsConstructor
public class User {
	
	@Autowired
	Redis redis;
	@Autowired
	HttpServletRequest request;
	
	/**
	 * 获得用户ID
	 * @return
	 */
	public Long getUserId() {
		try {
			// 1. 查询cookie
			Cookie cookie = CookieUtils.get(TokenConstant.COOKIE_TOKEN_KEY);
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
	 * @param clazz
	 * @return
	 */
	public <T> T getUser(Class<T> clazz) {
		try {
			// 1. 查询cookie
			Cookie cookie = CookieUtils.get(TokenConstant.COOKIE_TOKEN_KEY);
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
