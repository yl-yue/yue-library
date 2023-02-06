package ai.yue.library.test.docs.example.data.redis;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.redis.client.Redis;
import ai.yue.library.data.redis.dto.LockInfo;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
		// Redis基本使用
		String key = "key";
		String value = "value";
		// Redis基本使用-设置值
		redis.set(key, value);
		// Redis基本使用-获得值
		redis.get(key);
		// Redis基本使用-删除值
		redis.del(key);

		// 分布式锁
		String lockKey = "lockKey";
		int lockTimeoutMs = 3600;
		// 分布式锁-加锁
		LockInfo lock = redis.lock(lockKey, lockTimeoutMs);
		// 分布式锁-解锁
		redis.unlock(lock);
	}

	/**
	 * 对单个用户访问进行加锁，实现接口幂等性等场景示例
	 *
	 * @param userId 用户ID，唯一标识
	 * @return 结果
	 */
	public Result<?> userLock(String userId) {
		String lockKey = Redis.separatorJoin("userId", userId);
		int lockTimeout = 3600;
		LockInfo lock = redis.lock(lockKey, lockTimeout);
		if (lock.isLock() == false) {
			return R.errorPrompt(StrUtil.format("用户{}未拿到锁", userId));
		}
		// 业务逻辑 ...
		redis.unlock(lock);
		return R.success();
	}

}
