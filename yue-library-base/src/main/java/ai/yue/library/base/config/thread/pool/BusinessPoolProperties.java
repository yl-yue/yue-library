package ai.yue.library.base.config.thread.pool;

import cn.hutool.v7.core.util.RuntimeUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <h2>业务线程池自动配置属性</h2>
 * <p>
 * 支持通过配置文件声明式定义多个业务线程池，每个线程池拥有独立的线程池参数。
 * 未指定的配置项使用最佳默认值（与全局线程池一致），实现最小化配置。
 * <p>
 * 配置示例：
 * <pre>
 * yue:
 *   thread-pool:
 *     business-pools:
 *       email-pool:
 *         core-pool-size: 20
 *         max-pool-size: 50
 *         queue-capacity: 500
 * </pre>
 *
 * @author ylyue
 * @since 2026年6月6日
 */
@Data
@ConfigurationProperties("yue.thread-pool")
public class BusinessPoolProperties implements Serializable {

	/**
	 * 业务线程池配置映射
	 * <p>
	 * Key 为线程池名称（同时作为 Spring Bean name），Value 为线程池参数配置
	 */
	private Map<String, PoolConfig> businessPools = new HashMap<>();

	/**
	 * 单个业务线程池的参数配置
	 */
	@Data
	public static class PoolConfig implements Serializable {

		/**
		 * 核心线程数
		 * <p>
		 * 线程池创建时候初始化的线程数
		 * <p>
		 * 默认：JVM的可用处理器数量
		 */
		private Integer corePoolSize = RuntimeUtil.getProcessorCount();

		/**
		 * 最大线程数
		 * <p>
		 * 线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
		 * <p>
		 * 默认：JVM的可用处理器数量 × 5
		 */
		private Integer maxPoolSize = corePoolSize * 5;

		/**
		 * 任务队列容量（阻塞队列）
		 * <p>
		 * 当核心线程数达到最大时，新任务会放在队列中排队等待执行
		 * <p>
		 * 默认：200
		 */
		private Integer queueCapacity = 200;

		/**
		 * 允许线程的空闲时间（单位：秒）
		 * <p>
		 * 当超过了核心线程数之外的线程在空闲时间到达之后会被销毁
		 * <p>
		 * 默认：60
		 */
		private Integer keepAliveSeconds = 60;

		/**
		 * 是否允许核心线程超时
		 * <p>
		 * 默认：false
		 */
		private boolean allowCoreThreadTimeOut = false;

		/**
		 * 线程名的前缀
		 * <p>
		 * 设置好了之后可以方便我们定位处理任务所在的线程池
		 * <p>
		 * 默认：{poolName}-exec-
		 */
		private String threadNamePrefix;

		/**
		 * 应用关闭时-是否等待未完成任务继续执行，再继续销毁其他的Bean
		 * <p>
		 * 默认：true
		 */
		private boolean waitForTasksToCompleteOnShutdown = true;

		/**
		 * 依赖 {@linkplain #waitForTasksToCompleteOnShutdown} 为true
		 * <p>
		 * 应用关闭时-继续等待时间（单位：秒）
		 * <p>
		 * 如果超过这个时间还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
		 * <p>
		 * 默认：25
		 */
		private Integer awaitTerminationSeconds = 25;

		/**
		 * 线程池拒绝策略
		 * <p>
		 * 默认：{@linkplain RejectedExecutionHandlerPolicyEnum#CALLER_RUNS_POLICY}
		 */
		private RejectedExecutionHandlerPolicyEnum rejectedExecutionHandlerPolicy = RejectedExecutionHandlerPolicyEnum.CALLER_RUNS_POLICY;

	}

}
