package ai.yue.library.data.redis.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * QQ可配置属性
 * 
 * @author	ylyue
 * @since	2018年11月6日
 */
@Data
@ConfigurationProperties("yue.redis.qq")
public class QqProperties {

	/**
	 * QQ-appid
	 */
	private String qq_appid;
	
}
