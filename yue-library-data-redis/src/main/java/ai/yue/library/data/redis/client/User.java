package ai.yue.library.data.redis.client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.exception.LoginException;
import ai.yue.library.base.exception.ResultException;
import ai.yue.library.base.ipo.CaptchaIPO;
import ai.yue.library.base.util.CaptchaUtils;
import ai.yue.library.base.util.StringUtils;
import ai.yue.library.base.util.servlet.ServletUtils;
import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.ResultInfo;
import ai.yue.library.base.view.ResultPrompt;
import ai.yue.library.base.vo.CaptchaVO;
import ai.yue.library.data.redis.config.properties.ConstantProperties;
import ai.yue.library.data.redis.config.properties.QqProperties;
import ai.yue.library.data.redis.config.properties.WxOpenProperties;
import ai.yue.library.data.redis.dto.QqUserDTO;
import ai.yue.library.data.redis.dto.WxUserDTO;
import ai.yue.library.data.redis.vo.wx.open.AccessTokenVO;
import lombok.NoArgsConstructor;

/**
 * @author	孙金川
 * @since	2018年4月24日
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
	WxOpenProperties wxOpenProperties;
	@Autowired
	QqProperties qqProperties;
	
	// 微信-URI
	
	/** 通过code获取access_token */
	private static final String WX_URI_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={appid}&secret={secret}&code={code}&grant_type=authorization_code";
	/** 获取用户个人信息 */
	private static final String WX_URI_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?access_token={access_token}&openid={openid}";
	
	// QQ-URI
	
	/** 获取用户个人信息 */
	private static final String QQ_URI_USER_INFO = "https://graph.qq.com/user/get_user_info?oauth_consumer_key={oauth_consumer_key}&access_token={access_token}&openid={openid}";
	
	/**
	 * 微信-获取access_token
	 * @param code 微信授权code码
	 * @return accessTokenVO，参考微信返回说明文档
	 */
	public AccessTokenVO getWxAccessToken(String code) {
		JSONObject paramJson = new JSONObject();
		paramJson.put("appid", wxOpenProperties.getAppid());
		paramJson.put("secret", wxOpenProperties.getSecret());
		paramJson.put("code", code);
		String result = restTemplate.getForObject(WX_URI_ACCESS_TOKEN, String.class, paramJson);
		AccessTokenVO accessTokenVO = JSONObject.parseObject(result, AccessTokenVO.class);
		return accessTokenVO;
	}
	
	/**
	 * 微信-获取用户个人信息
	 * @param access_token 调用凭证
	 * @param openid 普通用户的标识，对当前开发者帐号唯一
	 * @return {@linkplain WxUserDTO}，开发者最好保存unionID信息，以便以后在不同应用之间进行用户信息互通。
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
	 * @param access_token 调用凭证
	 * @param openid 普通用户的标识，对当前开发者帐号唯一
	 * @return {@linkplain QqUserDTO}
	 */
	public QqUserDTO getQqUserInfo(String access_token, String openid) {
		JSONObject paramJson = new JSONObject();
		paramJson.put("oauth_consumer_key", qqProperties.getQq_appid());
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
     * 获得-验证码图片
     * <p>将验证码设置到redis
     * <p>将验证码图片写入response，并设置ContentType为image/png
     */
    public void getCaptchaImage() {
    	// 1. 创建图片验证码
    	CaptchaVO captchaVO = CaptchaUtils.createCaptchaImage(CaptchaIPO.builder().build());
    	String captcha = captchaVO.getCaptcha();
    	BufferedImage captchaImage = captchaVO.getCaptchaImage();
    	
    	// 2. 设置验证码到Redis
		String captcha_redis_key = String.format(CaptchaUtils.CAPTCHA_REDIS_PREFIX, captcha);
		redis.set(captcha_redis_key, captcha, constantProperties.getCaptcha_timeout());
		
		// 3. 设置验证码到响应输出流
		HttpServletResponse response = ServletUtils.getResponse();
        response.setContentType("image/png");
        OutputStream output;
		try {
			output = response.getOutputStream();
			// 响应结束时servlet会自动将output关闭
			ImageIO.write(captchaImage, "png", output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    /**
     * 验证-验证码
     * <p>验证码错误会抛出一个{@linkplain ResultException}异常，作为结果提示...<br>
     * 
     * @param captcha 验证码
     * @throws ResultException 验证码错误
     */
    public void captchaValidate(String captcha) {
    	String captcha_redis_key = String.format(CaptchaUtils.CAPTCHA_REDIS_PREFIX, captcha);
		String randCaptcha = redis.get(captcha_redis_key);
		if (StringUtils.isEmpty(randCaptcha) || !randCaptcha.equalsIgnoreCase(captcha)) {
			throw new ResultException(ResultInfo.dev_defined(ResultPrompt.CAPTCHA_ERROR));
		}
		
		redis.del(captcha_redis_key);
    }
    
	/**
	 * 获得请求token
	 * @return
	 */
	private String getRequestToken() {
		Cookie cookie = ServletUtils.getCookie(constantProperties.getCookie_token_key());
		String token = "";
		if (cookie != null) {
			token = cookie.getValue();
		} else {
			token = request.getHeader(constantProperties.getCookie_token_key());
		}
		
		return token;
	}
	
	/**
	 * 获得用户ID
	 * <p><code style="color:red"><b>注意：若 user_id == null ，请先确认 {@linkplain #login(Object)} 方法是否存入 user_id 字段，此处可以传 JSON 与 POJO 对象</b></code>
	 * 
	 * @return user_id
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
	        String tokenValue = redis.get(String.format(constantProperties.getRedis_token_prefix(), token));
	        
	        // 4. 返回user_id
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
			
			// 2. 确认token
			if (StringUtils.isEmpty(token)) {
				throw new LoginException("token == null");
			}
			
			// 3. 查询Redis中token的值
			String tokenValue = redis.get(String.format(constantProperties.getRedis_token_prefix(), token));
			
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
	
	/**
	 * 登录
	 * <p>登录成功-设置token至Cookie
	 * <p>登录成功-设置token至Header
	 * <p><code style="color:red"><b>注意：登录之后的所有相关操作，都是基于请求报文中所携带的token，若Cookie与Header皆没有token或Redis中匹配不到值，将视为未登录状态
	 * </b></code>
	 * 
	 * @param userInfo 用户信息（必须包含：<code style="color:red">user_id</code>）
	 * @return <b>token</b> 身份认证令牌 <code style="color:red"><b>（不建议使用，最好是忽略这个返回值，哪怕你只是将他放在响应体里面，也不推荐这样做）</b></code>
	 * <p>支持Cookie：建议使用默认的机制即可
	 * <p>不支持Cookie：建议从响应Header中获取token，之后的请求都将token放入请求Header中即可
	 */
	public String login(Object userInfo) {
		// 1. 获得请求token
		String token = getRequestToken();
		
        // 2. 注销会话
		String redis_token_key = null;
        if (StringUtils.isNotEmpty(token)) {
        	redis_token_key = String.format(constantProperties.getRedis_token_prefix(), token);
        	String tokenValue = redis.get(redis_token_key);
        	if (StringUtils.isNotEmpty(tokenValue)) {
        		redis.del(redis_token_key);
        	}
        }
        
        // 3. 生成新的token
        token = UUID.randomUUID().toString();
        redis_token_key = String.format(constantProperties.getRedis_token_prefix(), token);
        
		// 4. 登录成功-设置token至Redis
		Integer tokenTimeout = constantProperties.getToken_timeout();
		redis.set(redis_token_key, userInfo, tokenTimeout);
		
		// 5. 登录成功-设置token至Cookie
		ServletUtils.addCookie(constantProperties.getCookie_token_key(), token, tokenTimeout);
		
		// 6. 登录成功-设置token至Header
		response.setHeader(constantProperties.getCookie_token_key(), token);
		
		// 7. 登录成功-返回token
		return token;
	}
	
	/**
	 * 登出
	 * <p>清除Redis-token
	 * <p>清除Cookie-token
	 * 
	 * @return 成功
	 */
    public Result<?> logout() {
		// 1. 获得请求token
		String token = getRequestToken();
		
		// 2. 确认token
		if (StringUtils.isEmpty(token)) {
			return ResultInfo.unauthorized();
		}
		
		// 3. 清除Redis-token
		redis.del(String.format(constantProperties.getRedis_token_prefix(), token));
		
		// 4. 清除Cookie-token
		ServletUtils.addCookie(constantProperties.getCookie_token_key(), null, 0);
		
		// 5. 返回结果
		return ResultInfo.success();
    }
    
}
