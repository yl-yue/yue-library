package ai.yue.library.base.util;

import ai.yue.library.base.constant.CompareModeEnum;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @author  孙金川
 * @version 创建时间：2018年12月10日
 */
public class AutoIncrementZerofillUtils {
	
	/**
	 * 获得初始化值，自动填充零
	 * @param length 初始化长度
	 * @return
	 */
	public static String getInitValue(int length) {
		return StrUtil.padPre("1", length, '0');
	}
	
	/**
	 * 字符串尾部值自动递增
	 * @param str 尾部值是 {@linkplain Integer} 类型
	 * @return
	 */
	public static String autoIncrement(String str) {
		int maxIndex = str.length() - 1;
		Integer autoIncrementValue = Integer.parseInt(CharUtil.toString(str.charAt(maxIndex))) + 1;
		if (autoIncrementValue == 10) {
			for (int i = maxIndex - 1; i >= 0; i--) {
				Integer autoIncrementValueI = Integer.parseInt(CharUtil.toString(str.charAt(i))) + 1;
				if (autoIncrementValueI != 10) {
					return StringUtils.replace(str, autoIncrementValueI.toString(), i, i);
				}
			}
		}
		
		return str.substring(0, maxIndex) + autoIncrementValue;
	}
	
	/**
	 * 字符串尾部值自动递减
	 * @param str 尾部值是 {@linkplain Integer} 类型
	 * @return
	 */
	public static String autoDecr(String str) {
		int maxIndex = str.length() - 1;
		Integer autoIncrementValue = Integer.parseInt(CharUtil.toString(str.charAt(maxIndex))) - 1;
		return str.substring(0, maxIndex) + autoIncrementValue;
	}
	
	/**
	 * 比较
	 * @param str1
	 * @param str2
	 * @param compareModeEnum
	 * @return
	 */
	public static boolean isCompare(String str1, String str2, CompareModeEnum compareModeEnum) {
		Long long1 = Convert.toLong(str1);
		Long long2 = Convert.toLong(str2);
        if (long1 == null) {
        	long1 = 0L;
        }
        if (long2 == null) {
        	long2 = 0L;
        }
        
		if (compareModeEnum == CompareModeEnum.小于) {
			return long1 < long2;
		} else if (compareModeEnum == CompareModeEnum.大于) {
			return long1 > long2;
		} else if (compareModeEnum == CompareModeEnum.等于) {
			return long1 == long2;
		} else if (compareModeEnum == CompareModeEnum.小于等于) {
			return long1 <= long2;
		} else if (compareModeEnum == CompareModeEnum.大于等于) {
			return long1 >= long2;
		}
		
		return false;
	}
	
}
