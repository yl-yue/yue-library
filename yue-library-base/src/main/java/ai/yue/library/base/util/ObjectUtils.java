package ai.yue.library.base.util;

import ai.yue.library.base.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 对象工具类，包括对象比较与转换等问题
 * 
 * @author	ylyue
 * @since	2018年7月27日
 */
public class ObjectUtils extends ObjectUtil {
	
	/**
	 * 对象比较
	 * @param obj1 对象1
	 * @param obj2 对象2
	 * @return 是否相等
	 */
	public static boolean equals(Object obj1, Object obj2) {
		if (obj1 == obj2 || obj1.equals(obj2)) {
			return true;
		}

		Class<? extends Object> clazz = obj1.getClass();

		try {
			if (clazz == byte.class || clazz == Byte.class) {
				byte value1 = Byte.parseByte(obj1.toString());
				byte value2 = Byte.parseByte(obj2.toString());
				return value1 == value2;
			}
			if (clazz == short.class || clazz == Short.class || clazz == int.class || clazz == Integer.class) {
				int value1 = Integer.parseInt(obj1.toString());
				int value2 = Integer.parseInt(obj2.toString());
				return value1 == value2;
			}
			if (clazz == long.class || clazz == Long.class || clazz == BigInteger.class) {
				long value1 = Long.parseLong(obj1.toString());
				long value2 = Long.parseLong(obj2.toString());
				return value1 == value2;
			}
			if (clazz == float.class || clazz == Float.class || clazz == double.class || clazz == Double.class
					|| clazz == BigDecimal.class) {
				double value1 = Double.parseDouble(obj1.toString());
				double value2 = Double.parseDouble(obj2.toString());
				return value1 == value2;
			}
			if (clazz == char.class || clazz == Character.class || clazz == String.class) {
				return obj1.toString().equals(obj2);
			}
		} catch (Exception e) {
			return false;
		}

		return false;
	}
	
	/**
	 * 转换值为指定类型
	 * 
	 * @param <T> 泛型
	 * @param value 被转换的值
	 * @param clazz 泛型类型
	 * @return 转换后的对象
	 * @see Convert#toObject(Object, Class)
	 */
	public static <T> T toObject(Object value, Class<T> clazz) {
		return Convert.toObject(value, clazz);
	}
	
	/**
	 * 转换值为指定 POJO 类型
	 * 
	 * @param <T> 泛型
	 * @param value 被转换的值
	 * @param clazz 泛型类型
	 * @return 转换后的POJO
	 * @see Convert#toJavaBean(Object, Class)
	 */
	public static <T> T toJavaBean(Object value, Class<T> clazz) {
		return Convert.toJavaBean(value, clazz);
	}
	
	/**
	 * 转换为 {@linkplain JSONObject}
	 * 
	 * @param value 被转换的值
	 * @return JSON
	 * @see Convert#toJSONObject(Object)
	 */
	public static JSONObject toJSONObject(Object value) {
		return Convert.toJSONObject(value);
	}
	
	/**
	 * 转换为 {@linkplain JSONArray}
	 * 
	 * @param value 被转换的值
	 * @return JSON数组
	 * @see Convert#toJSONArray(Object)
	 */
	public static JSONArray toJSONArray(Object value) {
		return Convert.toJSONArray(value);
	}
    
}
