package ai.yue.library.data.redis.constant;

/**
 * Redis 常量
 * 
 * @author	ylyue
 * @since	2020年8月25日
 */
public class RedisConstant {

	/**
	 * Redis Key 分隔符
	 */
	public static final String KEY_SEPARATOR = ":";

	/**
	 * Redis Key 前缀
	 */
	public static final String KEY_PREFIX = "yue" + KEY_SEPARATOR;

	/**
	 * Redis 幂等性 Key 前缀
	 */
	public static final String API_IDEMPOTENT_KEY_PREFIX = RedisConstant.standardKey("api_idempotent:");

	/**
	 * 幂等性版本号请求key
	 */
	public static final String API_IDEMPOTENT_VERSION_REQUEST_KEY = "apiIdempotentVersion";

	/**
	 * 规范 Redis Key
	 *
	 * @param key Redis Key
	 * @return 加上yue前缀的key
	 */
	public static String standardKey(String key) {
		return KEY_PREFIX + key;
	}

}
