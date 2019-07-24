package ai.yue.library.base.config.thread.pool;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author	孙金川
 * @since	2018年11月6日
 */
@Data
@ConfigurationProperties("yue.thread-pool.async")
public class AsyncProperties {
	
	/**
	 * 是否启用 <code style="color:red">异步线程池</code> 自动配置
	 * <p>
	 * 默认：false
	 * <p>
	 * <b style="color:red">注意，@Async异步执行方法，不要和同步调用方法，写在同一个类中，否则异步执行将失效。</b>
	 */
	private boolean enabled = false;
	
	/**
	 * 线程池名的前缀
	 * <p>
	 * 设置好了之后可以方便我们定位处理任务所在的线程池
	 * <p>
	 * 默认：async-exec-
	 */
	private String threadNamePrefix = "async-exec-";
	
	/**
	 * 核心线程数
	 * <p>
	 * 线程池创建时候初始化的线程数
	 * <p>
	 * 默认：10
	 */
	private Integer corePoolSize = 10;
	
	/**
	 * 最大线程数
	 * <p>
	 * 线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
	 * <p>
	 * 默认：20
	 */
	private Integer maxPoolSize = 20;
	
	/**
	 * 允许线程的空闲时间（单位：秒）
	 * <p>
	 * 当超过了核心线程数之外的线程在空闲时间到达之后会被销毁
	 * <p>
	 * 默认：60
	 */
	private Integer keepAliveSeconds = 60;
	
	/**
	 * 缓冲队列
	 * <p>
	 * 用来缓冲执行任务的队列
	 * <p>
	 * 默认：200
	 */
	private Integer queueCapacity = 200;
	
	/**
	 * 是否允许核心线程超时
	 * <p>
	 * 默认：false
	 */
	private Boolean allowCoreThreadTimeOut = false;
	
	/**
	 * 应用关闭时-是否等待未完成任务继续执行，再继续销毁其他的Bean
	 * <p>
	 * 默认：true
	 */
	private Boolean waitForTasksToCompleteOnShutdown = true;
	
	/**
	 * 依赖 {@linkplain #waitForTasksToCompleteOnShutdown} 为true
	 * <p>
	 * 应用关闭时-继续等待时间（单位：秒）
	 * <p>
	 * 如果超过这个时间还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
	 * <p>
	 * 默认：10
	 */
	private Integer awaitTerminationSeconds = 10;
	
	/**
	 * 线程池拒绝策略
	 * <p>
	 * 默认：{@linkplain RejectedExecutionHandlerPolicy#CALLER_RUNS_POLICY}
	 */
	private RejectedExecutionHandlerPolicy rejectedExecutionHandlerPolicy = RejectedExecutionHandlerPolicy.CALLER_RUNS_POLICY;
	
}
