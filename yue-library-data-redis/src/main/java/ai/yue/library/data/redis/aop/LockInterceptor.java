package ai.yue.library.data.redis.aop;

import ai.yue.library.base.util.ClassUtils;
import ai.yue.library.data.redis.annotation.Lock;
import ai.yue.library.data.redis.client.LockClient;
import ai.yue.library.data.redis.config.properties.RedisProperties;
import ai.yue.library.data.redis.custom.DefaultLockFailureStrategy;
import ai.yue.library.data.redis.custom.DefaultLockKeyBuilder;
import ai.yue.library.data.redis.custom.LockFailureStrategy;
import ai.yue.library.data.redis.custom.LockKeyBuilder;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.redisson.api.RLock;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分布式锁aop处理器
 */
@Slf4j
@RequiredArgsConstructor
public class LockInterceptor implements MethodInterceptor,InitializingBean {

    private final Map<Class<? extends LockKeyBuilder>, LockKeyBuilder> keyBuilderMap = new LinkedHashMap<>();
    private final Map<Class<? extends LockFailureStrategy>, LockFailureStrategy> failureStrategyMap = new LinkedHashMap<>();

    private final RedisProperties.LockProperties lockProperties;
    private final LockClient lockClient;
    private final List<LockKeyBuilder> keyBuilders;
    private final List<LockFailureStrategy> failureStrategies;
    private LockOperation defaultLockOperation;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // 解决使用其他aop组件时，aop切了两次的问题
        Class<?> cls = AopProxyUtils.ultimateTargetClass(invocation.getThis());
        if (!cls.equals(invocation.getThis().getClass())) {
            return invocation.proceed();
        }

        Lock lock = AnnotatedElementUtils.findMergedAnnotation(invocation.getMethod(), Lock.class);
        RLock lockInstance = null;
        LockOperation lockOperation = buildLockOperation(lock);

        // 处理redis key前缀
        String lockPrefix = lockProperties.getLockKeyPrefix() + ":";
        if (StringUtils.hasText(lock.name())) {
            lockPrefix += lock.name() + ":";
        } else {
            lockPrefix += ClassUtils.getMethodReferencePath(invocation.getMethod()) + ":";
        }

        // 尝试获得锁实例
        String key = lockPrefix + lockOperation.lockKeyBuilder.buildKey(invocation, lock.keys());
        try {
            lockInstance = lockClient.lock(key, lock.expire(), lock.acquireTimeout());
            if (lockInstance != null) {
                return invocation.proceed();
            } else {
                // 调用锁失败处理策略
                lockOperation.lockFailureStrategy.onLockFailure(key, invocation.getMethod(), invocation.getArguments());
                return null;
            }
        } finally {
            if (lockInstance != null && lock.autoUnlock()) {
                final boolean unlock = lockClient.unlock(lockInstance);
                if (!unlock) {
                    log.warn("unlock fail, lockKey={}", lockInstance.getName());
                }
            }
        }
    }

    @Override
    public void afterPropertiesSet() {
        keyBuilderMap.putAll(keyBuilders.stream().collect(Collectors.toMap(LockKeyBuilder::getClass, x -> x)));
        failureStrategyMap.putAll(failureStrategies.stream().collect(Collectors.toMap(LockFailureStrategy::getClass, x -> x)));

        LockKeyBuilder lockKeyBuilder;
        LockFailureStrategy lockFailureStrategy;

        Class<? extends LockKeyBuilder> defaultKeyBuilder = lockProperties.getDefaultKeyBuilder();
        if (defaultKeyBuilder != null) {
            lockKeyBuilder = keyBuilderMap.get(defaultKeyBuilder);
        } else {
            lockKeyBuilder = keyBuilders.get(0);
        }

        Class<? extends LockFailureStrategy> defaultFailureStrategy = lockProperties.getDefaultFailureStrategy();
        if (defaultFailureStrategy != null) {
            lockFailureStrategy = failureStrategyMap.get(lockProperties.getDefaultFailureStrategy());
        } else {
            lockFailureStrategy = failureStrategies.get(0);
        }

        defaultLockOperation = LockOperation.builder().lockKeyBuilder(lockKeyBuilder).lockFailureStrategy(lockFailureStrategy).build();
    }

    @Builder
    private static class LockOperation {
        /**
         * key生成器
         */
        private LockKeyBuilder lockKeyBuilder;
        /**
         * 锁失败策略
         */
        private LockFailureStrategy lockFailureStrategy;
    }

    private LockOperation buildLockOperation(Lock lock) {
        LockKeyBuilder lockKeyBuilder;
        LockFailureStrategy lockFailureStrategy;
        Class<? extends LockKeyBuilder> keyBuilderStrategy = lock.keyBuilderStrategy();
        Class<? extends LockFailureStrategy> failStrategy = lock.failStrategy();

        if (keyBuilderStrategy == null || keyBuilderStrategy == DefaultLockKeyBuilder.class) {
            lockKeyBuilder = defaultLockOperation.lockKeyBuilder;
        } else {
            lockKeyBuilder = keyBuilderMap.get(keyBuilderStrategy);
        }

        if (failStrategy == null || failStrategy == DefaultLockFailureStrategy.class) {
            lockFailureStrategy = defaultLockOperation.lockFailureStrategy;
        } else {
            lockFailureStrategy = failureStrategyMap.get(failStrategy);
        }

        return LockOperation.builder().lockKeyBuilder(lockKeyBuilder).lockFailureStrategy(lockFailureStrategy).build();
    }

}
