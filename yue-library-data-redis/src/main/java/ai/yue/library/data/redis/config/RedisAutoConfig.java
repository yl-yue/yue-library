package ai.yue.library.data.redis.config;

import ai.yue.library.base.util.SpringUtils;
import ai.yue.library.data.redis.cache.Fastjson2SpringEncoderParser;
import ai.yue.library.data.redis.client.Redis;
import ai.yue.library.data.redis.config.properties.RedisProperties;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
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
@EnableConfigurationProperties({RedisProperties.class, CacheProperties.class})
@Import({LockAutoConfiguration.class, Fastjson2SpringEncoderParser.class})
public class RedisAutoConfig {

	@Bean
	@Primary
	public Redis redis(RedissonClient redisson) {
		log.info("【初始化配置-Redis客户端】配置项：{}，使用 JsonCodec 进行Redis存储对象序列/反序列化。Bean：Redis ... 已初始化完毕。", RedisProperties.PREFIX);
		Config config = redisson.getConfig();
		config.setCodec(new JsonCodec());
		redisson = Redisson.create(config);
		return new Redis((Redisson) redisson);
	}

	@Scheduled(cron = "0/30 * * * * *")
	public void heartbeat() {
		SpringUtils.getBean(Redis.class).getBucket("heartbeat").getAndSet(System.currentTimeMillis());
		log.debug("Redis heartbeat");
	}

}
