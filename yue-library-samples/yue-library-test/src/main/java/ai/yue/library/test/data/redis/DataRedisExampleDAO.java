package ai.yue.library.test.data.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ai.yue.library.data.redis.client.Redis;

/**
 * @author  ylyue
 * @version 创建时间：2019年3月12日
 */
@Repository
public class DataRedisExampleDAO {

	@Autowired
	Redis redis;// 直接注入即可
	
	/**
	 * 示例
	 */
	public void example() {
		String key = "key";
		String value = "value";
		String lockKey = "lockKey";
		long lockTimeout = 3600L;
		
		// 设置值
		redis.set(key, value);
		// 获得值
		redis.get(key);
		// 删除值
		redis.del(key);
		// 分布式锁-加锁
		redis.lock(lockKey, lockTimeout);
		// 分布式锁-解锁
		redis.unlock(lockKey, lockTimeout);
	}
	
}
