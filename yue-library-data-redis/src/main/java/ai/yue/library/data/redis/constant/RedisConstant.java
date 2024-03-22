package ai.yue.library.data.redis.constant;

/**
 * Redis 常量
 * 
 * @author	ylyue
 * @since	2020年8月25日
 */
public class RedisConstant {

	/**
	 * Redis Key分隔符
	 */
	public static final String KEY_SEPARATOR = ":";

	/**
	 * Redis锁的Key前缀
	 */
	public static final String LOCK_PREFIX = "Lock:";

	/**
	 * Redis幂等性的Key前缀
	 */
	public static final String IDEMPOTENT_PREFIX = "Idempotent:";

	/**
	 * Redis延迟队列Key前缀
	 */
	public static final String DELAYED_QUEUE_PREFIX = "DelayedQueue:";

	/**
	 * Redis有界阻塞队列Key前缀
	 */
	public static final String BOUNDED_BLOCKING_QUEUE_PREFIX = "BoundedBlockingQueue:";

}
