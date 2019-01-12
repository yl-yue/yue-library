package ai.yue.library.base.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author 	孙金川
 * @version 创建时间：2018年11月6日
 */
@Data
@ConfigurationProperties("yue.constant")
public class ConstantProperties {
	
	/**
	 * Token超时时间（单位：秒）
	 */
	private Integer token_timeout;

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
