package ai.yue.library.base.crypto.client;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.util.UrlUtils;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.lang.Singleton;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.AES;

/**
 * 加解密单例
 * 
 * @author	ylyue
 * @since	2019年8月9日
 */
public class SecureSingleton {

	/**
	 * 获取自动配置单例 - AES
	 * 
	 * @return AES 单例
	 */
	public static AES getAES() {
		return Singleton.get(AES.class);
	}
	
	/**
	 * 获取自动配置单例 - RSA
	 * 
	 * @return RSA 单例
	 */
	public static RSA getRSA() {
		return Singleton.get(RSA.class);
	}
	
	/**
	 * 1. 将URI转义内容进行解码<br>
	 * 2. 将RSA分段加密内容，进行分段解密
	 * 
	 * @param messageBody URI转义后的消息体
	 * @return 解密后的JSON
	 */
	public static JSONObject rsaUriDecodingAndDecrypt(String messageBody) {
		String content = UrlUtils.decode(messageBody);
		String jsonString = getRSA().decryptStrFromBcd(content, KeyType.PrivateKey);
		JSONObject json = null;
		try {
			json = JSONObject.parseObject(jsonString);
		}catch (Exception e) {
			throw new ConvertException(e.getMessage());
		}
		
		return json;
	}
	
}
