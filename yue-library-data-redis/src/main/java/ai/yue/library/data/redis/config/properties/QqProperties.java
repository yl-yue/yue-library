package ai.yue.library.data.redis.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * QQ
 * @author 	 孙金川
 * @version 创建时间：2018年11月6日
 */
@Data
@ConfigurationProperties("yue.qq")
public class QqProperties {

	/**
	 * QQ-appid
	 */
	private String qq_appid;
	
}
