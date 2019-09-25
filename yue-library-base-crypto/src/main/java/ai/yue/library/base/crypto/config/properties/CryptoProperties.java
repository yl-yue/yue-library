package ai.yue.library.base.crypto.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author	ylyue
 * @since	2018年11月6日
 */
@Data
@ConfigurationProperties("yue.crypto")
public class CryptoProperties {
	
	/**
	 * AES密钥
	 */
	private String aes_keyt;
	
	/**
	 * RSA公钥
	 */
	private String rsa_public_keyt;
	
	/**
	 * RSA私钥
	 */
	private String rsa_private_keyt;

}
