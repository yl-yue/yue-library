package ai.yue.library.base.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;

import ai.yue.library.base.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Map工具类
 * 
 * @author	ylyue
 * @since	2018年1月26日
 */
@Slf4j
public class MapUtils extends MapUtil {
	
	/** 不可变的空Json常量 */
	public final static JSONObject FINAL_EMPTY_JSON = new JSONObject();
	
	/**
	 * 判断Map数据结构key的一致性
	 * @param paramMap			参数
	 * @param mustContainKeys	必须包含的key（必传）
	 * @param canContainKeys	可包含的key（非必传）
	 * @return 是否满足条件
	 */
	public static boolean isKeys(Map<String, Object> paramMap, String[] mustContainKeys, String... canContainKeys) {
		// 1. 必传参数校验
		for (String key : mustContainKeys) {
			if (!paramMap.containsKey(key)) {
				return false;
			}
		}
		
		// 2. 无可选参数
		if (StringUtils.isEmptys(canContainKeys)) {
			return true;
		}
		
		// 3. 可选参数校验-确认paramMap大小
		int keySize = mustContainKeys.length + canContainKeys.length;
		if (paramMap.size() > keySize) {
			return false;
		}
		
		// 4. 获得paramMap中包含可包含key的大小
		int paramMapCanContainKeysLength = 0;
		for (String key : canContainKeys) {
			if (paramMap.containsKey(key)) {
				paramMapCanContainKeysLength++;
			}
		}
		
		// 5. 确认paramMap中包含的可包含key大小 + 必须包含key大小 是否等于 paramMap大小
		if (paramMapCanContainKeysLength + mustContainKeys.length != paramMap.size()) {
			return false;
		}
		
		// 6. 通过所有校验，返回最终结果
		return true;
	}
	
