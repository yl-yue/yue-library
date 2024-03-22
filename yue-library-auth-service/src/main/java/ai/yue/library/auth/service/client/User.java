package ai.yue.library.auth.service.client;

import ai.yue.library.auth.client.config.properties.AuthProperties;
import ai.yue.library.auth.service.config.properties.AuthServiceProperties;
import ai.yue.library.auth.service.config.properties.QqProperties;
import ai.yue.library.auth.service.config.properties.WxOpenProperties;
import ai.yue.library.auth.service.dto.QqUserDTO;
import ai.yue.library.auth.service.dto.WxUserDTO;
import ai.yue.library.auth.service.vo.wx.open.AccessTokenVO;
import ai.yue.library.base.exception.ResultException;
import ai.yue.library.base.util.StrUtils;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.ResultPrompt;
import ai.yue.library.web.ipo.CaptchaIPO;
import ai.yue.library.web.util.CaptchaUtils;
import ai.yue.library.web.util.ServletUtils;
import ai.yue.library.web.vo.CaptchaVO;
import com.alibaba.fastjson2.JSONObject;
import com.dtflys.forest.Forest;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.util.UUID;

/**
 * <b>User客户端</b>
 * <p>登录登出、第三方登录、token自动解析获取用户信息、分布式验证码
 * 
 * @author	ylyue
 * @since	2018年4月24日
 */
@NoArgsConstructor
public class User extends ai.yue.library.auth.client.client.User {
	
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private AuthServiceProperties authServiceProperties;
	@Autowired
	private WxOpenProperties wxOpenProperties;
	@Autowired
	private QqProperties qqProperties;
	
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
		AccessTokenVO accessTokenVO = Forest.get(WX_URI_ACCESS_TOKEN).addQuery(paramJson).execute(AccessTokenVO.class);
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
		WxUserDTO wxUserDTO = Forest.get(WX_URI_USER_INFO).addQuery(paramJson).execute(WxUserDTO.class);
		if (null == wxUserDTO.getOpenid()) {
			throw new ResultException(R.requestError(wxUserDTO));
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
		paramJson.put("oauth_consumer_key", qqProperties.getQqAppid());
		paramJson.put("access_token", access_token);
		paramJson.put("openid", openid);
		QqUserDTO qqUserDTO = Forest.get(QQ_URI_USER_INFO).addQuery(paramJson).execute(QqUserDTO.class);
		if (null == qqUserDTO.getGender()) {
			throw new ResultException(R.requestError(qqUserDTO));
		}

		return qqUserDTO;
	}
    
    /**
     * 获得-验证码图片（基于redis解决分布式验证的问题）
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
		redis.set(captcha_redis_key, captcha, Duration.ofSeconds(authServiceProperties.getCaptchaTimeout()) );
		
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
     * 验证-验证码（基于redis解决分布式验证的问题）
     * <p>验证码错误会抛出一个{@linkplain ResultException}异常，作为结果提示...<br>
     * 
     * @param captcha 验证码
     * @throws ResultException 验证码错误
     */
    public void captchaValidate(String captcha) {
    	String captcha_redis_key = String.format(CaptchaUtils.CAPTCHA_REDIS_PREFIX, captcha);
		String randCaptcha = redis.get(captcha_redis_key);
		if (StrUtils.isEmpty(randCaptcha) || !randCaptcha.equalsIgnoreCase(captcha)) {
			throw new ResultException(R.errorPrompt(ResultPrompt.CAPTCHA_ERROR));
		}
		
		redis.delete(captcha_redis_key);
    }
    
	/**
	 * 登录
	 * <p>登录成功-设置token至Cookie
	 * <p>登录成功-设置token至Header
	 * <p><code style="color:red"><b>注意：登录之后的所有相关操作，都是基于请求报文中所携带的token，若Cookie与Header皆没有token或Redis中匹配不到值，将视为未登录状态
	 * </b></code>
	 * 
	 * @param userInfo 用户信息（必须包含：<code style="color:red">{@linkplain AuthProperties#getUserKey()}，默认：userId，可通过配置文件进行配置</code>）
	 * @return <b>token</b> 身份认证令牌 <code style="color:red"><b>（不建议使用，最好是忽略这个返回值，哪怕你只是将他放在响应体里面，也不推荐这样做）</b></code>
	 * <p>支持Cookie：建议使用默认的机制即可
	 * <p>不支持Cookie：建议从响应Header中获取token，之后的请求都将token放入请求Header中即可
	 */
	public String login(Object userInfo) {
		// 1. 获得请求token
		String token = getRequestToken();
		
        // 2. 注销会话
		String redisTokenKey = null;
        if (StrUtils.isNotEmpty(token)) {
        	redisTokenKey = authProperties.getRedisTokenPrefix() + token;
        	String tokenValue = redis.get(redisTokenKey);
        	if (StrUtils.isNotEmpty(tokenValue)) {
        		redis.delete(redisTokenKey);
        	}
        }
        
        // 3. 生成新的token
        token = UUID.randomUUID().toString();
        redisTokenKey = authProperties.getRedisTokenPrefix() + token;
        
		// 4. 登录成功-设置token至Redis
		Integer tokenTimeout = authServiceProperties.getTokenTimeout();
		redis.set(redisTokenKey, JSONObject.toJSONString(userInfo), Duration.ofSeconds(tokenTimeout));
		
		// 5. 登录成功-设置token至Cookie
		ServletUtils.addCookie(response, authProperties.getCookieTokenKey(), token, tokenTimeout);
		
		// 6. 登录成功-设置token至Header
		response.setHeader(authProperties.getCookieTokenKey(), token);
		
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
		if (StrUtils.isEmpty(token)) {
			return R.unauthorized();
		}
		
		// 3. 清除Redis-token
		redis.delete(authProperties.getRedisTokenPrefix() + token);
		
		// 4. 清除Cookie-token
		ServletUtils.addCookie(response, authProperties.getCookieTokenKey(), null, 0);
		
		// 5. 返回结果
		return R.success();
    }
    
}
