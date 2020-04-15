package ai.yue.library.data.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.data.redis.serializer.StringRedisSerializer;

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
	 * <p>默认FastJson进行Redis存储对象序列/反序列化
	 * <p>https://github.com/alibaba/fastjson/wiki/%E5%9C%A8-Spring-%E4%B8%AD%E9%9B%86%E6%88%90-Fastjson
	 */
	@Bean
	public RedisTemplate<String, Object> yueRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		
		// 默认使用FastJson进行Redis存储对象序列/反序列化
//		redisTemplate.setDefaultSerializer(redisProperties.getRedisSerializerEnum().getRedisSerializer());
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(stringRedisSerializer);
		redisTemplate.setHashKeySerializer(stringRedisSerializer);
		return redisTemplate;
	}
	
	@Bean
	@Primary
	@ConditionalOnBean({ RedisTemplate.class, StringRedisTemplate.class })
	public Redis redis(@Qualifier("yueRedisTemplate") RedisTemplate<String, Object> redisTemplate, StringRedisTemplate stringRedisTemplate) {
		log.info("【初始化配置-Redis客户端】配置项：yue.redis，默认使用FastJson进行Redis存储对象序列/反序列化。Bean：Redis ... 已初始化完毕。");
		return new Redis(redisTemplate, stringRedisTemplate);
	}
	
	@Bean
	@Primary
	@ConditionalOnBean(Redis.class)
	public User user() {
		return new User();
	}
	
}
