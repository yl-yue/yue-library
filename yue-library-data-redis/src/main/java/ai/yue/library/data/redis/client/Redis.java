package ai.yue.library.data.redis.client;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.util.DateUtils;
import ai.yue.library.base.util.StringUtils;
import ai.yue.library.data.redis.constant.RedisConstant;
import ai.yue.library.data.redis.dto.LockInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <h2>简单Redis</h2>
 * 命令详细说明请参照 Redis <a href="http://www.redis.net.cn/order">官方文档</a> 进行查阅
 * 
 * @author	ylyue
 * @since	2018年3月27日
 */
@Slf4j
@Data
@AllArgsConstructor
public class Redis {

	RedisTemplate<String, Object> redisTemplate;
	StringRedisTemplate stringRedisTemplate;
	
	// Redis分布式锁

	/**
	 * Redis分布式锁-加锁
	 * <p>可用于实现接口幂等性、秒杀、库存加锁等业务场景需求
	 * <p>注意：服务器集群间需进行时间同步，保障分布式锁的时序性</p>
	 *
	 * @param lockKey       分布式锁的key（全局唯一性）
	 * @param lockTimeoutMs 分布式锁的超时时间（单位：毫秒），到期后锁将自动超时
	 * @return 是否成功拿到锁
	 */
	public LockInfo lock(String lockKey, Integer lockTimeoutMs) {
		// 1. 设置锁
		String redisLockKey = RedisConstant.LOCK_KEY_PREFIX + lockKey;
		String lockTimeoutStamp = String.valueOf(DateUtils.getTimestamp(lockTimeoutMs));
		LockInfo lockInfo = new LockInfo();
		lockInfo.setLockKey(redisLockKey);
		lockInfo.setLockTimeoutMs(lockTimeoutMs);
		lockInfo.setLockTimeoutStamp(lockTimeoutStamp);
		if (stringRedisTemplate.opsForValue().setIfAbsent(redisLockKey, lockTimeoutStamp, lockTimeoutMs, TimeUnit.MILLISECONDS)) {
			lockInfo.setLock(true);
			return lockInfo;
		}

		// 2. 锁设置失败，拿到当前锁
		String currentValue = stringRedisTemplate.opsForValue().get(redisLockKey);
		// 3. 判断当前锁是否过期
		if (!StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()) {
			// 4. 锁已过期 ，设置新锁同时得到上一个锁
			String oldValue = stringRedisTemplate.opsForValue().getAndSet(redisLockKey, lockTimeoutStamp);
			// 5. 确认新锁是否设置成功（判断当前锁与上一个锁是否相等）
			if (!StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue)) {
				// 此处只会有一个线程拿到锁
				lockInfo.setLock(true);
				return lockInfo;
			}
		}

