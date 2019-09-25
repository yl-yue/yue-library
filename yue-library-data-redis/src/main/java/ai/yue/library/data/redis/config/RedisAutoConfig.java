package ai.yue.library.data.redis.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.StringRedisTemplate;

import ai.yue.library.data.redis.client.Redis;
import ai.yue.library.data.redis.client.User;
import ai.yue.library.data.redis.client.WxMaUser;
import ai.yue.library.data.redis.config.properties.ConstantProperties;
import ai.yue.library.data.redis.config.properties.QqProperties;
import ai.yue.library.data.redis.config.properties.WxOpenProperties;

/**
 * @author	ylyue
 * @since	2018年6月11日
 */
@Configuration
@Import({ WxMaUser.class })
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties({ ConstantProperties.class, WxOpenProperties.class, QqProperties.class })
public class RedisAutoConfig {
	
	@Bean
	@Primary
	@ConditionalOnBean(StringRedisTemplate.class)
	public Redis redis(StringRedisTemplate stringRedisTemplate) {
		return new Redis(stringRedisTemplate);
	}
	
	@Bean
	@Primary
	@ConditionalOnBean(Redis.class)
	public User user() {
		return new User();
	}
	
}
