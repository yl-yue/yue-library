package ai.yue.library.test.docs.example.base.crypto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.yue.library.base.crypto.client.SecureSingleton;
import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.R;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.AES;

/**
 * @author	ylyue
 * @since	2019年8月9日
 */
@RestController
@RequestMapping("/cryptoExample")
public class CryptoExample {

	@GetMapping("/secure")
	public Result<?> secure() {
		// AES加密
		AES aes = SecureSingleton.getAES();
		aes.encryptBase64("");
		aes.decryptStr("");
		
		// RSA加密-公钥加密，私有解密
		RSA rsa = SecureSingleton.getRSA();
		rsa.encryptBcd("", KeyType.PublicKey);
		rsa.decryptStrFromBcd("", KeyType.PrivateKey);
		return R.success();
	}
	
}
