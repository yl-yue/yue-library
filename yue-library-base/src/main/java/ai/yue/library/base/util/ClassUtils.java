package ai.yue.library.base.util;

import cn.hutool.core.util.ClassUtil;

import java.lang.reflect.Method;

/**
 * 类工具类
 * 
 * @author	ylyue
 * @since	2018年1月19日
 */
public class ClassUtils extends ClassUtil {
	
	/**
	 * {@linkplain Class} 数组中是否包含此 {@linkplain Class} 或是其子类与子接口
	 * @param clazzs Class数组
	 * @param clazz Class
	 * @return 是否包含
	 */
	public static boolean isContains(Class<?>[] clazzs, Class<?> clazz) {
		for (Class<?> thisClazz : clazzs) {
			// 是否类或接口相同，或是否为该类或接口的超类或超接口。
			if (clazz.isAssignableFrom(thisClazz)) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * 获得方法引用路径，示例：
	 * <pre>
	 *     ai.yue.library.base.util.ClassUtils#getMethodReferencePath()
	 * </pre>
	 */
	public static String getMethodReferencePath(Method method) {
		return method.getDeclaringClass().getName() + "#" + method.getName() + "()";
	}

}
