package ai.yue.library.base.config.thread.pool;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

import javax.annotation.PostConstruct;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import lombok.extern.slf4j.Slf4j;

/**
 * <b>异步线程池</b>
 * <p>
 * 共用父线程上下文环境，异步执行任务时不丢失token
 * @author 	 孙金川
 * @version 创建时间：2017年10月13日
 */
@Slf4j
@EnableAsync
@Configuration
@EnableConfigurationProperties(AsyncProperties.class)
@ConditionalOnProperty(prefix = "yue.thread-pool.async", name = "enabled", havingValue = "true")
public class AsyncConfig implements AsyncConfigurer {
	
	@Autowired
	AsyncProperties asyncProperties;
	
    @PostConstruct
    private void init() {
    	log.info("【初始化配置-异步线程池】异步线程池配置已加载，待使用时初始化 ...");
    }
    
	/**
	 * 自定义异常处理类
	 */
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new AsyncUncaughtExceptionHandler() {
			@Override
			public void handleUncaughtException(Throwable arg0, Method arg1, Object... arg2) {
				log.error("=========================={}=======================", arg0.getMessage(), arg0);
				log.error("exception method: {}", arg1.getName());
				for (Object param : arg2) {
		        	log.error("Parameter value - {}", param);
		        }
			}
		};
	}
	
	/**
	 * 异步线程池<br>
	 * 实现AsyncConfigurer接口并重写getAsyncExecutor方法，返回一个ThreadPoolTaskExecutor，这样我们就获得了一个基本线程池TaskExecutor。
	 */
	@Override
	public Executor getAsyncExecutor() {
		ContextAwareAsyncExecutor executor = new ContextAwareAsyncExecutor();
		executor.setThreadNamePrefix(asyncProperties.getThreadNamePrefix());// 线程池名的前缀
		executor.setCorePoolSize(asyncProperties.getCorePoolSize());// 核心线程数
		executor.setMaxPoolSize(asyncProperties.getMaxPoolSize());// 最大线程数
		executor.setKeepAliveSeconds(asyncProperties.getKeepAliveSeconds());// 允许线程的空闲时间
		executor.setQueueCapacity(asyncProperties.getQueueCapacity());// 缓冲队列数
		executor.setAllowCoreThreadTimeOut(asyncProperties.getAllowCoreThreadTimeOut());// 是否允许核心线程超时
		executor.setWaitForTasksToCompleteOnShutdown(asyncProperties.getWaitForTasksToCompleteOnShutdown());// 应用关闭时-是否等待未完成任务继续执行，再继续销毁其他的Bean
		executor.setAwaitTerminationSeconds(asyncProperties.getAwaitTerminationSeconds());// 应用关闭时-继续等待时间（单位：秒）
		executor.setRejectedExecutionHandler(asyncProperties.getRejectedExecutionHandlerPolicy().getRejectedExecutionHandler());// 线程池拒绝策略
		executor.initialize();
		log.info("【初始化配置-异步线程池】共用父线程上下文环境，异步执行任务时不丢失token ... 已初始化完毕。");
		return executor;
	}
	
}
