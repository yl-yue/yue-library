package ai.yue.library.base.util;

import cn.hutool.v7.core.text.StrUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 线程池统一注册中心
 * <p>
 * 管理所有业务线程池的注册与查询。全局线程池和业务线程池均可通过本类查询。
 * <p>
 * 使用方式：
 * <pre>
 * // 获取业务线程池
 * ThreadPoolTaskExecutor pool = ThreadPoolRegistry.getExecutor("email-pool");
 *
 * // 查看所有已注册的线程池名称
 * Set&lt;String&gt; poolNames = ThreadPoolRegistry.getPoolNames();
 * </pre>
 *
 * @author ylyue
 * @since 2026年6月6日
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThreadPoolRegistry {

	/**
	 * 线程池注册表
	 * <p>
	 * Key 为线程池名称，Value 为 ThreadPoolTaskExecutor 实例
	 */
	private static final Map<String, ThreadPoolTaskExecutor> EXECUTOR_REGISTRY = new ConcurrentHashMap<>();

	/**
	 * 注册线程池
	 *
	 * @param poolName 线程池名称
	 * @param executor 线程池实例
	 */
	public static void register(String poolName, ThreadPoolTaskExecutor executor) {
		if (StrUtil.isBlank(poolName)) {
			throw new IllegalArgumentException("线程池名称不能为空");
		}
		if (executor == null) {
			throw new IllegalArgumentException("线程池实例不能为空");
		}
		ThreadPoolTaskExecutor existing = EXECUTOR_REGISTRY.putIfAbsent(poolName, executor);
		if (existing != null) {
			log.warn("线程池 [{}] 已存在，跳过重复注册", poolName);
		} else {
			log.info("【注册业务线程池】{} - 核心线程数: {}, 最大线程数: {}, 队列容量: {}",
					poolName, executor.getCorePoolSize(), executor.getMaxPoolSize(), executor.getQueueCapacity());
		}
	}

	/**
	 * 获取线程池
	 *
	 * @param poolName 线程池名称
	 * @return 线程池实例，不存在时抛出异常
	 */
	public static ThreadPoolTaskExecutor getExecutor(String poolName) {
		ThreadPoolTaskExecutor executor = EXECUTOR_REGISTRY.get(poolName);
		if (executor == null) {
			throw new IllegalArgumentException(StrUtil.format(
					"线程池 [{}] 未注册，请检查 yue.thread-pool.business-pools 配置。已注册的线程池: {}",
					poolName, EXECUTOR_REGISTRY.keySet()));
		}
		return executor;
	}

	/**
	 * 获取所有已注册的线程池名称
	 */
	public static Set<String> getPoolNames() {
		return Collections.unmodifiableSet(EXECUTOR_REGISTRY.keySet());
	}

	/**
	 * 判断线程池是否已注册
	 *
	 * @param poolName 线程池名称
	 */
	public static boolean contains(String poolName) {
		return EXECUTOR_REGISTRY.containsKey(poolName);
	}

}