	/**
	 * 判断Map数据结构所有的key是否与数组完全匹配
	 * @param paramMap 需要确认的Map
	 * @param keys 条件
	 * @return 匹配所有的key且大小一致（true）
	 */
	public static boolean isKeysEqual(Map<String, Object> paramMap, String[] keys) {
		if (paramMap.size() != keys.length) {
			return false;
		}
		for(String key : keys) {
			if(!paramMap.containsKey(key)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 判断Map数据结构是否包含 <b>keys</b> 之一
	 * @param paramMap 需要确认的Map
	 * @param keys 条件
	 * @return 只要包含一个key（true）
	 */
	public static boolean isContainsOneOfKey(Map<String, Object> paramMap, String[] keys) {
		for(String key : keys) {
			if(paramMap.containsKey(key)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断Map数组第一个元素，是否包含所有的key<br>
	 * <p>弱比较，只判断数组中第一个元素是否包含所有的key</p>
	 * @param paramMaps 需要确认的Map数组
	 * @param keys 条件数组
	 * @return Map数组元素0包含所有的key（true）
	 */
	public static boolean isMapsKeys(Map<String, Object>[] paramMaps, String[] keys) {
		return isKeys(paramMaps[0], keys);
	}
	
	/**
	 * 判断Map数组是否为空<br>
	 * <p>弱判断，只确定数组中第一个元素是否为空</p>
	 * @param paramMaps 要判断的Map[]数组
	 * @return Map数组==null或长度==0或第一个元素为空（true）
	 */
	public static boolean isEmptys(Map<String, Object>[] paramMaps) {
		return (null == paramMaps || paramMaps.length == 0 || paramMaps[0].isEmpty()) ? true : false;
	}
	
	/**
	 * 判断Map是否为空，或者Map中String类型的value值是否为空<br>
	 * @param paramMap 要判断的Map
	 * @return value值是否为空
	 */
	public static boolean isStringValueEmpty(Map<String, Object> paramMap) {
		if (paramMap.isEmpty()) {
			return true;
		}
		for (Object value : paramMap.values()) {
			if (null == value || "".equals(value))  {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 删除Value字符串前后空格
	 * @param paramMap 需要处理的map
	 */
	public static void trimStringValues(Map<String, Object> paramMap) {
		for (String key : paramMap.keySet()) {
			String str = getString(paramMap, key);
			String value = str.trim();
			if (!str.equals(value)) {
				paramMap.replace(key, value);
			}
		}
	}
	
	/**
	 * 批量移除
	 * @param paramMap 要操作的Map
	 * @param keys 被移除的key数组
	 */
	public static void remove(Map<String, Object> paramMap, String[] keys) {
		for (String key : keys) {
			paramMap.remove(key);
		}
	}
	
	/**
	 * 移除空对象
	 * @param paramMap 要操作的Map
	 */
	public static void removeEmpty(Map<String, Object> paramMap) {
		Iterator<Entry<String, Object>> iter = paramMap.entrySet().iterator();
	    while (iter.hasNext()) {
	        Entry<String, Object> entry = iter.next();
	        Object value = entry.getValue();
	        if (ObjectUtils.isNull(value)) {
	        	iter.remove();
	        }
	    }
	}
	
	/**
	 * 移除空白字符串
	 * <p>空白的定义如下： <br>
	 * 1、为null <br>
	 * 2、为不可见字符（如空格）<br>
	 * 3、""<br>
	 * 
	 * @param paramMap 要操作的Map
	 */
	public static void removeBlankStr(Map<String, Object> paramMap) {
		Iterator<Entry<String, Object>> iter = paramMap.entrySet().iterator();
	    while (iter.hasNext()) {
	        Entry<String, Object> entry = iter.next();
	        Object value = entry.getValue();
	        if (StrUtil.isBlankIfStr(value)) {
	        	iter.remove();
	        }
	    }
	}
	
	/**
	 * 替换key
	 * @param paramMap 		要操作的Map
	 * @param key 			被替换的key
	 * @param replaceKey	替换的key
	 */
	public static void replaceKey(Map<String, Object> paramMap, String key, String replaceKey) {
		var value = paramMap.get(key);
		paramMap.put(replaceKey, value);
		paramMap.remove(key);
	}
	
    /**
     * 获取所有的key
     * @param paramMap 需要获取keys的map
     * @return keyList
     */
    public static List<String> keyList(Map<String, Object> paramMap) {
		List<String> list = new ArrayList<>();
		paramMap.keySet().forEach(action -> {
			list.add(action);
		});
		return list;
    }
    
    /**
     * 以安全的方式从Map中获取一组数据，组合成一个新的JSONObject
     * @param paramMap 需要从中获取数据的map
     * @param keys 获取的keys
     * @return 结果
     */
	public static JSONObject getJSONObject(Map<String, Object> paramMap, String... keys) {
		JSONObject paramJson = new JSONObject(paramMap);
		if (!isContainsOneOfKey(paramJson, keys)) {
			return null;
		}

		JSONObject resultJson = new JSONObject();
		for (String key : keys) {
			Object value = paramJson.get(key);
			if (value != null) {
				resultJson.put(key, value);
			}
		}

		return resultJson;
	}
	
	/**
	 * 以安全的方式从Map中获取对象
	 * @param <T> 泛型
	 * @param paramMap 参数map
	 * @param key key
	 * @param clazz 泛型类型
	 * @return 结果
	 */
    public static <T> T getObject(final Map<?, ?> paramMap, final Object key, Class<T> clazz) {
        if (paramMap != null) {
            Object answer = paramMap.get(key);
            if (answer != null) {
            	return Convert.toObject(answer, clazz);
            }
        }
        
        return null;
    }
    
	/**
	 * 以安全的方式从Map中获取Number
	 * @param paramMap 参数map
	 * @param key key
	 * @return 结果
	 */
    public static Number getNumber(final Map<?, ?> paramMap, final Object key) {
        if (paramMap != null) {
            Object answer = paramMap.get(key);
            if (answer != null) {
                if (answer instanceof Number) {
                    return (Number) answer;
                    
                } else if (answer instanceof String) {
                    try {
                        String text = (String) answer;
                        return NumberFormat.getInstance().parse(text);
                        
                    } catch (ParseException e) {
                        // failure means null is returned
                    }
                }
            }
        }
        return null;
    }
	
	/**
	 * 以安全的方式从Map中获取字符串
	 * @param paramMap 参数map
	 * @param key key
	 * @return 结果
	 */
    public static String getString(final Map<?, ?> paramMap, final Object key) {
        if (paramMap != null) {
            Object answer = paramMap.get(key);
            if (answer != null) {
                return answer.toString();
            }
        }
        return null;
    }
    
    /**
     * 以安全的方式从Map中获取Boolean
     * @param paramMap 参数map
	 * @param key key
	 * @return 结果
     */
    public static Boolean getBoolean(final Map<?, ?> paramMap, final Object key) {
        if (paramMap != null) {
            Object answer = paramMap.get(key);
            if (answer != null) {
                if (answer instanceof Boolean) {
                    return (Boolean) answer;
                    
                } else if (answer instanceof String) {
                    return Boolean.valueOf((String) answer);
                    
                } else if (answer instanceof Number) {
                    Number n = (Number) answer;
                    return (n.intValue() != 0) ? Boolean.TRUE : Boolean.FALSE;
                }
            }
        }
        return null;
    }
    
    /**
     * 以安全的方式从Map中获取Integer
     * @param paramMap 参数map
	 * @param key key
	 * @return 结果
     */
    public static Integer getInteger(final Map<?, ?> paramMap, final Object key) {
        Number answer = getNumber(paramMap, key);
        if (answer == null) {
            return null;
        } else if (answer instanceof Integer) {
            return (Integer) answer;
        }
        return answer.intValue();
    }
	
    /**
     * 以安全的方式从Map中获取Long
     * @param paramMap 参数map
	 * @param key key
	 * @return 结果
     */
    public static Long getLong(final Map<?, ?> paramMap, final Object key) {
        Number answer = getNumber(paramMap, key);
        if (answer == null) {
            return null;
        } else if (answer instanceof Long) {
            return (Long) answer;
        }
        return answer.longValue();
    }
    
    /**
     * 以安全的方式从Map中获取Double
     * @param paramMap 参数map
	 * @param key key
	 * @return 结果
     */
    public static Double getDouble(final Map<?, ?> paramMap, final Object key) {
        Number answer = getNumber(paramMap, key);
        if (answer == null) {
            return null;
        } else if (answer instanceof Double) {
            return (Double) answer;
        }
        return answer.doubleValue();
    }
    
    /**
     * 以安全的方式从Map中获取BigDecimal
     * @param paramMap 参数map
	 * @param key key
	 * @return 结果
     */
    public static BigDecimal getBigDecimal(final Map<?, ?> paramMap, final Object key) {
        Number answer = getNumber(paramMap, key);
        if (answer == null) {
            return null;
        } else if (answer instanceof Double) {
        	return new BigDecimal((Double) answer);
        }
        return new BigDecimal(answer.doubleValue());
    }
    
    /**
     * 以安全的方式从Map中获取JSONObject
     * @param paramMap 参数map
	 * @param key key
	 * @return 结果
     */
    public static JSONObject getJSONObject(final Map<?, ?> paramMap, String key) {
        Object value = paramMap.get(key);
        return Convert.toJSONObject(value);
    }
	
    /**
     * 以安全的方式从Map中获取JSONArray
     * @param paramMap 参数map
	 * @param key key
	 * @return 结果
     */
    public static JSONArray getJSONArray(final Map<?, ?> paramMap, String key) {
        Object value = paramMap.get(key);
        return Convert.toJSONArray(value);
    }
    
	/**
	 * 属性命名策略转换-驼峰命名法
	 * 
	 * @param param Json参数 或 POJO对象
	 * @return 经过属性命名策略转换后的 JSONObject
	 */
	public static JSONObject toCamelCase(Object param) {
		return toPropertyNamingStrategy(param, PropertyNamingStrategy.CamelCase);
	}
	
	/**
	 * 属性命名策略转换-下划线命名法
	 * 
	 * @param param Json参数 或 POJO对象
	 * @return 经过属性命名策略转换后的 JSONObject
	 */
	public static JSONObject toUnderlineCase(Object param) {
		return toPropertyNamingStrategy(param, PropertyNamingStrategy.SnakeCase);
	}
	
	/**
	 * 属性命名策略转换-下划线命名法
	 * 
	 * @param param Json参数 或 POJO对象
	 * @return 经过属性命名策略转换后的 JSONObject
	 */
	public static JSONObject toSnakeCase(Object param) {
		return toPropertyNamingStrategy(param, PropertyNamingStrategy.SnakeCase);
	}
	
	/**
	 * 属性命名策略转换
	 * 
	 * @param param Json参数 或 POJO对象
	 * @param propertyNamingStrategy 属性命名策略
	 * @return 经过属性命名策略转换后的 JSONObject
	 */
	public static JSONObject toPropertyNamingStrategy(Object param, PropertyNamingStrategy propertyNamingStrategy) {
		// Json参数
		if (param instanceof Map) {
			JSONObject jsonObject = Convert.toJSONObject(param);
			if (MapUtils.isEmpty(jsonObject)) {
				return jsonObject;
			}
			
			JSONObject paramJson = new JSONObject(true);
			jsonObject.forEach((key, value) -> {
				paramJson.put(propertyNamingStrategy.translate(key), value);
			});
			
			return paramJson;
		}
		
		// POJO对象
		SerializeConfig serializeConfig = new SerializeConfig();
		serializeConfig.setPropertyNamingStrategy(propertyNamingStrategy);
		JSONObject paramJson = (JSONObject) JSONObject.toJSON(param, serializeConfig);
		return paramJson;
	}
	
	/**
	 * <b>将指定值提取出来作为map key,map的值为相同key值的list<b>
	 * <p>
	 * 例：一个用户集合中的对象有key、name、sex
	 * <p>
	 * 数据1：key：1，name：张三，sex：man
	 * <p>
	 * 数据2：key：2，name：李四，sex:woman
	 * <p>
	 * 数据3：key：3，name：王五，sex：man
	 * <p>
	 * 方法调用：ListPOJOExtractKeyToList(list,"sex");
	 * <p>
	 * 处理后返回结果为一个map，值为一个list,json表示为：
	 * <p>
	 * {"man":[{"key":"1","name":"张三","sex":"man"},{"key":"3","name":"王五","sex":"man"}],
	 * <p>
	 * "woman":[{"key":"2","name":"李四","sex":"woman"}]}
	 *
	 * @param objectList 对象list
	 * @return key为map key的键值对
	 */
	public static <T> Map<String, List<T>> listPOJOExtractKeyToList(List<T> objectList, String key) {
		// 声明一个返回的map集合
		Map<String, List<T>> map = new LinkedHashMap<>();
		// 如果需要转换的值是空的，直接返回一个空的集合
		if (ListUtils.isEmpty(objectList)) {
			return map;
		}
		// 循环集合，转换为map
		for (T item : objectList) {
			// 声明一个object对象接收key 的值
			Object valueKey = null;
			try {
				// 通过对象和属性值获取对应的值
				valueKey = getValue(item, key);
			} catch (Exception e) {
				// 未找到方法值时不处理，采用默认的null
				log.error("未找到方法值", e);
			}
			// 获取需要返回的map中是否已有该值的集合
			List<T> list = map.get(valueKey == null ? null : valueKey.toString());
			// 如果没有该值的集合，创建一个新的集合插入map中
			if (list == null) {
				list = new ArrayList<T>();
				map.put(valueKey == null ? null : valueKey.toString(), list);
			}
			// 将该对象插入对应的集合中去
			list.add(item);
		}
		
		return map;
	}
	
	/**
	 * <p>
	 * 将list对象中数据提取为单个map键值对
	 * <p>
	 * 注：如果有相同的key时，后面的值会覆盖第一次出现的key对应的值
	 * <p>
	 * 例：一个用户集合中的对象有key、name、sex
	 * <p>
	 * 数据1：key：1，name：张三，sex：man
	 * <p>
	 * 数据2：key：2，name：李四，sex:woman
	 * <p>
	 * 数据3：key：3，name：王五，sex：man
	 * <p>
	 * 方法调用：ListPOJOExtractKeyToList(list,"key");
	 * <p>
	 * 处理后返回结果为一个map，值为一个对象,json表示为：
	 * <p>
	 * {"1":{"key":"1","name":"张三","sex":"man"},"2":{"key":"2","name":"李四","sex":"woman"},"3":{"key":"3","name":"王五","sex":"man"}}
	 * 
	 * @param objectList list数据
	 * @param key        需要提取的key
	 * @param <T>        泛型对象
	 * @return Map<String, T>
	 */
	public static <T> Map<String, T> listPOJOExtractKeyToPOJO(List<T> objectList, String key) {
		// 声明一个返回的map集合
		Map<String, T> map = new LinkedHashMap<>();
		// 如果需要转换的值是空的，直接返回一个空的集合
		if (objectList == null || objectList.isEmpty()) {
			return map;
		}
		// 循环集合，转换为map
		for (T item : objectList) {
			// 声明一个object对象接收key 的值
			Object mapKey = null;
			try {
				// 通过对象和属性值获取对应的值
				mapKey = getValue(item, key);
			} catch (Exception e) {
				// 未找到方法值时不处理，采用默认的null
				log.error("未找到方法值", e);
			}
			// 将取到的值作为key，当前对象作为值，插入map中，如果有相同的key会覆盖之前的值
			map.put(mapKey == null ? null : mapKey.toString(), item);
		}
		
		return map;
	}
	
	/**
	 * 获取
	 *
	 * @param obj
	 * @param name
	 * @return
	 * @throws Exception
	 */
	private static Object getValue(Object obj, String name) {
		BeanInfo beanInfo;
		try {
			beanInfo = Introspector.getBeanInfo(obj.getClass());
		} catch (IntrospectionException e) {
			log.info("获取实体信息错误", e);
			return null;
		}
		//获取所有属性
		PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor descriptor : descriptors) {
			//获取get方法
			Method readMethod = descriptor.getReadMethod();
			//判断是否是需要的属性的get方法
			if (!name.equals(descriptor.getName())) {
				continue;
			}
			try {
				//执行get方法拿到值
				Object value = readMethod.invoke(obj);
				return value;
			} catch (IllegalAccessException | InvocationTargetException e) {
				log.info("获取值发生错误", e);
				return null;
			}
		}
		return null;
	}
	
}
