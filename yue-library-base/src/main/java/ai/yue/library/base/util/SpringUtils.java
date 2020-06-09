package ai.yue.library.base.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Spring(Spring boot)工具封装，包括：
 *
 * <pre>
 *     1、Spring IOC容器中的bean对象获取
 * </pre>
 *
 * @author	ylyue
 * @since	2020年4月6日
 */
@Slf4j
@Component
public class SpringUtils implements ApplicationContextAware {

	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		if (SpringUtils.applicationContext == null) {
			SpringUtils.applicationContext = applicationContext;
			log.info("【初始化工具-SpringUtils】用于普通类中获取Spring IOC容器中的Bean对象，SpringUtils.getBean(Class<T>)");
		}
	}
	
	/**
	 * 获取applicationContext
	 *
	 * @return ApplicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	// 通过name获取 Bean.

	/**
	 * 通过name获取 Bean
	 *
	 * @param name Bean名称
	 * @return Bean
	 */
	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}
	
	/**
	 * 通过class获取Bean
	 *
	 * @param <T> Bean类型
	 * @param clazz Bean类
	 * @return Bean对象
	 */
	public static <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
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
		return applicationContext.getBean(name, clazz);
	}

}