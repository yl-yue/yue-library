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
@ConfigurationProperties(RedisProperties.PREFIX)
public class RedisProperties {
	
	/**
	 * Prefix of {@link RedisProperties}.
	 */
	public static final String PREFIX = "yue.redis";
	
	/**
	 * <p>Redis存储对象序列/反序列化器
	 * <p>默认：{@linkplain RedisSerializerEnum#JDK}
	 */
	private RedisSerializerEnum redisSerializer = RedisSerializerEnum.JDK;
	
	/**
	 * IP前缀（自定义值，请保留“<code style="color:red">_%s</code>”部分）
	 * <p>默认：ip_%s
	 */
	private String ipPrefix = "ip_%s";
	
}
