package ai.yue.library.base.crypto.config;

import ai.yue.library.base.crypto.annotation.RequestDecryptAnnotationAdvice;
import ai.yue.library.base.crypto.annotation.ResponseEncryptAnnotationAdvice;
import ai.yue.library.base.crypto.aspect.key.exchange.RequestBodyDecryptAdvice;
import ai.yue.library.base.crypto.aspect.key.exchange.ResponseEncryptAdvice;
import ai.yue.library.base.crypto.config.properties.CryptoProperties;
import ai.yue.library.base.crypto.config.properties.KeyExchangeProperties;
import ai.yue.library.base.crypto.controller.key.exchange.KeyExchangeController;
import ai.yue.library.base.crypto.dao.key.exchange.BothCacheExchangeStorage;
import ai.yue.library.base.crypto.dao.key.exchange.CaffeineExchangeStorage;
import ai.yue.library.data.redis.client.Redis;
import com.alicp.jetcache.CacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * base-crypto配置，提供自动配置项支持与增强
 * 
 * @author	ylyue
 * @since	2018年6月11日
 */
@Slf4j
@Configuration
@Import({KeyExchangeController.class, RequestDecryptAnnotationAdvice.class, ResponseEncryptAnnotationAdvice.class, RequestBodyDecryptAdvice.class, ResponseEncryptAdvice.class})
@EnableConfigurationProperties({ CryptoProperties.class, KeyExchangeProperties.class })
public class BaseCryptoAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = KeyExchangeProperties.PREFIX, name = "key-exchange-storage-type", havingValue = "LOCAL_CACHE", matchIfMissing = true)
    public CaffeineExchangeStorage mapKeyExchangeStorage(KeyExchangeProperties keyExchangeProperties) {
        return new CaffeineExchangeStorage(keyExchangeProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass({Redis.class, RedisConnectionFactory.class})
    @ConditionalOnProperty(prefix = KeyExchangeProperties.PREFIX, name = "key-exchange-storage-type", havingValue = "BOTH_CACHE")
    public BothCacheExchangeStorage redisKeyExchangeStorage(KeyExchangeProperties keyExchangeProperties, CacheManager cacheManager) {
        return new BothCacheExchangeStorage(keyExchangeProperties, cacheManager);
    }

}
