package ai.yue.library.data.redis.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author	孙金川
 * @since	2018年11月6日
 */
@Data
@ConfigurationProperties("yue.redis.constant")
public class ConstantProperties {
	
	/**
	 * Cookie Token Key
	 * <p>默认：token
	 */
	private String cookie_token_key = "token";
	
	/**
	 * Redis Token 前缀（自定义值，请保留“<code style="color:red">_%s</code>”部分）
	 * <p>默认：token_%s
	 */
	private String redis_token_prefix = "token_%s";
	
	/**
	 * IP前缀（自定义值，请保留“<code style="color:red">_%s</code>”部分）
	 * <p>默认：ip_%s
	 */
	private String ip_prefix = "ip_%s";
	
	/**
	 * Token超时时间（单位：秒）
	 * <p>默认：36000（10小时）
	 */
	private Integer token_timeout = 36000;
	
	/**
	 * 验证码超时时间（单位：秒）
	 * <p>默认：360（6分钟）
	 */
	private Integer captcha_timeout = 360;

}
