package ai.yue.library.data.redis.client;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>简单Redis</h2>
 * 命令详细说明请参照Redis官方文档进行查阅<br>
 * <a>http://www.redis.net.cn/order/</a>
 * 
 * @author  孙金川
 * @version 创建时间：2018年3月27日
 */
@Slf4j
@AllArgsConstructor
public class Redis {

	StringRedisTemplate stringRedisTemplate;
	
	// Redis分布式锁
	
	/**
	 * 加锁
	 * @param lockKey
	 * @param lockTimeout 当前时间戳 + 超时毫秒
	 * @return
	 */
	public boolean lock(String lockKey, Long lockTimeout) {
		String value = lockTimeout.toString();
		// 1. 设置锁
		if (stringRedisTemplate.opsForValue().setIfAbsent(lockKey, value)) {
			return true;
		}
		// 2. 锁设置失败，拿到当前锁
		String currentValue = stringRedisTemplate.opsForValue().get(lockKey);
		// 3. 判断当前锁是否过期
		if (!StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()) {
			// 4. 锁已过期 ，设置新锁同时得到上一个锁
			String oldValue = stringRedisTemplate.opsForValue().getAndSet(lockKey, value);
			// 5. 确认新锁是否设置成功（判断当前锁与上一个锁是否相等）
			if (!StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue)) {
				// 此处只会有一个线程拿到锁
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 解锁
	 * @param lockKey
	 * @param lockTimeout
	 */
	public void unlock(String lockKey, Long lockTimeout) {
		String value = lockTimeout.toString();
		try {
			String currentValue = stringRedisTemplate.opsForValue().get(lockKey);
			if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
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
	 * @param key
	 * @return
	 */
	public long ttl(String key) {
		return stringRedisTemplate.getExpire(key);
	}
	
	/**
	 * 实现命令：expire 设置过期时间，单位秒
	 * 
	 * @param key
	 * @return
	 */
	public void expire(String key, long timeout) {
		stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
	}
	
	/**
	 * 实现命令：INCR key，将 key 中储存的数字值增一。
	 * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。
	 * 
	 * @param key
	 * @return
	 */
	public long incr(String key, long delta) {
		return stringRedisTemplate.opsForValue().increment(key, delta);
	}

	/**
	 * 实现命令：KEYS pattern，查找所有符合给定模式 pattern的 key
	 * 
	 * @param pattern
	 * @return
	 */
	public Set<String> keys(String pattern) {
		return stringRedisTemplate.keys(pattern);
	}

	/**
	 * 实现命令：DEL key，删除一个key
	 * 
	 * @param key
	 */
	public void del(String key) {
		stringRedisTemplate.delete(key);
	}

	// String（字符串）

	/**
	 * 实现命令：SET key value，设置一个key-value（将字符串值 value关联到 key）
	 * 
	 * @param key
	 * @param value
	 */
	public void set(String key, String value) {
		stringRedisTemplate.opsForValue().set(key, value);
	}

	/**
	 * 实现命令：SET key value EX seconds，设置key-value和超时时间（秒）
	 * 
	 * @param key
	 * @param value
	 * @param timeout （以秒为单位）
	 */
	public void set(String key, String value, long timeout) {
		stringRedisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
	}
	
	/**
	 * 实现命令：SET key value EX seconds，设置key-value和超时时间（秒）
	 * 
	 * @param key
	 * @param value POJO对象
	 * @param timeout （以秒为单位）
	 */
	public void set(String key, Object value, long timeout) {
		stringRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(value), timeout, TimeUnit.SECONDS);
	}
	
	/**
	 * 实现命令：GET key，返回 key所关联的字符串值。
	 * 
	 * @param key
	 * @return value
	 */
	public String get(String key) {
		return stringRedisTemplate.opsForValue().get(key);
	}
	
	// Hash（哈希表）

	/**
	 * 实现命令：HSET key field value，将哈希表 key中的域 field的值设为 value
	 * 
	 * @param key
	 * @param field
	 * @param value
	 */
	public void hset(String key, String field, Object value) {
		stringRedisTemplate.opsForHash().put(key, field, value);
	}
	
	/**
	 * 实现命令：HGET key field，返回哈希表 key中给定域 field的值
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public String hget(String key, String field) {
		return (String) stringRedisTemplate.opsForHash().get(key, field);
	}

	/**
	 * 实现命令：HDEL key field [field ...]，删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
	 * 
	 * @param key
	 * @param fields
	 */
	public void hdel(String key, Object... fields) {
		stringRedisTemplate.opsForHash().delete(key, fields);
	}

	/**
	 * 实现命令：HGETALL key，返回哈希表 key中，所有的域和值。
	 * 
	 * @param key
	 * @return
	 */
	public Map<Object, Object> hgetall(String key) {
		return stringRedisTemplate.opsForHash().entries(key);
	}

	// List（列表）

	/**
	 * 实现命令：LPUSH key value，将一个值 value插入到列表 key的表头
	 * 
	 * @param key
	 * @param value
	 * @return 执行 LPUSH命令后，列表的长度。
	 */
	public long lpush(String key, String value) {
		return stringRedisTemplate.opsForList().leftPush(key, value);
	}

	/**
	 * 实现命令：LPOP key，移除并返回列表 key的头元素。
	 * 
	 * @param key
	 * @return 列表key的头元素。
	 */
	public String lpop(String key) {
		return (String)stringRedisTemplate.opsForList().leftPop(key);
	}

	/**
	 * 实现命令：RPUSH key value，将一个值 value插入到列表 key的表尾(最右边)。
	 * 
	 * @param key
	 * @param value
	 * @return 执行 LPUSH命令后，列表的长度。
	 */
	public long rpush(String key, String value) {
		return stringRedisTemplate.opsForList().rightPush(key, value);
	}
	
}
