package ai.yue.library.data.redis.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.StringRedisTemplate;

import ai.yue.library.data.redis.client.Redis;

/**
 * @author  孙金川
 * @version 创建时间：2018年6月11日
 */
@Configuration
@ConditionalOnBean(StringRedisTemplate.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisAutoConfig {
	
	@Bean
	@Primary
	public Redis redis(StringRedisTemplate stringRedisTemplate) {
		return new Redis(stringRedisTemplate);
	}
	
}
