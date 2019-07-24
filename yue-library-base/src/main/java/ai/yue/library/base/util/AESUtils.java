package ai.yue.library.base.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.util.Base64Utils;

import ai.yue.library.base.exception.DecryptException;
import lombok.extern.slf4j.Slf4j;

/**
 * AES 加密工具类
 * 
 * @author	孙金川
 * @since	2018年3月28日
 */
@Slf4j
public class AESUtils {

	private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//默认的加密算法

    /**
     * AES 加密操作
     *
     * @param content 	待加密内容
     * @param keyt 		密钥
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String keyt) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器

            byte[] byteContent = content.getBytes("utf-8");

            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(keyt));// 初始化为加密模式的密码器
            
            byte[] encryptedData = cipher.doFinal(byteContent);// 加密
            
            return Base64Utils.encodeToString(encryptedData);//Base64转码
        } catch (Exception e) {
        	log.error("AES加密失败，请检查参数：", e);
        }
        
        return null;
    }
    
    /**
     * AES 解密操作
     *
     * @param content 待解密内容
     * @param keyt 密钥
     * @return 解密结果
     */
    public static String decrypt(String content, String keyt) {
        try {
            //实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(keyt));

            //执行操作
            byte[] encryptedData = Base64Utils.decodeFromString(content);
            byte[] decryptedData = cipher.doFinal(encryptedData);
            
            return new String(decryptedData, "utf-8");
        } catch (Exception e) {
        	e.printStackTrace();
			log.error("【AES解密错误】解密内容 = {}", content);
			throw new DecryptException(e.getMessage());
        }
    }

    /**
     * 生成加密秘钥
     * @param password 密钥
     * @return AES密钥
     */
    private static SecretKeySpec getSecretKey(final String password) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        try {
        	KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        	SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        	secureRandom.setSeed(password.getBytes());
            //AES 要求密钥长度为 128
            kg.init(128, secureRandom);

            //生成一个密钥
            SecretKey secretKey = kg.generateKey();

            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥
        } catch (NoSuchAlgorithmException e) {
        	log.error("AES生成加密秘钥失败，请检查参数：", e);
        }
        
        return null;
    }
    
}
