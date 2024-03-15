package ai.yue.library.data.redis.client;

import ai.yue.library.base.util.DateUtils;
import ai.yue.library.base.util.StrUtils;
import ai.yue.library.data.redis.constant.RedisConstant;
import ai.yue.library.data.redis.dto.LockInfo;
import ai.yue.library.data.redis.dto.LockMapInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.*;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <b>简单Redis</b>
 * <p>封装或重载常用的方法，更丰富的实现请使用{@link #getRedisson()}</p>
 * <p>命令详细说明请参照 Redis <a href="http://www.redis.net.cn/order">官方文档</a> 进行查阅</p>
 *
 * @author	ylyue
 * @since	2018年3月27日
 */
@Slf4j
@Data
@AllArgsConstructor
public class Redis {

	Redisson redisson;

	// ======Redis分布式锁======

	/**
	 * Redis分布式锁-加锁
	 * <p>简单redis锁满足常规使用场景，此锁特性：线程安全、不阻塞线程、防止死锁、不可重入、非公平锁</p>
	 * <p>可用于实现接口幂等性、秒杀、库存加锁等业务场景需求</p>
	 * <p>注意：服务器集群间需进行时间同步，保障分布式锁的时序性</p>
	 * <p>可重入锁、公平锁请使用{@link #getRedisson()}</p>
	 *
	 * @param lockKey       分布式锁的key（全局唯一性）
	 * @param lockTimeoutMs 分布式锁的超时时间（单位：毫秒），到期后锁将自动超时
	 * @return 是否成功拿到锁
	 */
	public synchronized LockInfo lock(String lockKey, Integer lockTimeoutMs) {
		// 1. 设置锁
		String redisLockKey = RedisConstant.LOCK_KEY_PREFIX + lockKey;
		String lockTimeoutStamp = String.valueOf(DateUtils.getTimestamp(lockTimeoutMs));
		LockInfo lockInfo = new LockInfo();
		lockInfo.setLockKey(redisLockKey);
		lockInfo.setLockTimeoutMs(lockTimeoutMs);
		lockInfo.setLockTimeoutStamp(lockTimeoutStamp);
		RBucket<String> bucket = redisson.getBucket(redisLockKey);
		if (bucket.setIfAbsent(lockTimeoutStamp, Duration.ofMillis(lockTimeoutMs))) {
			lockInfo.setLock(true);
			return lockInfo;
		}

		// 2. 锁设置失败，拿到当前锁
		String currentValue = bucket.get();
		// 3. 判断当前锁是否过期
		if (!StrUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()) {
			// 4. 锁已过期 ，设置新锁同时得到上一个锁
			String oldValue = bucket.getAndSet(lockTimeoutStamp, Duration.ofMillis(lockTimeoutMs));
			// 5. 确认新锁是否设置成功（判断当前锁与上一个锁是否相等）
			if (StrUtils.isNotEmpty(oldValue) && oldValue.equals(currentValue)) {
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
			RBucket<String> bucket = redisson.getBucket(lockKey);
			String currentValue = bucket.get();
			if (StrUtils.isNotEmpty(currentValue) && currentValue.equals(lockTimeoutStamp)) {
				bucket.delete();
			}
		} catch (Exception e) {
			log.error("【redis分布式锁】解锁异常，{}", e);
		}
	}

	/**
	 * Redis分布式锁-加锁（使用Map数据结构存储锁）
	 * <p>简单redis锁满足常规使用场景，此锁特性：线程安全、不阻塞线程、防止死锁、不可重入、非公平锁</p>
	 * <p>可用于实现接口幂等性、秒杀、库存加锁等业务场景需求</p>
	 * <p>注意：服务器集群间需进行时间同步，保障分布式锁的时序性</p>
	 * <p>可重入锁、公平锁请使用{@link #getRedisson()}</p>
	 *
	 * @param redisKey      分布式锁的redisKey（redisKey + mapKey = 全局唯一性）
	 * @param mapKey        分布式锁的mapKey（redisKey + mapKey = 全局唯一性）
	 * @param lockTimeoutMs 分布式锁的超时时间（单位：毫秒），到期后锁将自动超时
	 * @return 是否成功拿到锁
	 */
	public synchronized <K> LockMapInfo lockMap(String redisKey, K mapKey, Integer lockTimeoutMs) {
		// 1. 设置锁
		redisKey = RedisConstant.LOCK_KEY_PREFIX + redisKey;
		String lockTimeoutStamp = String.valueOf(DateUtils.getTimestamp(lockTimeoutMs));
		LockMapInfo<K> lockMapInfo = new LockMapInfo();
		lockMapInfo.setRedisKey(redisKey);
		lockMapInfo.setMapKey(mapKey);
		lockMapInfo.setLockTimeoutMs(lockTimeoutMs);
		lockMapInfo.setLockTimeoutStamp(lockTimeoutStamp);
		RMapCache<K, String> mapCache = redisson.getMapCache(redisKey);
		if (mapCache.fastPutIfAbsent(mapKey, lockTimeoutStamp, lockTimeoutMs, TimeUnit.MILLISECONDS)) {
			lockMapInfo.setLock(true);
			return lockMapInfo;
		}

		// 2. 锁设置失败，拿到当前锁
		String currentValue = mapCache.get(mapKey);
		// 3. 判断当前锁是否过期
		if (!StrUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()) {
			// 4. 锁已过期 ，设置新锁同时得到上一个锁
			String oldValue = mapCache.put(mapKey, lockTimeoutStamp, lockTimeoutMs, TimeUnit.MILLISECONDS);
			// 5. 确认新锁是否设置成功（判断当前锁与上一个锁是否相等）
			if (StrUtils.isNotEmpty(oldValue) && oldValue.equals(currentValue)) {
				// 此处只会有一个线程拿到锁
				lockMapInfo.setLock(true);
				return lockMapInfo;
			}
		}

		lockMapInfo.setLock(false);
		return lockMapInfo;
	}

	/**
	 * Redis分布式锁-解锁（使用Map数据结构存储锁）
	 *
	 * @param lockMapInfo 加锁时返回的map锁对象
	 */
	public <K> void unlockMap(LockMapInfo<K> lockMapInfo) {
		String redisKey = lockMapInfo.getRedisKey();
		K mapKey = lockMapInfo.getMapKey();
		String lockTimeoutStamp = lockMapInfo.getLockTimeoutStamp();

		try {
			RMapCache<K, String> mapCache = redisson.getMapCache(redisKey);
			String currentValue = mapCache.get(mapKey);
			if (StrUtils.isNotEmpty(currentValue) && currentValue.equals(lockTimeoutStamp)) {
				mapCache.remove(mapKey);
			}
		} catch (Exception e) {
			log.error("【redis分布式锁】解锁异常，{}", e);
		}
	}

	// ======value操作======

	/**
	 * 获取value对象
	 * <p>对象的最大大小为512MB</p>
	 */
	public <V> RBucket<V> getBucket(String key) {
		return redisson.getBucket(key);
	}

	/**
	 * 检查对象是否存在
	 */
	public boolean isExists(@NotNull String key) {
		return getBucket(key).isExists();
	}

	/**
	 * 获取值
	 */
	public <V> V get(@NotNull String key) {
		return (V) getBucket(key).get();
	}

	/**
	 * 设置值
	 *
	 * @param key   不能为空
	 * @param value 可序列化对象
	 */
	public <V> void set(String key, V value) {
		getBucket(key).set(value);
	}

	/**
	 * 设置值
	 *
	 * @param key     不能为空
	 * @param value   可序列化对象
	 * @param timeout 超时时间
	 */
	public <V> void set(String key, V value, Duration timeout) {
		getBucket(key).set(value, timeout);
	}

	/**
	 * 删除对象
	 * <p>删除redis中的整个key，如果这个key是map或list数据结构，那么将删除的是整个map或list</p>
	 */
	public Boolean delete(@NotNull String key) {
		return getBucket(key).delete();
	}

	// ======map操作======

	/**
	 * 获取map对象
	 * <p>不允许将null存储为键或值</p>
	 */
	public <K, V> RMap<K, V> getMap(String key) {
		return redisson.getMap(key);
	}

	/**
	 * 获取map值
	 *
	 * @param key    不能为空
	 * @param mapKey 不能为空
	 */
	public <K, V> V getMapValue(String key, K mapKey) {
		return (V) getMap(key).get(mapKey);
	}

	/**
	 * 添加map值
	 *
	 * @param key      不能为空
	 * @param mapKey   不能为空
	 * @param mapValue 设置的值
	 */
	public <K, V> void addMapValue(String key, K mapKey, V mapValue) {
		getMap(key).put(mapKey, mapValue);
	}

	/**
	 * 删除map值
	 *
	 * @param key     不能为空
	 * @param mapKeys 不能为空
	 */
	public <K> long removeMapValue(String key, K... mapKeys) {
		return getMap(key).fastRemove(mapKeys);
	}

	// ======List操作======

	/**
	 * 获取list对象
	 */
	public <V> RList<V> getList(String key) {
		return redisson.getList(key);
	}

	/**
	 * 获取list值
	 *
	 * @param key   不能为空
	 * @param index 索引值
	 */
	public <V> V getListValue(String key, int index) {
		return (V) getList(key).get(index);
	}

	/**
	 * 获取list值
	 *
	 * @param key     不能为空
	 * @param indexes 索引值
	 */
	public <V> List<V> getListValue(String key, int... indexes) {
		return (List<V>) getList(key).get(indexes);
	}

	/**
	 * 添加list值
	 *
	 * @param key       不能为空
	 * @param listValue 设置的值
	 */
	public <V> boolean addListValue(String key, V listValue) {
		return getList(key).add(listValue);
	}

	/**
	 * 添加list值
	 *
	 * @param key       不能为空
	 * @param listValue 设置的值
	 */
	public <V> boolean addListValue(String key, Collection<V> listValue) {
		return getList(key).addAll(listValue);
	}

	/**
	 * 删除list值
	 *
	 * @param key   不能为空
	 * @param index 索引值
	 */
	public <V> V removeListValue(String key, int index) {
		return (V) getList(key).remove(index);
	}

	/**
	 * ======list有界阻塞队列操作======
	 * 建议在非必要的情况下，都使用有界阻塞队列操作，避免队列未消费，导致redis内存暴增
	 * redis属于轻量级分布式队列，如果需要更加健壮可靠的队列操作，请根据业务需求使用：RabbitMQ、RocketMQ、Kafka等专业队列中间件实现
	 */

	/**
	 * 获得list有界阻塞队列
	 * <p>建议在非必要的情况下，都使用有界阻塞队列操作，避免队列未消费，导致redis内存暴增</p>
	 * <p>redis属于轻量级分布式队列，如果需要更加健壮可靠的队列操作，请根据业务需求使用：RabbitMQ、RocketMQ、Kafka等专业队列中间件实现</p>
	 */
	public <V> RBlockingQueue<V> getBoundedBlockingQueue(String key) {
		/**
		 * https://github.com/redisson/redisson/issues/3020
		 * RBoundedBlockingQueue有界阻塞队列有bug，capacity值只减不增，导致队列最终可用容量为0，无法继续插入数据
		 */
		return redisson.getBlockingQueue(key);
	}

	/**
	 * 获取并删除list有界阻塞队列值
	 * <p>每次最大消费1000条数据</p>
	 *
	 * @param key redis key
	 */
	public <V> List<V> getAndRemoveBoundedBlockingQueueValue(String key) {
		return getAndRemoveBoundedBlockingQueueValue(key, 1000);
	}

	/**
	 * 获取并删除list有界阻塞队列值
	 *
	 * @param key         redis key
	 * @param consumeSize 一次消费多少条数据
	 */
	public <V> List<V> getAndRemoveBoundedBlockingQueueValue(String key, int consumeSize) {
		RBlockingQueue<V> queue = getBoundedBlockingQueue(key);
		return queue.poll(consumeSize);
	}

	/**
	 * 添加list有界阻塞队列值
	 * <p>队列大小限制为100000，超过丢弃</p>
	 *
	 * @param key  redis key
	 * @param list list值
	 */
	public <V> boolean addBoundedBlockingQueueValue(String key, List<V> list) {
		return addBoundedBlockingQueueValue(key, list, 100000);
	}

	/**
	 * 添加list有界阻塞队列值
	 *
	 * @param key       redis key
	 * @param list      list值
	 * @param blockSize 队列最大大小，队列满后，继续添加的数据将被直接丢弃
	 */
	public <V> boolean addBoundedBlockingQueueValue(String key, List<V> list, int blockSize) {
		RBlockingQueue<V> queue = getBoundedBlockingQueue(key);
		int size = queue.size();
		if (size >= blockSize) {
			return false;
		}
		return queue.addAll(list);
	}

	// util
	
    /**
	 * <b>“:”分割的字符串连接，组成符合redis规范的key</b>
	 *
	 * <p>示例：{@code redisKeyJoin("Java", "is", "cool") == "Java:is:cool"}</p>
     *
     * @param elements 要连接在一起的字符串
     * @return {@code ":"}分隔的redis规范key
     */
	public static String redisKey(String... elements) {
		return String.join(RedisConstant.KEY_SEPARATOR, elements);
	}

	public String redisKeyJoin(String... elements) {
		return Redis.redisKey(elements);
	}

}
