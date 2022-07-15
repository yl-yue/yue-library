package ai.yue.library.auth.service.config;

import ai.yue.library.auth.client.config.AuthClientAutoConfig;
import ai.yue.library.auth.service.client.User;
import ai.yue.library.auth.service.client.WxMaUser;
import ai.yue.library.auth.service.config.properties.AuthServiceProperties;
import ai.yue.library.auth.service.config.properties.QqProperties;
import ai.yue.library.auth.service.config.properties.WxOpenProperties;
import ai.yue.library.data.redis.client.Redis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

/**
 * AuthService自动配置
 * 
 * @author	ylyue
 * @since	2018年6月11日
 */
@Slf4j
@Configuration
@Import({ WxMaUser.class })
@AutoConfigureAfter(AuthClientAutoConfig.class)
@EnableConfigurationProperties({ AuthServiceProperties.class, WxOpenProperties.class, QqProperties.class })
public class AuthServiceAutoConfig {
	
	@Bean
	@Primary
	@ConditionalOnBean(Redis.class)
	public User user() {
		log.info("【初始化配置-AuthService-User客户端】配置项：" + AuthServiceProperties.PREFIX + "。Bean：User ... 已初始化完毕。");
		return new User();
	}

}
