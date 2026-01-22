package ai.yue.library.base.crypto.config.properties;

import ai.yue.library.base.crypto.annotation.RequestDecrypt;
import ai.yue.library.base.crypto.annotation.ResponseEncrypt;
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

	/**
	 * 启用 {@link RequestDecrypt} 注解
	 * <p>默认：false</p>
	 */
	private boolean enableRequestDecryptAnnotation = false;

	/**
	 * 启用 {@link ResponseEncrypt} 注解
	 * <p>默认：false</p>
	 */
	private boolean enableResponseEncryptAnnotation = false;

}
