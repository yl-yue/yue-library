package ai.yue.library.base.util;

import org.springframework.web.util.UriUtils;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.exception.JSONObjectException;

/**
 * @author  孙金川
 * @version 创建时间：2018年4月24日
 */
public class URIUtils {

    /**
     * The default encoding for URI encode/decode: <kbd>UTF-8</kbd>.
     */
	public static final String DEFAULT_ENCODING = "UTF-8";
	
	/**
	 * URI前缀或后缀通配符匹配（只能同时存在一种通配）
	 * @param array 包含通配符的URI的数组
	 * @param uri 实际的URI
	 * @return
	 */
	public static boolean isUriArraySuffixOrPrefixWildcard(String[] array, String uri) {
		for (String url : array) {
			// 1. 前通配
			if (!url.endsWith("**")) {
				if (uri.startsWith("/")) {
					url = url.substring(2);
				} else {
					url = url.substring(3);
				}
				if (uri.endsWith(url)) {
					return true;
				}
			} else {
				// 2. 后通配
				url = url.substring(0, url.length() - 3);
				if (uri.startsWith(url)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * URI编码
	 * @param source 要编码的字符串
	 * @return
	 */
	public static String encode(String source) {
		return UriUtils.encode(source, DEFAULT_ENCODING);
	}
	
	/**
	 * URI解码
	 * @param source 要解码的字符串
	 * @return
	 */
	public static String decode(String source) {
		return UriUtils.decode(source, DEFAULT_ENCODING);
	}
	
	/**
	 * 1. 将URI转义内容进行解码<br>
	 * 2. 将RSA 1024分段加密内容，进行分段解密
	 * @param text				URI转义后的文本
	 * @param rsa_private_key	RSA1024私钥
	 * @return
	 */
	public static JSONObject rsaUriDecodingAndDecrypt(String text, String rsa_private_key) {
		String content = decode(text);
		String jsonString = RSAUtils.decrypt(content, rsa_private_key);
		JSONObject json = null;
		try {
			json = JSONObject.parseObject(jsonString);
		}catch (Exception e) {
			throw new JSONObjectException(e.getMessage());
		}
		return json;
	}
	
}
