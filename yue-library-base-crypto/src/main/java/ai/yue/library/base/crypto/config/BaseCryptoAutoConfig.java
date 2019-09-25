package ai.yue.library.base.crypto.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import ai.yue.library.base.crypto.config.properties.CryptoProperties;
import ai.yue.library.base.util.StringUtils;
import cn.hutool.core.lang.Singleton;
import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;

/**
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
		String aes_keyt = cryptoProperties.getAes_keyt();
		if (StringUtils.isNotEmpty(aes_keyt)) {
			Singleton.put(SecureUtil.aes(aes_keyt.getBytes()));
			log.info("【初始化工具-SecureSingleton】AES单例配置 ... 已初始化完毕。");
		}
		
		// RSA
		String rsa_public_keyt = cryptoProperties.getRsa_public_keyt();
		String rsa_private_keyt = cryptoProperties.getRsa_private_keyt();
		if (StringUtils.isNotEmpty(rsa_public_keyt) || StringUtils.isNotEmpty(rsa_private_keyt)) {
			Singleton.put(SecureUtil.rsa(rsa_private_keyt, rsa_public_keyt));
			log.info("【初始化工具-SecureSingleton】RSA单例配置 ... 已初始化完毕。");
		}
	}
	
}
