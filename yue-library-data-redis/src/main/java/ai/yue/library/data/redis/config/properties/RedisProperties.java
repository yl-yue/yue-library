package ai.yue.library.data.redis.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import ai.yue.library.data.redis.constant.RedisSerializerEnum;
import lombok.Data;

/**
 * redis可配置属性
 * 
 * @author	ylyue
 * @since	2018年11月6日
 */
@Data
@ConfigurationProperties("yue.redis")
public class RedisProperties {
	
	/**
	 * <p>Redis存储对象序列/反序列化器
	 * <p>默认：{@linkplain RedisSerializerEnum#JDK}
	 */
	private RedisSerializerEnum redisSerializerEnum = RedisSerializerEnum.JDK;
	
	/**
	 * Cookie Token Key
	 * <p>默认：token
	 */
	private String cookieTokenKey = "token";
	
	/**
	 * Redis Token 前缀（自定义值，请保留“<code style="color:red">_%s</code>”部分）
	 * <p>默认：token_%s
	 */
	private String redisTokenPrefix = "token_%s";
	
	/**
	 * IP前缀（自定义值，请保留“<code style="color:red">_%s</code>”部分）
	 * <p>默认：ip_%s
	 */
	private String ipPrefix = "ip_%s";
	
	/**
	 * Token超时时间（单位：秒）
	 * <p>默认：36000（10小时）
	 */
	private Integer tokenTimeout = 36000;
	
	/**
	 * 验证码超时时间（单位：秒）
	 * <p>默认：360（6分钟）
	 */
	private Integer captchaTimeout = 360;

}
