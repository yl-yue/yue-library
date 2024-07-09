package ai.yue.library.web.config.qps.limit;

import ai.yue.library.base.util.AsyncUtils;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Rate Limiter Context
 */
class RateLimiterContext {

    /**
     * Cache a rateLimiter for each request address
     */
    private static final ConcurrentMap<String, RateLimiter> RATE_LIMITER_MAP = new ConcurrentHashMap();

    /**
     * cache request uri rate limiter
     *
     * @param requestUri request uri
     * @param qps        qps
     * @return RateLimiter
     */
    public static RateLimiter cacheRateLimiter(String requestUri, double qps) {
        RateLimiter rateLimiter = RATE_LIMITER_MAP.get(requestUri);
        if (ObjectUtils.isEmpty(rateLimiter)) {
            return getRateLimiter(requestUri, qps);
        }

        return rateLimiter;
    }

    private static synchronized RateLimiter getRateLimiter(String requestUri, double qps) {
        RateLimiter rateLimiter = RateLimiter.create(qps);
        // 延迟500ms，等待RateLimiter初始化
        AsyncUtils.sleep(500);
        RATE_LIMITER_MAP.put(requestUri, rateLimiter);
        return rateLimiter;
    }

}
