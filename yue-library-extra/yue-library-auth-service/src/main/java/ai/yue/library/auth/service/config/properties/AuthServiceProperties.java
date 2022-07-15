package ai.yue.library.auth.service.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * AuthService可配置属性
 * 
 * @author	ylyue
 * @since	2018年11月6日
 */
@Data
@ConfigurationProperties(AuthServiceProperties.PREFIX)
public class AuthServiceProperties {
	
	/**
	 * Prefix of {@link AuthServiceProperties}.
	 */
	public static final String PREFIX = "yue.auth.service";
	
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
