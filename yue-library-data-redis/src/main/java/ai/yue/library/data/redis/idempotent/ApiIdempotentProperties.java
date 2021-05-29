package ai.yue.library.data.redis.idempotent;

import ai.yue.library.data.redis.config.properties.RedisProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 幂等性配置属性
 * 
 * @author	ylyue
 * @since	2018年11月6日
 */
@Data
@ConfigurationProperties(ApiIdempotentProperties.PREFIX)
public class ApiIdempotentProperties {
	
	/**
	 * Prefix of {@link ApiIdempotentProperties}.
	 */
	public static final String PREFIX = RedisProperties.PREFIX + "api-idempotent";

	/**
	 * 启用接口幂等性
	 * <p>默认：false</p>
	 */
	private boolean enabled = false;

	/**
	 * 幂等版本号Redis存储失效时间
	 */
	private int versionTimeout = 300;

}
