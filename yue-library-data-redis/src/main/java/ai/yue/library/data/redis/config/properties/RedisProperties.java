package ai.yue.library.data.redis.config.properties;

import ai.yue.library.data.redis.custom.LockFailureStrategy;
import ai.yue.library.data.redis.custom.LockKeyBuilder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * redis可配置属性
 * 
 * @author	ylyue
 * @since	2018年11月6日
 */
@Data
@ConfigurationProperties(RedisProperties.PREFIX)
public class RedisProperties {
	
	/**
	 * Prefix of {@link RedisProperties}.
	 */
	public static final String PREFIX = "yue.redis";
	
	/**
	 * IP前缀（自定义值，请保留“<code style="color:red">_%s</code>”部分）
	 * <p>默认：ip_%s
	 */
	private String ipPrefix = "ip_%s";

	/**
	 * 幂等锁默认过期时间（单位：毫秒）
	 * <p>超过此时间，如果幂等锁还未被释放，那么锁也将自动失效，解决幂等锁未被正确释放导致的阻塞问题。</p>
	 * <p>此时间应该比大多数接口的执行时间更长，避免锁的提前释放，导致的幂等问题</p>
	 * <p>默认：10秒</p>
	 */
	private int idempotentExpire = 10000;

	/**
	 * 锁
	 */
	private final LockProperties lock = new LockProperties();

	@Data
	public static class LockProperties {

		/**
		 * 过期时间 单位：毫秒
		 */
		private Long expire = 30000L;

		/**
		 * 获取锁超时时间 单位：毫秒
		 */
		private Long acquireTimeout = 3000L;

		/**
		 * 获取锁失败时重试时间间隔 单位：毫秒
		 */
		private Long retryInterval = 100L;

		/**
		 * 默认失败策略，不设置存在多个时默认根据PriorityOrdered、Ordered排序规则选择|注入顺序选择
		 */
		private Class<? extends LockFailureStrategy> primaryFailureStrategy;

		/**
		 * 默认key生成策略，不设置存在多个时默认根据PriorityOrdered、Ordered排序规则选择|注入顺序选择
		 */
		private Class<? extends LockKeyBuilder> primaryKeyBuilder;

		/**
		 * 锁key前缀
		 */
		private String lockKeyPrefix = "Lock";

	}

}
