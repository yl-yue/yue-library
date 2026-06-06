package ai.yue.library.base.config.thread.pool;

import ai.yue.library.base.util.ThreadPoolRegistry;
import cn.hutool.v7.core.text.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;
import java.util.Set;

/**
 * <h2>业务线程池自动配置</h2>
 * <p>
 * 读取 {@code yue.thread-pool.business-pools} 配置，为每个业务线程池创建 Spring Bean 并注册到 {@link ThreadPoolRegistry}。
 * <p>
 * 自动注入 {@link TaskDecorator}（上下文传递），业务线程池无需手动配置上下文装饰器。
 * <p>
 * 使用方式：
 * <pre>
 * // 1. 配置文件定义
 * yue:
 *   thread-pool:
 *     business-pools:
 *       email-pool:
 *         core-pool-size: 20
 *         max-pool-size: 50
 *
 * // 2. 注解式使用
 * &#64;Async("email-pool")
 * public void sendEmail() { ... }
 *
 * // 3. 编程式使用
 * AsyncUtils.execAsync("email-pool", () -&gt; { ... });
 * </pre>
 *
 * @author ylyue
 * @since 2026年6月6日
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(BusinessPoolProperties.class)
@ConditionalOnProperty(prefix = "yue.thread-pool.business-pools", value = "enabled", havingValue = "true", matchIfMissing = true)
public class BusinessPoolAutoConfiguration {

	final BusinessPoolProperties businessPoolProperties;
	final TaskDecorator taskDecorator;

	/**
	 * Spring 保留的 executor bean name，业务线程池禁止使用
	 */
	private static final Set<String> RESERVED_NAMES = Set.of(
			"taskExecutor",
			"applicationTaskExecutor"
	);

	/**
	 * poolName 命名规范正则：小写字母开头，仅允许小写字母、数字、中划线
	 */
	private static final String POOL_NAME_PATTERN = "[a-z][a-z0-9-]*";

	/**
	 * 注册所有业务线程池
	 * <p>
	 * 使用 {@link org.springframework.context.annotation.ImportBeanDefinitionRegistrar} 的替代方案：
	 * 在 {@code @Bean} 方法中手动注册，确保线程池 bean 可被 {@code @Async} 发现。
	 */
	@org.springframework.context.annotation.Bean
	public BusinessPoolRegistrar businessPoolRegistrar(org.springframework.beans.factory.support.DefaultListableBeanFactory beanFactory) {
		Map<String, BusinessPoolProperties.PoolConfig> pools = businessPoolProperties.getBusinessPools();
		BusinessPoolRegistrar registrar = new BusinessPoolRegistrar(beanFactory, taskDecorator);

		pools.forEach((poolName, config) -> {
			// 1. 校验 poolName
			validatePoolName(poolName);

			// 2. 解析默认线程名前缀
			if (StrUtil.isBlank(config.getThreadNamePrefix())) {
				config.setThreadNamePrefix(poolName + "-exec-");
			}

			// 3. 创建线程池并注册
			registrar.register(poolName, config);
		});

		return registrar;
	}

	/**
	 * 校验线程池名称
	 */
	private void validatePoolName(String poolName) {
		if (StrUtil.isBlank(poolName)) {
			throw new IllegalArgumentException("业务线程池名称不能为空");
		}
		if (!poolName.matches(POOL_NAME_PATTERN)) {
			throw new IllegalArgumentException(StrUtil.format(
					"业务线程池名称 [{}] 不符合命名规范，仅允许：小写字母开头，包含小写字母、数字、中划线。正则: {}",
					poolName, POOL_NAME_PATTERN));
		}
		if (RESERVED_NAMES.contains(poolName)) {
			throw new IllegalArgumentException(StrUtil.format(
					"业务线程池名称 [{}] 是 Spring 保留名，禁止使用。保留名列表: {}",
					poolName, RESERVED_NAMES));
		}
	}

	/**
	 * 业务线程池注册器
	 * <p>
	 * 负责创建 ThreadPoolTaskExecutor 并注册为 Spring Bean
	 */
	@RequiredArgsConstructor
	public static class BusinessPoolRegistrar {

		final org.springframework.beans.factory.support.DefaultListableBeanFactory beanFactory;
		final TaskDecorator taskDecorator;

		/**
		 * 创建并注册业务线程池
		 */
		public void register(String poolName, BusinessPoolProperties.PoolConfig config) {
			ThreadPoolTaskExecutor executor = createExecutor(poolName, config);

			// 注册为 Spring Bean，使 @Async("poolName") 可以发现
			beanFactory.registerSingleton(poolName, executor);

			// 注册到统一注册中心，使 AsyncUtils 可以发现
			ThreadPoolRegistry.register(poolName, executor);
		}

		/**
		 * 创建 ThreadPoolTaskExecutor 实例
		 */
		private ThreadPoolTaskExecutor createExecutor(String poolName, BusinessPoolProperties.PoolConfig config) {
			ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
			executor.setThreadNamePrefix(config.getThreadNamePrefix());
			executor.setCorePoolSize(config.getCorePoolSize());
			executor.setMaxPoolSize(config.getMaxPoolSize());
			executor.setKeepAliveSeconds(config.getKeepAliveSeconds());
			executor.setQueueCapacity(config.getQueueCapacity());
			executor.setAllowCoreThreadTimeOut(config.isAllowCoreThreadTimeOut());
			executor.setWaitForTasksToCompleteOnShutdown(config.isWaitForTasksToCompleteOnShutdown());
			executor.setAwaitTerminationSeconds(config.getAwaitTerminationSeconds());
			executor.setRejectedExecutionHandler(config.getRejectedExecutionHandlerPolicy().getRejectedExecutionHandler());
			executor.setTaskDecorator(taskDecorator);
			executor.setBeanName(poolName);
			executor.initialize();
			return executor;
		}

	}

}
