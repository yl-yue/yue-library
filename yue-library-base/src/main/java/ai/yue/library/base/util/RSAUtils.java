package ai.yue.library.base.util;

import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.springframework.util.Base64Utils;

import ai.yue.library.base.exception.DecryptException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author  孙金川
 * @version 创建时间：2018年3月29日
 */
@Slf4j
public class RSAUtils {
	
	/**
     * 加密算法RSA
     */
	private static final String KEY_ALGORITHM = "RSA";
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * RSA最大解密密文大小 
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

	static void init() {
		// 1.初始化发送方密钥
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
			keyPairGenerator.initialize(1024);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
			RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
			System.out.println("Public Key:" + Base64Utils.encodeToString(rsaPublicKey.getEncoded()));
			System.out.println("Private Key:" + Base64Utils.encodeToString(rsaPrivateKey.getEncoded()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 加密
	 * @param content
	 * @param rsaPublicKey
	 * @return
	 */
	public static String encrypt(String content, String rsaPublicKey) {
		// 4.公钥加密、私钥解密 ---- 加密
		try {
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64Utils.decodeFromString(rsaPublicKey));
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
			Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			
			byte[] data = content.getBytes();
			int inputLen = data.length;
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        int offSet = 0;
	        byte[] cache;
	        int i = 0;
	        // 对数据分段加密
	        while (inputLen - offSet > 0) {
	            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
	                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
	            } else {
	                cache = cipher.doFinal(data, offSet, inputLen - offSet);
	            }
	            out.write(cache, 0, cache.length);
	            i++;
	            offSet = i * MAX_ENCRYPT_BLOCK;
	        }
	        byte[] encryptedData = out.toByteArray();
	        out.close();
	        
	        return Base64Utils.encodeToString(encryptedData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 解密
	 * @param content
	 * @param rsaPrivateKey
	 * @return
	 */
	public static String decrypt(String content, String rsaPrivateKey) {
		// 5.私钥解密、公钥加密 ---- 解密
		try {
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64Utils.decodeFromString(rsaPrivateKey));
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
			Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			
			byte[] encryptedData = Base64Utils.decodeFromString(content);
			int inputLen = encryptedData.length;
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        int offSet = 0;
	        byte[] cache;
	        int i = 0;
	        // 对数据分段解密
	        while (inputLen - offSet > 0) {
	            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
	                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
	            } else {
	                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
	            }
	            out.write(cache, 0, cache.length);
	            i++;
	            offSet = i * MAX_DECRYPT_BLOCK;
	        }
	        byte[] decryptedData = out.toByteArray();
	        out.close();
	        
	        return new String(decryptedData);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("【RSA解密错误】解密内容 = {}", content);
			throw new DecryptException(e.getMessage());
		}
	}

}
