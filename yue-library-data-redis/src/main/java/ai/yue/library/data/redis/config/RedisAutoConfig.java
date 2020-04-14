package ai.yue.library.data.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;

import ai.yue.library.data.redis.client.Redis;
import ai.yue.library.data.redis.client.User;
import ai.yue.library.data.redis.client.WxMaUser;
import ai.yue.library.data.redis.config.properties.QqProperties;
import ai.yue.library.data.redis.config.properties.RedisProperties;
import ai.yue.library.data.redis.config.properties.WxOpenProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * redis自动配置
 * 
 * @author	ylyue
 * @since	2018年6月11日
 */
@Slf4j
@Configuration
@Import({ WxMaUser.class })
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties({ RedisProperties.class, WxOpenProperties.class, QqProperties.class })
public class RedisAutoConfig {
	
	@Autowired
	RedisProperties redisProperties;
	
	/**
	 * <p>使用FastJson进行Redis存储对象序列/反序列化
	 * <p>https://github.com/alibaba/fastjson/wiki/%E5%9C%A8-Spring-%E4%B8%AD%E9%9B%86%E6%88%90-Fastjson
	 */
	@Bean
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		
		// 使用FastJson进行Redis存储对象序列/反序列化
		if (redisProperties.isEnabledFastJsonRedisSerializer()) {
			GenericFastJsonRedisSerializer fastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
			// 设置默认的Serialize，包含 keySerializer & valueSerializer
			redisTemplate.setDefaultSerializer(fastJsonRedisSerializer);
			log.info("【初始化配置-GenericFastJsonRedisSerializer】默认配置为true，当前环境为true：使用FastJson进行Redis存储对象序列/反序列化。Bean：RedisTemplate<Object, Object> ... 已初始化完毕。");
		}
		
		return redisTemplate;
	}
	
	/**
	 * <p>使用FastJson进行Redis存储对象序列/反序列化
	 * <p>https://github.com/alibaba/fastjson/wiki/%E5%9C%A8-Spring-%E4%B8%AD%E9%9B%86%E6%88%90-Fastjson
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplateString(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		
		// 使用FastJson进行Redis存储对象序列/反序列化
		if (redisProperties.isEnabledFastJsonRedisSerializer()) {
			GenericFastJsonRedisSerializer fastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
			// 设置默认的Serialize，包含 keySerializer & valueSerializer
			redisTemplate.setDefaultSerializer(fastJsonRedisSerializer);
			log.info("【初始化配置-GenericFastJsonRedisSerializer】默认配置为false，当前环境为true：使用FastJson进行Redis存储对象序列/反序列化。Bean：RedisTemplate<String, Object> ... 已初始化完毕。");
		}
		
		return redisTemplate;
	}
	
	@Bean
	@Primary
	@ConditionalOnBean({ RedisTemplate.class, StringRedisTemplate.class })
	public Redis redis(RedisTemplate<Object, Object> redisTemplate, StringRedisTemplate stringRedisTemplate) {
		return new Redis(redisTemplate, stringRedisTemplate);
	}
	
	@Bean
	@Primary
	@ConditionalOnBean(Redis.class)
	public User user() {
		return new User();
	}
	
}