		lockInfo.setLock(false);
		return lockInfo;
	}

	/**
	 * Redis分布式锁-解锁
	 *
	 * @param lockInfo 加锁时返回的锁对象
	 */
	public void unlock(LockInfo lockInfo) {
		String lockKey = lockInfo.getLockKey();
		String lockTimeoutStamp = lockInfo.getLockTimeoutStamp();

		try {
			String currentValue = stringRedisTemplate.opsForValue().get(lockKey);
			if (StringUtils.isNotEmpty(currentValue) && currentValue.equals(lockTimeoutStamp)) {
				stringRedisTemplate.opsForValue().getOperations().delete(lockKey);
			}
		} catch (Exception e) {
			log.error("【redis分布式锁】解锁异常，{}", e);
		}
	}
	
	// Key（键），简单的key-value操作
	
	/**
	 * 实现命令：TTL key，以秒为单位，返回给定 key的剩余生存时间(TTL, time to live)。
	 * 
	 * @param key key
	 * @return key的剩余生存时间（单位：秒）
	 */
	public long ttl(String key) {
		return stringRedisTemplate.getExpire(key);
	}
	
	/**
	 * 实现命令：expire 设置过期时间，单位秒
	 * 
	 * @param key key
	 * @param timeout 过期时间（单位：秒）
	 */
	public void expire(String key, long timeout) {
		stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
	}
	
	/**
	 * 实现命令：INCR key，将 key 中储存的数字值按增量递增。
	 * 
	 * @param key 不能为空
	 * @param delta 增量数字
	 * @return 递增后的值
	 */
	public long incr(String key, long delta) {
		return stringRedisTemplate.opsForValue().increment(key, delta);
	}
	
	/**
	 * 实现命令：KEYS pattern，查找所有符合给定模式 pattern的 key
	 * 
	 * @param pattern 不能为空
	 * @return keys
	 */
	public Set<String> keys(String pattern) {
		return stringRedisTemplate.keys(pattern);
	}
	
	/**
	 * 实现命令：DEL key，删除一个key
	 *
	 * @param key 不能为空
	 * @return
	 */
	public Boolean del(String key) {
		return stringRedisTemplate.delete(key);
	}
	
	// get set ...
	
	/**
	 * 实现命令：SET key value，设置一个key-value（将字符串对象 value 关联到 key）
	 * 
	 * @param key 不能为空
	 * @param value 字符串对象
	 */
	public void set(String key, String value) {
		stringRedisTemplate.opsForValue().set(key, value);
	}
	
	/**
	 * 实现命令：SET key value，设置一个key-value（将可序列化对象 value 关联到 key）
	 * 
	 * @param key 不能为空
	 * @param value 可序列化对象
	 */
	public void set(String key, Object value) {
		redisTemplate.opsForValue().set(key, value);
	}
	
	/**
	 * 实现命令：SET key value EX seconds，设置key-value和超时时间（秒）
	 * 
	 * @param key 不能为空
	 * @param value 可序列化对象
	 * @param timeout 超时时间（单位：秒）
	 */
	public void set(String key, Object value, long timeout) {
		redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
	}
	
	/**
	 * 实现命令：SET key value EX seconds，设置key-value和超时时间（秒）
	 * 
	 * @param key 不能为空
	 * @param value 字符串对象
	 * @param timeout 超时时间（单位：秒）
	 */
	public void set(String key, String value, long timeout) {
		stringRedisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
	}
	
	/**
	 * 实现命令：GET key，返回 key所关联的字符串值。
	 * 
	 * @param key 不能为空
	 * @return value
	 */
	public String get(String key) {
		return stringRedisTemplate.opsForValue().get(key);
	}
	
	/**
	 * 实现命令：GET key，返回 key 所关联的对象。
	 * 
	 * @param key 不能为空
	 * @return 对象
	 */
	public Object getObject(String key) {
		return redisTemplate.opsForValue().get(key);
	}
	
	/**
	 * 实现命令：GET key，返回 key 所关联的反序列化对象。
	 * 
	 * @param <T> 反序列化对象类型
	 * @param key 不能为空
	 * @param clazz 反序列化对象类
	 * @return 反序列化对象
	 */
	public <T> T get(String key, Class<T> clazz) {
		return Convert.convert(redisTemplate.opsForValue().get(key), clazz);
	}
	
	// Hash（哈希表）
	
	/**
	 * 实现命令：HSET key field value，将哈希表 key中的域 field的值设为 value
	 * <p>设置hashKey的值
	 * 
	 * @param key 不能为空
	 * @param hashKey 不能为空
	 * @param value 设置的值
	 */
	public void hset(String key, String hashKey, Object value) {
		redisTemplate.opsForHash().put(key, hashKey, value);
	}
	
	/**
	 * 实现命令：HGET key field，返回哈希表 key中给定域 field的值
	 * <p>从hashKey获取值
	 * 
	 * @param key 不能为空
	 * @param hashKey 不能为空
	 * @return hashKey的值
	 */
	public Object hget(String key, String hashKey) {
		return redisTemplate.opsForHash().get(key, hashKey);
	}
	
	/**
	 * 实现命令：HGET key field，返回哈希表 key中给定域 field的值
	 * <p>从hashKey获取值
	 * 
	 * @param <T> 反序列化对象类型
	 * @param key 不能为空
	 * @param hashKey 不能为空
	 * @param clazz 反序列化对象类
	 * @return hashKey的反序列化对象
	 */
	public <T> T hget(String key, String hashKey, Class<T> clazz) {
		return Convert.convert(redisTemplate.opsForHash().get(key, hashKey), clazz);
	}
	
	/**
	 * 实现命令：HDEL key field [field ...]，删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
	 * <p>删除给定的hashKeys
	 * 
	 * @param key 不能为空
	 * @param hashKeys 不能为空
	 */
	public void hdel(String key, Object... hashKeys) {
		redisTemplate.opsForHash().delete(key, hashKeys);
	}
	
	/**
	 * 实现命令：HGETALL key，返回哈希表 key中，所有的域和值。
	 * <p>获取存储在键上的整个散列
	 * 
	 * @param key 不能为空
	 * @return map
	 */
	public Map<Object, Object> hgetall(String key) {
		return redisTemplate.opsForHash().entries(key);
	}
	
	// List（列表）
	
	/**
	 * 实现命令：LPUSH key value，将一个值 value插入到列表 key的表头
	 * 
	 * @param key 不能为空
	 * @param value 插入的值
	 * @return 执行 LPUSH命令后，列表的长度。
	 */
	public long lpush(String key, String value) {
		return stringRedisTemplate.opsForList().leftPush(key, value);
	}
	
	/**
	 * 实现命令：RPUSH key value，将一个值 value插入到列表 key的表尾(最右边)。
	 * 
	 * @param key 不能为空
	 * @param value 插入的值
	 * @return 执行 LPUSH命令后，列表的长度。
	 */
	public long rpush(String key, String value) {
		return stringRedisTemplate.opsForList().rightPush(key, value);
	}
	
	/**
	 * 实现命令：LPOP key，移除并返回列表 key的头元素。
	 * 
	 * @param key 不能为空
	 * @return 列表key的头元素。
	 */
	public String lpop(String key) {
		return stringRedisTemplate.opsForList().leftPop(key);
	}
	
	// static
	
    /**
     * Redis分割Key拼接，参考 {@linkplain String#join(CharSequence, CharSequence...)}
     * 
     * <blockquote>示例：
     * <pre>
     * {@code
     *     String message = Redis.join("Java", "is", "cool");
     *     // message returned is: "Java:is:cool"
     * }
     * </pre>
     * </blockquote>
     * 
     * 注意，如果元素为null，则添加 {@code "null"}。
     * 
     * @param  elements 要连接在一起的元素。
     * @return 由Redis Key分隔符分隔的元素组成的新字符串
     */
	public static String separatorJoin(String... elements) {
		return String.join(RedisConstant.KEY_SEPARATOR, elements);
	}
	
}
