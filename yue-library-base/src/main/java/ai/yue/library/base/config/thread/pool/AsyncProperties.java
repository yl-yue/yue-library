package ai.yue.library.base.config.thread.pool;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <h2>异步线程池自动配置属性</h2>
 *
 * 线程池按以下行为执行任务：
 * <p>
 *     1. 当线程数小于核心线程数时，创建线程。<br>
 *     2. 当线程数大于等于核心线程数，且任务队列未满时，将任务放入任务队列。<br>
 *     3. 当线程数大于等于核心线程数，且任务队列已满：<br>
 *     　　- 若线程数小于最大线程数，创建线程<br>
 *     　　- 若线程数等于最大线程数，抛出异常，拒绝任务
 * </p>
 *
 * @author	ylyue
 * @since	2018年11月6日
 */
@Data
@ConfigurationProperties("yue.thread-pool.async")
public class AsyncProperties {

	/**
	 * ServletAsyncContext阻塞超时时长 setAttribute 时所使用的固定变量名
	 */
	public static final String SERVLET_ASYNC_CONTEXT_TIMEOUT_MILLIS = "servletAsyncContextTimeoutMillis";

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
	 * 任务队列容量（阻塞队列）
	 * <p>
	 * 当核心线程数达到最大时，新任务会放在队列中排队等待执行
	 * <p>
	 * 默认：200
	 */
	private Integer queueCapacity = 200;
	
	/**
	 * 是否允许核心线程超时
	 * <p>
	 * 默认：false
	 */
	private boolean allowCoreThreadTimeOut = false;
	
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
	 * 默认：5
	 */
	private Integer awaitTerminationSeconds = 5;
	
	/**
	 * 线程池拒绝策略
	 * <p>
	 * 默认：{@linkplain RejectedExecutionHandlerPolicyEnum#CALLER_RUNS_POLICY}
	 */
	private RejectedExecutionHandlerPolicyEnum rejectedExecutionHandlerPolicy = RejectedExecutionHandlerPolicyEnum.CALLER_RUNS_POLICY;

	/**
	 * 是否开启 ServletAsyncContext
	 * <p>
	 *     用于阻塞父线程 Servlet 的关闭（调用 destroy() 方法），导致子线程获取的上下文为空
	 * </p>
	 * 默认：false
	 */
	private boolean enableServletAsyncContext = false;

	/**
	 * ServletAsyncContext阻塞超时时长（单位：毫秒）
	 * <p>
	 *     单个方法的阻塞超时时间需要更长时，可以使用 {@code ServletUtils.getRequest().setAttribute(AsyncProperties.SERVLET_ASYNC_CONTEXT_TIMEOUT_MILLIS, 2000)}，为单个异步方法设置单独的超时时长。
	 * </p>
	 * 默认：600
	 */
	private Long servletAsyncContextTimeoutMillis = 600L;

}
