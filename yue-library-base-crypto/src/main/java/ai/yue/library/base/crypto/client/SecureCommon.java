package ai.yue.library.base.crypto.client;

import cn.hutool.v7.core.codec.binary.Base64;
import cn.hutool.v7.core.net.url.UrlEncoder;
import cn.hutool.v7.crypto.SecureUtil;
import cn.hutool.v7.crypto.digest.mac.HmacAlgorithm;

/**
 * 加解密-常用业务
 * 
 * @author	ylyue
 * @since	2019年8月9日
 */
public class SecureCommon {

	/**
	 * 钉钉机器人签名
	 * 
	 * @param dingtalkRobotWebhook 钉钉机器人Webhook
	 * @param dingtalkRobotSignSecret 钉钉机器人密钥，机器人安全设置页面，加签一栏下面显示的SEC开头的字符串
	 * @return 钉钉机器人签名后的Webhook
	 */
	public static String dingtalkRobotSign(String dingtalkRobotWebhook, String dingtalkRobotSignSecret) {
        Long timestamp = System.currentTimeMillis();
        String signContent = timestamp + "\n" + dingtalkRobotSignSecret;
        byte[] signByte = SecureUtil.hmac(HmacAlgorithm.HmacSHA256, dingtalkRobotSignSecret).digest(signContent);
        String sign = UrlEncoder.encodeQuery(Base64.encode(signByte));
        return dingtalkRobotWebhook + "&timestamp=" + timestamp + "&sign=" + sign;
	}
	
}
