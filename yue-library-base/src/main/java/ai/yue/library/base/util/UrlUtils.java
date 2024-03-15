package ai.yue.library.base.util;

import cn.hutool.core.util.URLUtil;
import org.springframework.web.util.UriUtils;

/**
 * URL处理
 *
 * @author	ylyue
 * @since	2018年4月24日
 */
public class UrlUtils extends URLUtil {

    /**
     * The default encoding for Url encode/decode: <kbd>UTF-8</kbd>.
     */
	public static final String DEFAULT_ENCODING = "UTF-8";

	/**
	 * 是否通配符匹配
	 * <p>Url前缀或后缀通配符匹配（只能同时存在一种通配）</p>
	 *
	 * @param normalUrl    实际的Url
	 * @param wildcardUrls 通配符Url数组
	 * @return 是否匹配
	 */
	public static boolean isUrlWildcardMatching(String normalUrl, String[] wildcardUrls) {
		for (String url : wildcardUrls) {
			// 1. 前通配
			if (!url.endsWith("**")) {
				if (normalUrl.startsWith("/")) {
					url = url.substring(2);
				} else {
					url = url.substring(3);
				}
				if (normalUrl.endsWith(url)) {
					return true;
				}
			} else {
				// 2. 后通配
				url = url.substring(0, url.length() - 3);
				if (normalUrl.startsWith(url)) {
					return true;
				}
			}
		}
		
		return false;
	}

	/**
	 * Url编码
	 *
	 * @param source 要编码的字符串
	 * @return 编码后的字符串
	 */
	public static String encode(String source) {
		return UriUtils.encode(source, DEFAULT_ENCODING);
	}

	/**
	 * Url解码
	 *
	 * @param source 要解码的字符串
	 * @return 解码后的字符串
	 */
	public static String decode(String source) {
		return UriUtils.decode(source, DEFAULT_ENCODING);
	}
	
}
