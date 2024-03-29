package ai.yue.library.base.crypto.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 加解密自动配置属性
 * 
 * @author	ylyue
 * @since	2018年11月6日
 */
@Data
@ConfigurationProperties(CryptoProperties.PREFIX)
public class CryptoProperties {

	/**
	 * Prefix of {@link CryptoProperties}.
	 */
	public static final String PREFIX = "yue.crypto";
	public static final String REDIS_PREFIX = "yue:crypto";

	/**
	 * AES密钥
	 */
	private String aesKeyt;
	
	/**
	 * RSA公钥
	 */
	private String rsaPublicKeyt;
	
	/**
	 * RSA私钥
	 */
	private String rsaPrivateKeyt;

}
