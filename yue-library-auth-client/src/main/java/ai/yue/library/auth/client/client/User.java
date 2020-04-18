package ai.yue.library.auth.client.client;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.auth.client.config.properties.AuthProperties;
import ai.yue.library.base.exception.LoginException;
import ai.yue.library.base.util.StringUtils;
import ai.yue.library.data.redis.client.Redis;
import ai.yue.library.web.util.servlet.ServletUtils;
import lombok.NoArgsConstructor;

/**
 * <b>User客户端</b>
 * <p>token自动解析获取用户信息
 * 
 * @author	ylyue
 * @since	2018年4月24日
 */
@NoArgsConstructor
public class User {
	
	@Autowired
	protected Redis redis;
	@Autowired
	protected HttpServletRequest request;
	@Autowired
	protected AuthProperties authProperties;
	
	/**
	 * 获得请求token
	 * @return
	 */
	public String getRequestToken() {
		Cookie cookie = ServletUtils.getCookie(authProperties.getCookieTokenKey());
		String token = "";
		if (cookie != null) {
			token = cookie.getValue();
		} else {
			token = request.getHeader(authProperties.getCookieTokenKey());
		}
		
		return token;
	}
	
	/**
	 * 获得用户ID
	 * <p><code style="color:red"><b>注意：若 userId == null ，请先确认 {@linkplain ai.yue.library.auth.service.client.User#login(Object)} 方法是否存入 {@linkplain AuthProperties#getUserKey()} 字段，此处可以传 JSON 与 POJO 对象</b></code>
	 * 
	 * @return userId
	 */
	public Long getUserId() {
		try {
			// 1. 获得请求token
			String token = getRequestToken();
			
			// 2. 确认token
			if (StringUtils.isEmpty(token)) {
				throw new LoginException("token == null");
			}
			
			// 3. 查询Redis中token的值
	        String tokenValue = redis.get(authProperties.getRedisTokenPrefix() + token);
	        
	        // 4. 返回userId
			return JSONObject.parseObject(tokenValue).getLong(authProperties.getUserKey());
		} catch (Exception e) {
			throw new LoginException(e.getMessage());
		}
	}
	
	/**
	 * 获得用户相关信息
	 * @param <T> 泛型
	 * @param clazz 泛型类型
	 * @return POJO对象
	 */
	public <T> T getUser(Class<T> clazz) {
		try {
			// 1. 获得请求token
			String token = getRequestToken();
			
			// 2. 确认token
			if (StringUtils.isEmpty(token)) {
				throw new LoginException("token == null");
			}
			
			// 3. 查询Redis中token的值
			String tokenValue = redis.get(authProperties.getRedisTokenPrefix() + token);
			
			// 4. 返回POJO
			T t = JSONObject.parseObject(tokenValue, clazz);
			if (t == null) {
				throw new LoginException(null);
			}
			return t;
		} catch (Exception e) {
			throw new LoginException(e.getMessage());
		}
	}
    
}
