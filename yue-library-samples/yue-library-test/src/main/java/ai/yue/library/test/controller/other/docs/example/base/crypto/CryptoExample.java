package ai.yue.library.test.controller.other.docs.example.base.crypto;

import ai.yue.library.base.crypto.client.SecureSingleton;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.AES;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
		rsa.encryptBase64("", KeyType.PublicKey);
		rsa.decryptStr("", KeyType.PrivateKey);
		return R.success();
	}
	
}
