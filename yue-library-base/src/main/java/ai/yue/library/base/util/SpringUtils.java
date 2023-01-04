package ai.yue.library.base.util;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Spring(Spring boot)工具封装，包括：
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
	 * @throws UtilException 当上下文非ConfigurableListableBeanFactory抛出异常
	 */
	public static ConfigurableListableBeanFactory getConfigurableBeanFactory() throws UtilException {
		final ConfigurableListableBeanFactory factory;
		if (null != beanFactory) {
			factory = beanFactory;
		} else if (applicationContext instanceof ConfigurableApplicationContext) {
			factory = ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
		} else {
			throw new UtilException("No ConfigurableListableBeanFactory from context!");
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
	 * @param <T> Bean类型
	 * @param clazz Bean类
	 * @return Bean对象
	 */
	public static <T> T getBean(Class<T> clazz) {
		return getBeanFactory().getBean(clazz);
	}

	/**
	 * 通过name,以及Clazz返回指定的Bean
	 *
	 * @param <T> bean类型
	 * @param name Bean名称
	 * @param clazz bean类型
	 * @return Bean对象
	 */
	public static <T> T getBean(String name, Class<T> clazz) {
		return getBeanFactory().getBean(name, clazz);
	}

	/**
	 * 获取配置文件配置项的值
	 *
	 * @param key 配置项key
	 * @return 属性值
	 */
	public static String getProperty(String key) {
		if (null == applicationContext) {
			return null;
		}
		return applicationContext.getEnvironment().getProperty(key);
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
	 *
	 * @param <T>      Bean类型
	 * @param beanName 名称
	 * @param bean     bean
	 */
	public static <T> void registerBean(String beanName, T bean) {
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
	public static void unregisterBean(String beanName) {
		final ConfigurableListableBeanFactory factory = getConfigurableBeanFactory();
		if (factory instanceof DefaultSingletonBeanRegistry) {
			DefaultSingletonBeanRegistry registry = (DefaultSingletonBeanRegistry) factory;
			registry.destroySingleton(beanName);
		} else {
			throw new UtilException("Can not unregister bean, the factory is not a DefaultSingletonBeanRegistry!");
		}
	}

	/**
	 * 发布事件
	 *
	 * @param event 待发布的事件，事件必须是{@link ApplicationEvent}的子类
	 */
	public static void publishEvent(ApplicationEvent event) {
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
	public static void publishEvent(Object event) {
		if (null != applicationContext) {
			applicationContext.publishEvent(event);
		}
	}

}
