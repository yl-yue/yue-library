package ai.yue.library.auth.service.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 微信开放平台可配置属性
 * 
 * @author	ylyue
 * @since	2018年11月6日
 */
@Data
@ConfigurationProperties(WxOpenProperties.PREFIX)
public class WxOpenProperties {

	/**
	 * Prefix of {@link WxOpenProperties}.
	 */
	public static final String PREFIX = AuthServiceProperties.PREFIX + ".wx.open";
	
	/**
	 * 微信开放平台-appid
	 */
	private String appid;
	
	/**
	 * 微信开放平台-密钥
	 */
	private String secret;
	
}
