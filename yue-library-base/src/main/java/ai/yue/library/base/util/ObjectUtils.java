package ai.yue.library.base.util;

import static com.alibaba.fastjson.JSON.toJSON;
import static com.alibaba.fastjson.util.TypeUtils.castToJavaBean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.util.ObjectUtil;

/**
 * @author  孙金川
 * @version 创建时间：2018年7月27日
 */
public final class ObjectUtils extends ObjectUtil {

	/**
	 * 对象克隆方法，实现深拷贝
	 * @param <T> 泛型
	 * @param obj 需要实现了{@link Serializable}接口的对象才能拷贝
	 * @return 克隆后的对象
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T objectClone(T obj) {
		T cloneObj = null;
		try {
			// 写入字节流
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream obs = new ObjectOutputStream(out);
			obs.writeObject(obj);
			obs.close();

			// 分配内存，写入原始对象，生成新对象
			ByteArrayInputStream ios = new ByteArrayInputStream(out.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(ios);
			// 返回生成的新对象
			cloneObj = (T) ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cloneObj;
	}
	
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
	 * 对象类型转换
	 * <h1>对象 转 {@linkplain Class}</h1>
	 * 
	 * @param <T> 泛型
	 * @param obj 需要转换的对象
	 * @param clazz 泛型类型
	 * @return 转换后的对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toObject(Object obj, Class<T> clazz) {
		if (obj != null && obj instanceof String) {
			String str = (String) obj;
			if (clazz == LocalDate.class) {
				return (T) LocalDate.parse(str);
			} else if (clazz == LocalDateTime.class) {
				return (T) LocalDateTime.parse(str);
			}
		}
		
        return castToJavaBean(obj, clazz);
	}
	
	/**
	 * 对象类型转换
	 * <h1>{@linkplain Object} 转 {@linkplain Class}</h1>
	 * 
	 * @param <T> 泛型
	 * @param obj 需要转换的对象
	 * @param clazz 泛型类型
	 * @return 转换后的POJO
	 */
	public static <T> T toJavaObject(Object obj, Class<T> clazz) {
		return JSONObject.toJavaObject(toJSONObject(obj), clazz);
	}
	
	/**
	 * 对象类型转换
	 * <h1>{@linkplain Object} 转 {@linkplain JSONObject}</h1>
	 * 
	 * @param obj 需要转换的对象
	 * @return JSON
	 */
	public static JSONObject toJSONObject(Object obj) {
		if (obj instanceof JSONObject) {
			return (JSONObject) obj;
        }
        
        if (obj instanceof String) {
			return JSONObject.parseObject((String) obj);
        }
        
        return (JSONObject) toJSON(obj);
	}
	
	/**
	 * 对象类型转换
	 * <h1>{@linkplain Object} 转 {@linkplain JSONArray}</h1>
	 * 
	 * @param obj 需要转换的对象
	 * @return JSON数组
	 */
    public static JSONArray toJSONArray(Object obj) {
        if (obj instanceof JSONArray) {
            return (JSONArray) obj;
        }

        if (obj instanceof String) {
            return (JSONArray) JSON.parse((String) obj);
        }

        return (JSONArray) toJSON(obj);
    }
    
}
