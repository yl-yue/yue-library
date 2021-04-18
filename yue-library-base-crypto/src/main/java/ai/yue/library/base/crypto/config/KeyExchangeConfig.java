package ai.yue.library.base.crypto.config;

import ai.yue.library.base.crypto.annotation.key.exchange.RequestDecryptHandler;
import ai.yue.library.base.crypto.annotation.key.exchange.ResponseEncryptHandler;
import ai.yue.library.base.crypto.config.properties.KeyExchangeProperties;
import ai.yue.library.base.crypto.controller.key.exchange.KeyExchangeController;
import ai.yue.library.base.crypto.dao.key.exchange.MapKeyExchangeStorage;
import ai.yue.library.base.crypto.dao.key.exchange.RedisKeyExchangeStorage;
import ai.yue.library.base.crypto.dto.key.exchange.KeyExchangeDTO;
import ai.yue.library.base.util.SpringUtils;
import ai.yue.library.data.redis.client.Redis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 密钥交换Bean配置
 *
 * @author	ylyue
 * @since	2018年6月11日
 */
@Slf4j
@Configuration
@Import({KeyExchangeController.class, RequestDecryptHandler.class, ResponseEncryptHandler.class})
@EnableConfigurationProperties({ KeyExchangeProperties.class })
@ConditionalOnProperty(prefix = KeyExchangeProperties.PREFIX, name = "enabled", havingValue = "true")
public class KeyExchangeConfig {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = KeyExchangeProperties.PREFIX, name = "key-exchange-storage-type", havingValue = "LOCAL_MAP", matchIfMissing = true)
	public MapKeyExchangeStorage mapKeyExchangeStorage() {
		return new MapKeyExchangeStorage();
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass({Redis.class, RedisConnectionFactory.class})
	@ConditionalOnProperty(prefix = KeyExchangeProperties.PREFIX, name = "key-exchange-storage-type", havingValue = "REDIS")
	public RedisKeyExchangeStorage redisKeyExchangeStorage() {
		RedisTemplate<String, KeyExchangeDTO> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(SpringUtils.getBean(RedisConnectionFactory.class));
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(stringRedisSerializer);
		redisTemplate.afterPropertiesSet();
		return new RedisKeyExchangeStorage(redisTemplate);
	}

}
