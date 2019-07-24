package ai.yue.library.base.convert;

import static com.alibaba.fastjson.JSON.toJSON;
import static com.alibaba.fastjson.util.TypeUtils.cast;
import static com.alibaba.fastjson.util.TypeUtils.castToJavaBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * @author	孙金川
 * @since 	2019年7月23日
 */
@Slf4j
public class Convert extends cn.hutool.core.convert.Convert {
	
	/**
	 * 转换值为指定类型
	 * 
	 * @param <T> 泛型
	 * @param value 被转换的值
	 * @param clazz 泛型类型
	 * @return 转换后的对象
	 * @since Greenwich.SR1.2
	 * @see #toObject(Object, Class)
	 */
	public static <T> T convert(Object value, Class<T> clazz) {
		return toObject(value, clazz);
	}
	
	/**
	 * 转换值为指定类型
	 * 
	 * @param <T> 泛型
	 * @param value 被转换的值
	 * @param clazz 泛型类型
	 * @return 转换后的对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toObject(Object value, Class<T> clazz) {
		// 不用转换
		if (value != null && clazz != null && (clazz == value.getClass() || clazz.isInstance(value))) {
			return (T) value;
		}
		
		// JDK8日期时间转换
		if (value != null && value instanceof String) {
			String str = (String) value;
			if (clazz == LocalDate.class) {
				return (T) LocalDate.parse(str);
			} else if (clazz == LocalDateTime.class) {
				return (T) LocalDateTime.parse(str);
			}
		}
		
		// 采用 fastjson 转换
		try {
			return cast(value, clazz, ParserConfig.getGlobalInstance());
		} catch (Exception e) {
			log.warn("【Convert】采用 fastjson 类型转换器转换失败，正尝试 yue-library 类型转换器转换，错误内容：{}", e.getMessage());
		}
		
		// 采用 yue-library 转换
		return convert(clazz, value);
	}
	
	/**
	 * 转换值为指定 POJO 类型
	 * 
	 * @param <T> 泛型
	 * @param value 被转换的值
	 * @param clazz 泛型类型
	 * @return 转换后的POJO
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toJavaBean(Object value, Class<T> clazz) {
		// 不用转换
		if (value != null && clazz != null && (clazz == value.getClass() || clazz.isInstance(value))) {
			return (T) value;
		}
		
		// 采用 fastjson 转换
		try {
			if (value instanceof JSONObject) {
				return castToJavaBean((JSONObject) value, clazz, ParserConfig.getGlobalInstance());
			}
			
			if (value instanceof String) {
				return JSONObject.parseObject((String) value, clazz);
			}
			
			return castToJavaBean(toJSONObject(value), clazz, ParserConfig.getGlobalInstance());
		} catch (Exception e) {
			log.warn("【Convert】采用 fastjson 类型转换器转换失败，正尝试 yue-library 类型转换器转换，错误内容：{}", e.getMessage());
		}
		
		// 采用 yue-library 转换
		if (value instanceof String) {
			return convert(JSONObject.parseObject((String) value), clazz);
		}
		
		return convert(value, clazz);
	}
	
	/**
	 * 转换为 {@linkplain JSONObject}
	 * 
	 * @param value 被转换的值
	 * @return JSON
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject toJSONObject(Object value) {
		if (value instanceof JSONObject) {
			return (JSONObject) value;
        }
        
        if (value instanceof Map) {
            return new JSONObject((Map<String, Object>) value);
        }
        
        if (value instanceof String) {
			return JSONObject.parseObject((String) value);
        }
        
        return (JSONObject) toJSON(value);
	}
	
	/**
	 * 转换为 {@linkplain JSONArray}
	 * 
	 * @param value 被转换的值
	 * @return JSON数组
	 */
    @SuppressWarnings("unchecked")
	public static JSONArray toJSONArray(Object value) {
        if (value instanceof JSONArray) {
            return (JSONArray) value;
        }
        
        if (value instanceof List) {
            return new JSONArray((List<Object>) value);
        }
        
		if (value instanceof String) {
			return JSONArray.parseArray((String) value);
		}
        
        return (JSONArray) toJSON(value);
    }
    
}
