package ai.yue.library.test.controller.other.docs.example.data.redis;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.redis.annotation.Lock;
import ai.yue.library.data.redis.client.Redis;
import ai.yue.library.data.redis.instance.BoundedBlockingQueue;
import ai.yue.library.data.redis.instance.DelayedQueue;
import ai.yue.library.data.redis.instance.LockInfo;
import ai.yue.library.test.entity.TableExampleStandard;
import ai.yue.library.test.service.TableExampleStandardService;
import cn.hutool.core.util.StrUtil;
import org.redisson.Redisson;
import org.redisson.api.RList;
import org.redisson.api.RMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author  ylyue
 * @version 创建时间：2019年3月12日
 */
@Repository
public class DataRedisExample {

	@Autowired
	Redis redis;// 直接注入即可
	@Autowired
	TableExampleStandardService tableExampleService;

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
		// 准备参数
		String lockKey = Redis.redisKey("userId", userId);
		int lockTimeout = 3600;

		// 加锁
		LockInfo lock = redis.lock(lockKey, lockTimeout);
		if (lock.isLock() == false) {
			return R.errorPrompt(StrUtil.format("用户{}未拿到锁", userId));
		}

		// 业务逻辑 ...

		// 解锁
		redis.unlock(lock);
		return R.success();
	}

//	// 全局锁住此方法
//	@Lock
//	// 基于参数锁住此方法
//	@Lock(keys = "#lockIPO.id", acquireTimeout = 15000, expire = 1000)
//	// 基于bean值锁住此方法
//	@Lock(keys = {"@lockBeanIPO.id", "@lockBeanIPO.name"}, acquireTimeout = 5000, expire = 5000)

	@Caching(
			cacheable = {
					@Cacheable(value = "cache_test", key = "#id")
			},
			put = {
					@CachePut(value = "cache_test", key = "#result.cellphone", condition = "#result != null"),
					@CachePut(value = "cache_test", key = "#result.email", condition = "#result != null")
			}
	)
	public TableExampleStandard getExample(Long id) {
		return tableExampleService.getById(id);
	}

	/**
	 * 有界阻塞队列
	 */
	public void boundedBlockingQueue() {
		// 准备参数
		List<String> list = new ArrayList<>();
		list.add("1");
		list.add("2");
		BoundedBlockingQueue<String> boundedBlockingQueue = redis.getBoundedBlockingQueue("queueKey");

		// 往队列添加数据
		boundedBlockingQueue.addData(list);

		// 消费队列数据
		List<String> queueValue = boundedBlockingQueue.consumerData();
	}

	public void delayedQueue() {
		// 准备参数
		LocalDateTime now = LocalDateTime.now();
		DelayedQueue<LocalDateTime> delayedQueue = redis.getDelayedQueue("delayedQueueKey");

		// 发送一条延迟消息
		delayedQueue.sendDelayedMsg(now, 5, TimeUnit.SECONDS);

		// 消费一条延迟消息
		LocalDateTime delayedMsg = delayedQueue.consumerDelayedMsg();

		// 持续消费（监听）
		delayedQueue.consumerDelayedMsg(msg -> {
			System.out.println(msg);
		});
	}

}
