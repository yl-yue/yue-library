package ai.yue.library.base.crypto.config.properties;

import ai.yue.library.base.crypto.constant.key.exchange.KeyExchangeStorageEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 密钥交换属性配置
 * 
 * @author	ylyue
 * @since	2018年11月6日
 */
@Data
@ConfigurationProperties(KeyExchangeProperties.PREFIX)
public class KeyExchangeProperties {

	/**
	 * Prefix of {@link KeyExchangeProperties}.
	 */
	public static final String PREFIX = CryptoProperties.PREFIX + ".key-exchange";

	/**
	 * 启用密钥交换
	 * <p>默认：false</p>
	 */
	private boolean enabled = false;

	/**
	 * 密钥交换存储类型
	 * <p>默认：LOCAL_MAP</p>
	 */
	private KeyExchangeStorageEnum keyExchangeStorageType = KeyExchangeStorageEnum.LOCAL_MAP;

}
