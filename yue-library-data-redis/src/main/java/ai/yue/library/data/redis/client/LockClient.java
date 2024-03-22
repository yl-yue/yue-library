package ai.yue.library.data.redis.client;

import ai.yue.library.base.exception.ResultException;
import ai.yue.library.base.view.R;
import ai.yue.library.data.redis.config.properties.RedisProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * redisson 重入锁客户端
 */
@Slf4j
@Data
@AllArgsConstructor
public class LockClient implements InitializingBean {

    private RedisProperties.LockProperties lockProperties;
    private final Redisson redisson;

    /**
     * 加锁
     *
     * @param key            锁key 同一个key只能被一个客户端持有
     * @param expire         过期时间(ms) 防止死锁
     * @param acquireTimeout 尝试获取锁超时时间(ms)
     * @return 加锁成功返回锁信息 失败返回null
     */
    public RLock lock(String key, long expire, long acquireTimeout) {
        acquireTimeout = acquireTimeout < 0 ? lockProperties.getAcquireTimeout() : acquireTimeout;
        long retryInterval = lockProperties.getRetryInterval();
        long start = System.currentTimeMillis();
        try {
            do {
                RLock lockInstance = acquire(key, expire, acquireTimeout);
                if (lockInstance != null) {
                    return lockInstance;
                }

                TimeUnit.MILLISECONDS.sleep(retryInterval);
            } while (System.currentTimeMillis() - start < acquireTimeout);
        } catch (InterruptedException e) {
            log.error("lock error", e);
            throw new ResultException(R.lockError(e.getMessage()));
        }

        return null;
    }

    /**
     * 解锁
     *
     * @param lockInstance 锁实例
     * @return 是否释放成功
     */
    public boolean unlock(RLock lockInstance) {
        if (lockInstance == null) {
            return false;
        }

        if (lockInstance.isHeldByCurrentThread()) {
            try {
                lockInstance.unlockAsync().get();
                return true;
            } catch (ExecutionException | InterruptedException e) {
                return false;
            }
        }

        return false;
    }

    /**
     * 尝试获得锁
     *
     * @param lockKey        锁标识
     * @param expire         锁有效时间
     * @param acquireTimeout 获取锁超时时间
     * @return 锁信息
     */
    private RLock acquire(String lockKey, long expire, long acquireTimeout) {
        try {
            final RLock lockInstance = redisson.getLock(lockKey);
            final boolean locked = lockInstance.tryLock(acquireTimeout, expire, TimeUnit.MILLISECONDS);
            return locked ? lockInstance : null;
        } catch (InterruptedException e) {
            return null;
        }
    }

    @Override
    public void afterPropertiesSet() {
        Assert.isTrue(lockProperties.getAcquireTimeout() >= 0, "tryTimeout must least 0");
        Assert.isTrue(lockProperties.getExpire() >= -1, "expireTime must lease -1");
        Assert.isTrue(lockProperties.getRetryInterval() >= 0, "retryInterval must more than 0");
        Assert.hasText(lockProperties.getLockKeyPrefix(), "lock key prefix must be not blank");
    }

}
