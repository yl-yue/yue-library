package ai.yue.library.base.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;

/**
 * Bean解析工具类
 * 
 * @author	ylyue
 * @since	2020年8月29日
 */
public class BeanUtils extends BeanUtil {

	public static final String GET_METHOD_NAME_FORMAT = "get%s";
	public static final String SET_METHOD_NAME_FORMAT = "set%s";
	
	/**
	 * 获得Java Bean Get方法名
	 * 
	 * @param fieldName 字段名
	 * @return Get方法名
	 */
	public static String getGEtMethodName(String fieldName) {
		return String.format(GET_METHOD_NAME_FORMAT, StrUtil.upperFirst(fieldName));
	}
	
	/**
	 * 获得Java Bean Set方法名
	 * 
	 * @param fieldName 字段名
	 * @return Get方法名
	 */
	public static String getSetMethodName(String fieldName) {
		return String.format(SET_METHOD_NAME_FORMAT, StrUtil.upperFirst(fieldName));
	}
	
}
