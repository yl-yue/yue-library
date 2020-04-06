package ai.yue.library.base.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 应用上下文工具类，用于在普通类中获取Spring IOC容器中的bean对象
 * 
 * @author	ylyue
 * @since	2019年8月9日
 */
@Slf4j
@Component
public class ApplicationContextUtils implements ApplicationContextAware {

	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (ApplicationContextUtils.applicationContext == null) {
			ApplicationContextUtils.applicationContext = applicationContext;
			log.info("【初始化工具-ApplicationContextUtils】Bean：ApplicationContext ... 已初始化完毕。普通类获取ApplicationContext，ApplicationContextUtils.getApplicationContext()");
		}
	}
	
	/**
	 * 获得 {@linkplain #applicationContext}
	 * 
	 * @return applicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
    
	/**
	 * 获取Spring容器中的Bean - 通过Bean名称
	 * 
	 * @param name Bean名称
	 * @return Object，需要做强制类型转换
	 */
	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}
	
	/**
	 * 获取Spring容器中的Bean - 通过Bean类型
	 * 
	 * @param <T> Bean类型
	 * @param clazz 泛型类型，可以是接口或超类
	 * @return 返回指定类型的单个bean实例
	 */
	public static <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}
	
	/**
	 * 获取Spring容器中的Bean - 通过Bean名称与Bean类型精准获取
	 * 
	 * @param <T> Bean类型
	 * @param name Bean名称
	 * @param clazz 泛型类型，可以是接口或超类
	 * @return Bean的实例
	 */
	public static <T> T getBean(String name, Class<T> clazz) {
		return applicationContext.getBean(name, clazz);
	}
	
}
