package ai.yue.library.base.crypto.config.properties;

import ai.yue.library.base.crypto.constant.key.exchange.KeyExchangeStorageEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

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

	public static final String[] DEFAULT_WHITE_LIST = {
			"/open/v2.6/keyExchange/**",
			"/actuator/**",
			"/error/**"
	};

	/**
	 * 启用密钥交换
	 * <p>默认：false</p>
	 */
	private boolean enabled = false;

	/**
	 * 启用密钥交换接口
	 * <p>默认：false</p>
	 */
	private boolean enableController = false;

	/**
	 * 使用OAuth2 Token获得交换密钥
	 * <p>开启之后，优先使用OAuth2 Token去查找交换密钥，查找不到才使用{@link #headerNameGetExchangeKey}</p>
	 * <p>默认：true</p>
	 */
	private boolean useAuthTokenGetExchangeKey =  true;

	/**
	 * 使用headerName获得交换密钥
	 * <p>默认：Yue-ExchangeKey-SessionKey</p>
	 */
	private String headerNameGetExchangeKey =  "Yue-ExchangeKey-SessionKey";

	/**
	 * 密钥交换存储类型
	 * <p>默认：LOCAL_CACHE</p>
	 */
	private KeyExchangeStorageEnum keyExchangeStorageType = KeyExchangeStorageEnum.LOCAL_CACHE;

	/**
	 * 缓存过期时间
	 */
	private Duration cacheExpire = Duration.ofHours(24);

	/**
	 * 白名单
	 */
	private List<String> whiteList = new ArrayList<>();

}
