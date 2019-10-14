package ai.yue.library.base.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @deprecated 见 yue-library-data-redis 模块 ConfigProperties
 * 
 * @author	ylyue
 * @since	2018年11月6日
 */
@Data
@Deprecated
@ConfigurationProperties("yue.constant")
public class ConstantProperties {
	
	/**
	 * Token超时时间（单位：秒）
	 * <p>默认：36000（10小时）
	 */
	private Integer token_timeout = 36000;
	
	/**
	 * 验证码超时时间（单位：秒）
	 * <p>默认：360（6分钟）
	 */
	private Integer captcha_timeout = 360;
	
	/**
	 * AES密钥
	 * 
	 * @deprecated 请使用 yue-library-base-crypto 模块 CryptoProperties
	 */
	@Deprecated
	private String aes_keyt;
	
	/**
	 * RSA公钥
	 * 
	 * @deprecated 请使用 yue-library-base-crypto 模块 CryptoProperties
	 */
	@Deprecated
	private String rsa_public_keyt;
	
	/**
	 * RSA私钥
	 * 
	 * @deprecated 请使用 yue-library-base-crypto 模块 CryptoProperties
	 */
	@Deprecated
	private String rsa_private_keyt;

}
