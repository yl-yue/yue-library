package ai.yue.library.data.redis.config;

import ai.yue.library.base.util.SpringUtils;
import ai.yue.library.data.redis.client.Redis;
import ai.yue.library.data.redis.config.properties.RedisProperties;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * redis自动配置
 * 
 * @author	ylyue
 * @since	2018年6月11日
 */
@Slf4j
@Configuration
@EnableScheduling
@AutoConfigureAfter(RedissonAutoConfiguration.class)
@EnableConfigurationProperties({RedisProperties.class})
public class RedisAutoConfig {

	@Autowired
	RedisProperties redisProperties;

	@Bean
	@Primary
	public Redis redis(RedissonClient redisson, RedisCacheManager redisCacheManager, CacheProperties cacheProperties) {
		log.info("【初始化配置-Redis客户端】配置项：{}，使用 JsonCodec 进行Redis存储对象序列/反序列化。Bean：Redis ... 已初始化完毕。", RedisProperties.PREFIX);
		Config config = redisson.getConfig();
		config.setCodec(new JsonCodec());
		redisson = Redisson.create(config);

		// 解决@Cacheable、@CachePut注解，Redis序列化乱码的问题
		String keyPrefix = cacheProperties.getRedis().getKeyPrefix();
		RedisCacheConfiguration defaultCacheConfig = ((RedisCacheConfiguration) ReflectUtil.getFieldValue(redisCacheManager, "defaultCacheConfig"))
				// 解决双冒号问题（:: -> :）
				.computePrefixWith(new CacheKeyPrefix() {
					String SEPARATOR = ":";

					@Override
					public String compute(String cacheName) {
						if (keyPrefix != null) {
							return keyPrefix + cacheName + SEPARATOR;
						} else {
							return cacheName + SEPARATOR;
						}
					}
				})
				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericFastJsonRedisSerializer()));
		ReflectUtil.setFieldValue(redisCacheManager, "defaultCacheConfig", defaultCacheConfig);

		return new Redis((Redisson) redisson);
	}

	@Scheduled(cron = "0/30 * * * * *")
//	@Scheduled(cron = "0 * * * * *")
	public void heartbeat() {
		SpringUtils.getBean(Redis.class).getBucket("heartbeat").getAndSet(System.currentTimeMillis());
		log.debug("Redis heartbeat");
	}

}
