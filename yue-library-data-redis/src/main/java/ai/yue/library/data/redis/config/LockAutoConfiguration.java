package ai.yue.library.data.redis.config;

import ai.yue.library.data.redis.aop.LockAnnotationAdvisor;
import ai.yue.library.data.redis.aop.LockInterceptor;
import ai.yue.library.data.redis.client.LockClient;
import ai.yue.library.data.redis.client.Redis;
import ai.yue.library.data.redis.config.properties.RedisProperties;
import ai.yue.library.data.redis.custom.DefaultLockFailureStrategy;
import ai.yue.library.data.redis.custom.DefaultLockKeyBuilder;
import ai.yue.library.data.redis.custom.LockFailureStrategy;
import ai.yue.library.data.redis.custom.LockKeyBuilder;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;

import java.util.List;

/**
 * 分布式锁自动配置器
 */
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class LockAutoConfiguration {

    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    @ConditionalOnMissingBean
    public LockClient lockClient(RedisProperties redisProperties, Redis redis) {
        return new LockClient(redisProperties.getLock(), redis.getRedisson());
    }

    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    @ConditionalOnMissingBean
    public LockKeyBuilder lockKeyBuilder(BeanFactory beanFactory) {
        return new DefaultLockKeyBuilder(beanFactory);
    }

    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    @ConditionalOnMissingBean
    public LockFailureStrategy lockFailureStrategy() {
        return new DefaultLockFailureStrategy();
    }

    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    @ConditionalOnMissingBean
    public LockInterceptor lockInterceptor(RedisProperties redisProperties, @Lazy LockClient lockClient, List<LockKeyBuilder> keyBuilders, List<LockFailureStrategy> failureStrategies) {
        return new LockInterceptor(redisProperties.getLock(), lockClient, keyBuilders, failureStrategies);
    }

    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    @ConditionalOnMissingBean
    public LockAnnotationAdvisor lockAnnotationAdvisor(LockInterceptor lockInterceptor) {
        return new LockAnnotationAdvisor(lockInterceptor, Ordered.HIGHEST_PRECEDENCE);
    }

}
