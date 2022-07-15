package ai.yue.library.auth.client.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import ai.yue.library.auth.client.client.User;
import lombok.Data;

/**
 * Auth可配置属性，适用于auth所有模块的通用可配置属性
 * 
 * @author	ylyue
 * @since	2018年11月6日
 */
@Data
@ConfigurationProperties(AuthProperties.PREFIX)
public class AuthProperties {
	
	/**
	 * Prefix of {@link AuthProperties}.
	 */
	public static final String PREFIX = "yue.auth";
	public static final String PREFIX_REDIS = "yue:auth:";
	
	/**
	 * Cookie Token Key
	 * <p>默认：token
	 */
	private String cookieTokenKey = "token";
	
	/**
	 * Redis Token 前缀（自定义值，请保留“<code style="color:red">:</code>”部分）
	 * <p>默认：token:
	 */
	private String redisTokenPrefix = PREFIX_REDIS + "token:";
	
	/**
	 * Redis Token Value 序列化后的key，反序列化时需使用，如：{@linkplain User#getUserId()}
	 * <p>默认：userId
	 */
	private String userKey = "userId";
	
	/**
	 * IP前缀（自定义值，请保留“<code style="color:red">_%s</code>”部分）
	 * <p>默认：ip_%s
	 */
	private String ipPrefix = "ip_%s";
	
}
