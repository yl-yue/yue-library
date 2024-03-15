package ai.yue.library.base.crypto.config;

import ai.yue.library.base.crypto.config.properties.CryptoProperties;
import ai.yue.library.base.util.StrUtils;
import cn.hutool.core.lang.Singleton;
import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * base-crypto配置，提供自动配置项支持与增强
 * 
 * @author	ylyue
 * @since	2018年6月11日
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({ CryptoProperties.class })
public class BaseCryptoAutoConfig {
	
	@Autowired
	private void init(CryptoProperties cryptoProperties) {
		// AES
		String aes_keyt = cryptoProperties.getAesKeyt();
		if (StrUtils.isNotEmpty(aes_keyt)) {
			Singleton.put(SecureUtil.aes(aes_keyt.getBytes()));
			log.info("【初始化工具-SecureSingleton】AES单例配置 ... 已初始化完毕。");
		}
		
		// RSA
		String rsa_public_keyt = cryptoProperties.getRsaPublicKeyt();
		String rsa_private_keyt = cryptoProperties.getRsaPrivateKeyt();
		if (StrUtils.isNotEmpty(rsa_public_keyt) || StrUtils.isNotEmpty(rsa_private_keyt)) {
			Singleton.put(SecureUtil.rsa(rsa_private_keyt, rsa_public_keyt));
			log.info("【初始化工具-SecureSingleton】RSA单例配置 ... 已初始化完毕。");
		}
	}
	
}
