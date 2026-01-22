package ai.yue.library.base.util.rate.limiter;

import ai.yue.library.base.util.AsyncUtils;
import cn.hutool.v7.core.util.RuntimeUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.util.ObjectUtils;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 限流工具
 */
public class RateLimiterUtils {

    /**
     * 缓存限流器
     */
    private static final ConcurrentMap<String, RateLimiter> RATE_LIMITER_MAP = new ConcurrentHashMap();
    /**
     * 缓存令牌桶限流器
     */
    private static final Cache<String, TokenBucketRateLimiter> RATE_LIMITER_CACHE = CacheBuilder.newBuilder()
            .concurrencyLevel(RuntimeUtil.getProcessorCount())
            .maximumSize(50000)
            .build();

    /**
     * 获得一个缓存的限流器（Guava）
     *
     * @param limitId 限流标识
     * @param qps     qps
     */
    public static RateLimiter getCacheRateLimiter(String limitId, double qps) {
        RateLimiter rateLimiter = RATE_LIMITER_MAP.get(limitId);
        if (ObjectUtils.isEmpty(rateLimiter)) {
            return createRateLimiter(limitId, qps);
        }

        return rateLimiter;
    }

    private static synchronized RateLimiter createRateLimiter(String limitId, double qps) {
        RateLimiter rateLimiter = RateLimiter.create(qps);
        // 延迟500ms，等待RateLimiter初始化
        AsyncUtils.sleep(500);
        RATE_LIMITER_MAP.put(limitId, rateLimiter);
        return rateLimiter;
    }

    /**
     * 获得一个缓存的令牌桶限流器
     * - 此限流器支持不同时间单位，如：每秒钟限制多少、每分钟限制多少、每5分钟限制多少等
     * - 此缓存是个全局有界缓存，容量大小为50000，超过此界限后，会按照最近最少使用清理缓存
     *
     * @param limitId   限流标识
     * @param limit     限制数量
     * @param limitRate 限制速率，如：每秒、每5秒、每分钟等
     */
    public static TokenBucketRateLimiter getCacheTokenBucketRateLimiter(String limitId, int limit, Duration limitRate) {
        TokenBucketRateLimiter rateLimiter = RATE_LIMITER_CACHE.getIfPresent(limitId);
        if (ObjectUtils.isEmpty(rateLimiter)) {
            return createTokenBucketRateLimiter(limitId, limit, limitRate);
        }

        return rateLimiter;
    }

    private static synchronized TokenBucketRateLimiter createTokenBucketRateLimiter(String limitId, int limit, Duration limitRate) {
        TokenBucketRateLimiter rateLimiter = TokenBucketRateLimiter.create(limit, limitRate);
        RATE_LIMITER_CACHE.put(limitId, rateLimiter);
        return rateLimiter;
    }

}
