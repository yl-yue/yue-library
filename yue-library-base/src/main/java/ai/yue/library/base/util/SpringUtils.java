package ai.yue.library.base.util;

import lombok.extern.slf4j.Slf4j;
import cn.hutool.v7.core.array.ArrayUtil;
import cn.hutool.v7.core.exception.HutoolException;
import cn.hutool.v7.core.reflect.TypeReference;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Map;

/**
 * 应用上下文工具类，用于在普通类中获取Spring IOC容器中的bean对象，包括：
 *
 * <pre>
 *     1、Spring IOC容器中的bean对象获取
 *     2、注册和注销Bean
 * </pre>
 *
 * @author	ylyue
 * @since	2020年4月6日
 */
@Slf4j
@Component
public class SpringUtils implements BeanFactoryPostProcessor, ApplicationContextAware {

	/**
	 * "@PostConstruct"注解标记的类中，由于ApplicationContext还未加载，导致空指针<br>
	 * 因此实现BeanFactoryPostProcessor注入ConfigurableListableBeanFactory实现bean的操作
	 */
	private static ConfigurableListableBeanFactory beanFactory;

	/**
	 * Spring应用上下文环境
	 */
	private static ApplicationContext applicationContext;

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		SpringUtils.beanFactory = beanFactory;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		if (SpringUtils.applicationContext == null) {
			SpringUtils.applicationContext = applicationContext;
			log.info("【初始化工具-SpringUtils】用于普通类中获取Spring IOC容器中的Bean对象，SpringUtils.getBean(Class<T>)");
		}
	}

	/**
	 * 获取{@link ListableBeanFactory}，可能为{@link ConfigurableListableBeanFactory} 或 {@link ApplicationContextAware}
	 *
	 * @return {@link ListableBeanFactory}
	 */
	public static ListableBeanFactory getBeanFactory() {
		return null == beanFactory ? applicationContext : beanFactory;
	}

	/**
	 * 获取{@link ConfigurableListableBeanFactory}
	 *
	 * @return {@link ConfigurableListableBeanFactory}
	 * @throws HutoolException 当上下文非ConfigurableListableBeanFactory抛出异常
	 */
	public static ConfigurableListableBeanFactory getConfigurableBeanFactory() throws HutoolException {
		final ConfigurableListableBeanFactory factory;
		if (null != beanFactory) {
			factory = beanFactory;
		} else if (applicationContext instanceof ConfigurableApplicationContext) {
			factory = ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
		} else {
			throw new HutoolException("No ConfigurableListableBeanFactory from context!");
		}
		return factory;
	}

	/**
	 * 获取applicationContext
	 *
	 * @return ApplicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 通过name获取 Bean
	 *
	 * @param name Bean名称
	 * @return Bean
	 */
	public static Object getBean(String name) {
		return getBeanFactory().getBean(name);
	}

	/**
	 * 通过class获取Bean
	 *
	 * @param <T>   Bean类型
	 * @param clazz Bean类
	 * @param args  构造函数参数
	 * @return Bean对象
	 */
	public static <T> T getBean(final Class<T> clazz, final Object... args) {
		final ListableBeanFactory beanFactory = getBeanFactory();
		if (ArrayUtil.isEmpty(args)) {
			return beanFactory.getBean(clazz);
		}
		return getBeanFactory().getBean(clazz, args);
	}

	/**
	 * 通过name,以及Clazz返回指定的Bean
	 *
	 * @param <T>   bean类型
	 * @param name  Bean名称
	 * @param clazz bean类型
	 * @return Bean对象
	 */
	public static <T> T getBean(final String name, final Class<T> clazz) {
		return getBeanFactory().getBean(name, clazz);
	}

	/**
	 * 通过name,以及Clazz返回指定的Bean
	 *
	 * @param name Bean名称
	 * @param args 创建bean需要的参数属性
	 * @param <T>  Bean类型
	 * @return Bean对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(final String name, final Object... args) {
		final ListableBeanFactory beanFactory = getBeanFactory();
		if (ArrayUtil.isEmpty(args)) {
			return (T) beanFactory.getBean(name);
		}
		return (T) beanFactory.getBean(name, args);
	}

	/**
	 * 通过类型参考返回带泛型参数的Bean
	 *
	 * @param reference 类型参考，用于持有转换后的泛型类型
	 * @param <T>       Bean类型
	 * @return 带泛型参数的Bean
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(final TypeReference<T> reference) {
		final ParameterizedType parameterizedType = (ParameterizedType) reference.getType();
		final Class<T> rawType = (Class<T>) parameterizedType.getRawType();
		final Class<?>[] genericTypes = Arrays.stream(parameterizedType.getActualTypeArguments()).map(type -> (Class<?>) type).toArray(Class[]::new);
		final String[] beanNames = getBeanFactory().getBeanNamesForType(ResolvableType.forClassWithGenerics(rawType, genericTypes));
		return getBean(beanNames[0], rawType);
	}

	/**
	 * 获取指定类型对应的所有Bean，包括子类
	 *
	 * @param <T>  Bean类型
	 * @param type 类、接口，null表示获取所有bean
	 * @return 类型对应的bean，key是bean注册的name，value是Bean
	 */
	public static <T> Map<String, T> getBeansOfType(final Class<T> type) {
		return getBeanFactory().getBeansOfType(type);
	}

	/**
	 * 获取指定类型对应的Bean名称，包括子类
	 *
	 * @param type 类、接口，null表示获取所有bean名称
	 * @return bean名称
	 */
	public static String[] getBeanNamesForType(final Class<?> type) {
		return getBeanFactory().getBeanNamesForType(type);
	}

	/**
	 * 获取配置文件配置项的值
	 *
	 * @param key 配置项key
	 * @return 属性值
	 */
	public static String getProperty(final String key) {
		final ConfigurableEnvironment environment = getEnvironment();
		return null == environment ? null : environment.getProperty(key);
	}

	/**
	 * 获取配置文件配置项的值
	 *
	 * @param key          配置项key
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	public static String getProperty(final String key, final String defaultValue) {
		final ConfigurableEnvironment environment = getEnvironment();
		return null == environment ? null : environment.getProperty(key, defaultValue);
	}

	/**
	 * 获取配置文件配置项的值
	 *
	 * @param <T>          属性值类型
	 * @param key          配置项key
	 * @param targetType   配置项类型
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	public static <T> T getProperty(final String key, final Class<T> targetType, final T defaultValue) {
		final ConfigurableEnvironment environment = getEnvironment();
		return null == environment ? null : environment.getProperty(key, targetType, defaultValue);
	}

	/**
	 * 获取环境属性
	 *
	 * @return {@link ConfigurableEnvironment}
	 */
	public static ConfigurableEnvironment getEnvironment() {
		return null == applicationContext ? null : (ConfigurableEnvironment) applicationContext.getEnvironment();
	}

	/**
	 * 获取应用程序名称
	 *
	 * @return 应用程序名称
	 */
	public static String getApplicationName() {
		return getProperty("spring.application.name");
	}

	/**
	 * 获取当前的环境配置，无配置返回null
	 *
	 * @return 当前的环境配置
	 */
	public static String[] getActiveProfiles() {
		if (null == applicationContext) {
			return null;
		}
		return applicationContext.getEnvironment().getActiveProfiles();
	}

	/**
	 * 获取当前的环境配置，当有多个环境配置时，只获取第一个
	 *
	 * @return 当前的环境配置
	 */
	public static String getActiveProfile() {
		final String[] activeProfiles = getActiveProfiles();
		return ArrayUtil.isNotEmpty(activeProfiles) ? activeProfiles[0] : null;
	}

	/**
	 * 动态向Spring注册Bean
	 * <p>
	 * 由{@link org.springframework.beans.factory.BeanFactory} 实现，通过工具开放API
	 * <p>
	 * 更新: shadow 2021-07-29 17:20:44 增加自动注入，修复注册bean无法反向注入的问题
	 *
	 * @param <T>      Bean类型
	 * @param beanName 名称
	 * @param bean     bean
	 */
	public static <T> void registerBean(final String beanName, final T bean) {
		final ConfigurableListableBeanFactory factory = getConfigurableBeanFactory();
		factory.autowireBean(bean);
		factory.registerSingleton(beanName, bean);
	}

	/**
	 * 注销bean
	 * <p>
	 * 将Spring中的bean注销，请谨慎使用
	 *
	 * @param beanName bean名称
	 */
	public static void unregisterBean(final String beanName) {
		final ConfigurableListableBeanFactory factory = getConfigurableBeanFactory();
		if (factory instanceof DefaultSingletonBeanRegistry) {
			final DefaultSingletonBeanRegistry registry = (DefaultSingletonBeanRegistry) factory;
			registry.destroySingleton(beanName);
		} else {
			throw new HutoolException("Can not unregister bean, the factory is not a DefaultSingletonBeanRegistry!");
		}
	}

	/**
	 * 发布事件
	 *
	 * @param event 待发布的事件，事件必须是{@link ApplicationEvent}的子类
	 */
	public static void publishEvent(final ApplicationEvent event) {
		if (null != applicationContext) {
			applicationContext.publishEvent(event);
		}
	}

	/**
	 * 发布事件
	 * Spring 4.2+ 版本事件可以不再是{@link ApplicationEvent}的子类
	 *
	 * @param event 待发布的事件
	 */
	public static void publishEvent(final Object event) {
		if (null != applicationContext) {
			applicationContext.publishEvent(event);
		}
	}

}
