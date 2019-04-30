package ai.yue.library.data.redis.client;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.config.properties.ConstantProperties;
import ai.yue.library.base.constant.TokenConstant;
import ai.yue.library.base.exception.LoginException;
import ai.yue.library.base.exception.ResultException;
import ai.yue.library.base.util.CookieUtils;
import ai.yue.library.base.util.StringUtils;
import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.ResultInfo;
import ai.yue.library.data.redis.config.properties.UserProperties;
import ai.yue.library.data.redis.dto.QqUserDTO;
import ai.yue.library.data.redis.dto.WxUserDTO;
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
	RestTemplate restTemplate;
	@Autowired
	HttpServletRequest request;
	@Autowired
	HttpServletResponse response;
	@Autowired
	ConstantProperties constantProperties;
	@Autowired
	UserProperties userProperties;
	
	// 微信-URI
	
	/** 通过code获取access_token */
	private static final String WX_URI_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={appid}&secret={secret}&code={code}&grant_type=authorization_code";
	/** 获取用户个人信息 */
	private static final String WX_URI_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?access_token={access_token}&openid={openid}";
	
	// QQ-URI
	
	/** 获取用户个人信息 */
	private static final String QQ_URI_USER_INFO = "https://graph.qq.com/user/get_user_info?oauth_consumer_key={oauth_consumer_key}&access_token={access_token}&openid={openid}";
	
	/**
	 * 通过code获取access_token
	 * @param code
	 * @return
	 */
	public JSONObject getWxAccessToken(String code) {
		JSONObject paramJson = new JSONObject();
		paramJson.put("appid", userProperties.getWx_appid());
		paramJson.put("secret", userProperties.getWx_secret());
		paramJson.put("code", code);
		String responseString = restTemplate.getForObject(WX_URI_ACCESS_TOKEN, String.class, paramJson);
		JSONObject wxAccessToken = JSONObject.parseObject(responseString);
		return wxAccessToken;
	}
	
	/**
	 * 获取用户个人信息
	 * @param access_token
	 * @param openid
	 * @return
	 */
	public WxUserDTO getWxUserInfo(String access_token, String openid) {
		JSONObject paramJson = new JSONObject();
		paramJson.put("access_token", access_token);
		paramJson.put("openid", openid);
		String result = restTemplate.getForObject(WX_URI_USER_INFO, String.class, paramJson);
		WxUserDTO wxUserDTO = JSONObject.parseObject(result, WxUserDTO.class);
		if (null == wxUserDTO.getOpenid()) {
			throw new ResultException(ResultInfo.error(result));
		}

		return wxUserDTO;
	}
	
	/**
	 * 获取用户个人信息
	 * @param access_token
	 * @param openid
	 * @return
	 */
	public QqUserDTO getQqUserInfo(String access_token, String openid) {
		JSONObject paramJson = new JSONObject();
		paramJson.put("oauth_consumer_key", userProperties.getQq_appid());
		paramJson.put("access_token", access_token);
		paramJson.put("openid", openid);
		String result = restTemplate.getForObject(QQ_URI_USER_INFO, String.class, paramJson);
		QqUserDTO qqUserDTO = JSONObject.parseObject(result, QqUserDTO.class);
		if (null == qqUserDTO.getGender()) {
			throw new ResultException(ResultInfo.error(result));
		}

		return qqUserDTO;
	}
	
	/**
	 * 获得请求token
	 * @return
	 */
	private String getRequestToken() {
		Cookie cookie = CookieUtils.get(TokenConstant.COOKIE_TOKEN_KEY);
		String token = "";
		if (cookie != null) {
			token = cookie.getValue();
		} else {
			token = request.getHeader(TokenConstant.COOKIE_TOKEN_KEY);
		}
		
		return token;
	}
	
	/**
	 * 获得用户ID
	 * @return user_id
	 */
	public Long getUserId() {
		try {
			// 1. 获得请求token
			String token = getRequestToken();
			
			// 2. 查询Redis中token的值
	        String tokenValue = redis.get(String.format(TokenConstant.REDIS_TOKEN_PREFIX, token));
	        
	        // 3. 返回user_id
			return JSONObject.parseObject(tokenValue).getLong("user_id");
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

			// 2. 查询Redis中token的值
			String tokenValue = redis.get(String.format(TokenConstant.REDIS_TOKEN_PREFIX, token));
			
			// 3. 返回POJO
			T t = JSONObject.parseObject(tokenValue, clazz);
			if (t == null) {
				throw new LoginException(null);
			}
			
			return t;
		} catch (Exception e) {
			throw new LoginException(e.getMessage());
		}
	}
	
	/**
	 * 登录
	 * @param userInfo 用户信息（必须包含：<font color=red>user_id</font>）
	 */
	public void login(Object userInfo) {
		// 1. 获得请求token
		String token = getRequestToken();
		
        // 2. 注销会话
		String redis_token_key = String.format(TokenConstant.REDIS_TOKEN_PREFIX, token);
        if (StringUtils.isNotEmpty(token)) {
        	String tokenValue = redis.get(redis_token_key);
        	if (StringUtils.isNotEmpty(tokenValue)) {
        		redis.del(redis_token_key);
        	}
        }
        
        // 3. 生成新的token
        token = UUID.randomUUID().toString();

		// 4. 登录成功-设置token至Redis
		Integer tokenTimeout = constantProperties.getToken_timeout();
		redis.set(redis_token_key, userInfo, tokenTimeout);
		
		// 5. 登录成功-设置token至Cookie
		CookieUtils.set(TokenConstant.COOKIE_TOKEN_KEY, token, tokenTimeout);
		
		// 6. 登录成功-设置token至Header
		response.setHeader(TokenConstant.COOKIE_TOKEN_KEY, token);
	}
	
	/**
	 * 登出
	 * @return
	 */
    public Result<?> logout() {
		// 1. 获得请求token
		String token = getRequestToken();
		
		// 2. 确认token
		if (StringUtils.isEmpty(token)) {
			return ResultInfo.unauthorized();
		}
		
		// 3. 清除Redis-token
		redis.del(String.format(TokenConstant.REDIS_TOKEN_PREFIX, token));
		
		// 4. 清除Cookie-token
		CookieUtils.set(TokenConstant.COOKIE_TOKEN_KEY, null, 0);
		
		// 5. 返回结果
		return ResultInfo.success();
    }
	
}
