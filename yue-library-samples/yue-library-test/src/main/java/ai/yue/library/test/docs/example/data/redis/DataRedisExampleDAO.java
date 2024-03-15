package ai.yue.library.test.docs.example.data.redis;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.redis.client.Redis;
import ai.yue.library.data.redis.dto.LockInfo;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import org.redisson.Redisson;
import org.redisson.api.RList;
import org.redisson.api.RMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  ylyue
 * @version 创建时间：2019年3月12日
 */
@Repository
public class DataRedisExampleDAO {

	@Autowired
	Redis redis;// 直接注入即可
	
	/**
	 * Redis使用示例
	 */
	public void example() {
		String key = "key";

		// value操作
		redis.get(key);
		redis.set(key, "value");
		redis.delete(key);

		// map操作
		RMap<String, Object> map = redis.getMap(key);
		map.get("mapKey");
		map.put("mapKey", "str");

		// list操作
		RList<Object> list = redis.getList(key);
		list.get(0);
		list.add("str");

		// 有界阻塞队列
		ArrayList<Object> arrayList = new ArrayList<>();
		arrayList.add("1");
		arrayList.add("2");
		redis.addBoundedBlockingQueueValue("queueKey", arrayList);
		List<Object> queueValue = redis.getAndRemoveBoundedBlockingQueueValue("queueKey");

		// 分布式锁
		String lockKey = "lockKey";
		int lockTimeoutMs = 3600;
		LockInfo lock = redis.lock(lockKey, lockTimeoutMs);		// 加锁
		redis.unlock(lock);										// 解锁

		// 获得redisson客户端
		Redisson redisson = redis.getRedisson();
	}

	/**
	 * 对单个用户访问进行加锁，实现接口幂等性等场景示例
	 *
	 * @param userId 用户ID，唯一标识
	 * @return 结果
	 */
	public Result<?> userLock(String userId) {
		String lockKey = Redis.redisKey("userId", userId);
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
