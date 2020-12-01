package ai.yue.library.test.doc.example.data.redis;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.redis.client.Redis;
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

	/**
	 * 对单个用户访问进行加锁，实现接口幂等性等场景示例
	 *
	 * @param userId 用户ID，唯一标识
	 * @return 结果
	 */
	public Result<?> userLock(String userId) {
		String lockKey = Redis.separatorJoin("lock:userId", userId);
		long lockTimeout = 3600L;
		boolean lock = redis.lock(lockKey, lockTimeout);
		if (!lock) {
			return R.errorPrompt(StrUtil.format("用户{}未拿到锁", userId));
		}
		// 业务逻辑 ...
		redis.unlock(lockKey, lockTimeout);
		return R.success();
	}

}
