package ai.yue.library.data.redis.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author 	 孙金川
 * @version 创建时间：2018年11月6日
 */
@Data
@ConfigurationProperties("yue.user")
public class UserProperties {

	/**
	 * 微信-appid
	 */
	private String wx_appid;
	
	/**
	 * 微信-密钥
	 */
	private String wx_secret;
	
	/**
	 * QQ-appid
	 */
	private String qq_appid;
	
}
