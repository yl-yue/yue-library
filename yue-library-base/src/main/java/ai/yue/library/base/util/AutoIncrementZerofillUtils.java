package ai.yue.library.base.util;

import ai.yue.library.base.exception.ParamException;
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
	 * @return 如：0001
	 */
	public static String getInitValue(int length) {
		return StrUtil.padPre("1", length, '0');
	}
	
	/**
	 * 字符串尾部值自动递增
	 * @param str 尾部值是 {@linkplain Integer} 类型
	 * @return 自动递增后的值
	 * @throws ParamException 如：("999", "str999")
	 */
	public static String autoIncrement(String str) {
		int maxIndex = str.length() - 1;
		Integer autoIncrementValue = Integer.parseInt(CharUtil.toString(str.charAt(maxIndex))) + 1;
		if (autoIncrementValue == 10) {
			int cycleIndex = 0;
			for (int i = maxIndex - 1; i >= 0; i--) {
				Integer autoIncrementValueI = Integer.parseInt(CharUtil.toString(str.charAt(i))) + 1;
				cycleIndex++;
				if (autoIncrementValueI != 10) {
					String pad = StrUtil.padPre("0", cycleIndex, '0');
					String replaceValue = autoIncrementValueI.toString() + pad;
					return StringUtils.replace(str, replaceValue, i, i + 1 + replaceValue.length());
				}
			}
			
			throw new ParamException("无法自动递增，此参数已是最大值：" + str);
		}
		
		return str.substring(0, maxIndex) + autoIncrementValue;
	}
	
	/**
	 * 字符串尾部值自动递减
	 * @param str 尾部值是 {@linkplain Integer} 类型
	 * @return 自动递减后的值
	 */
	public static String autoDecr(String str) {
		int maxIndex = str.length() - 1;
		Integer autoDecrValue = Integer.parseInt(CharUtil.toString(str.charAt(maxIndex))) - 1;
		if (autoDecrValue == -1) {
			int cycleIndex = 0;
			for (int i = maxIndex - 1; i >= 0; i--) {
				Integer autoDecrValueI = Integer.parseInt(CharUtil.toString(str.charAt(i))) - 1;
				cycleIndex++;
				if (autoDecrValueI != -1) {
					String pad = StrUtil.padPre("9", cycleIndex, '9');
					String replaceValue = autoDecrValueI.toString() + pad;
					return StringUtils.replace(str, replaceValue, i, i + 1 + replaceValue.length());
				}
			}
			
			throw new ParamException("无法自动递减，此参数已是最小值：" + str);
		}
		
		return str.substring(0, maxIndex) + autoDecrValue;
	}
	
}
