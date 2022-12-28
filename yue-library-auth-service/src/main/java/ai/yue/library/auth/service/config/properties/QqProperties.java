package ai.yue.library.auth.service.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * QQ可配置属性
 * 
 * @author	ylyue
 * @since	2018年11月6日
 */
@Data
@ConfigurationProperties(QqProperties.PREFIX)
public class QqProperties {

	/**
	 * Prefix of {@link QqProperties}.
	 */
	public static final String PREFIX = AuthServiceProperties.PREFIX + ".qq";
	
	/**
	 * QQ-appid
	 */
	private String qqAppid;
	
}
