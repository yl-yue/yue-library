package ai.yue.library.base.util.rate.limiter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;

/**
 * 令牌桶限流
 * - 支持不同时间单位，如：每秒钟限制多少、每分钟限制多少、每5分钟限制多少等
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenBucketRateLimiter {

    /**
     * 桶内的最大令牌数量
     */
    private long maxTokens;

    /**
     * 桶内的当前令牌数量
     */
    private long currentTokens;

    /**
     * 令牌的上次生成时间
     */
    private long lastGenerateTime;

    /**
     * 创建令牌速率
     */
    private double generateTokenRate;

    /**
     * 限制速率，如：每秒、每5秒、每分钟等
     */
    private Duration limitRate;

    /**
     * 创建令牌桶限流器，如：
     * - 每秒限制5个请求
     * - 每30秒限制50个请求
     * - 每分钟限制100个请求
     * - 每三分钟限制200个请求
     *
     * @param limit     限制数量
     * @param limitRate 限制速率，如：每秒、每5秒、每分钟等
     */
    public static TokenBucketRateLimiter create(int limit, Duration limitRate) {
        return create(limit * 2, limit / 2, limit, limitRate);
    }

    /**
     * 创建令牌桶限流器，如：
     * - 每秒限制5个请求
     * - 每30秒限制50个请求
     * - 每分钟限制100个请求
     * - 每三分钟限制200个请求
     *
     * @param maxTokens         桶内的最大令牌数量
     * @param currentTokens     桶内的当前令牌数量
     * @param generateTokenRate 创建令牌速率
     * @param limitRate         限制速率，如：每秒、每5秒、每分钟等
     */
    public static synchronized TokenBucketRateLimiter create(long maxTokens, long currentTokens, double generateTokenRate, Duration limitRate) {
        TokenBucketRateLimiter rateLimiter = new TokenBucketRateLimiter();
        rateLimiter.maxTokens = maxTokens;
        rateLimiter.currentTokens = currentTokens;
        rateLimiter.generateTokenRate = generateTokenRate;
        rateLimiter.limitRate = limitRate;
        rateLimiter.lastGenerateTime = System.currentTimeMillis();
        return rateLimiter;
    }

    /**
     * 是否限流
     * - true限流
     * - false不限流
     */
    public synchronized boolean limit() {
        // 调用生成令牌方法
        this.generateTokens();

        // 判断桶内是否还有令牌
        if (currentTokens > 0) {
            currentTokens--;
            return false;
        }

        return true;
    }

    /**
     * 生成令牌
     * - 计算并更新这段时间内生成的令牌数量
     */
    private void generateTokens() {
        // 计算从上次填充令牌到现在过去了多少时间
        long now = System.currentTimeMillis();
        long timeDelta = now - lastGenerateTime;

        // 根据时间差计算应该新增的令牌数
        long newTokens = (long) (timeDelta * (generateTokenRate / limitRate.toMillis()));

        System.out.println("newTokens: " + newTokens);

        // 更新桶中的令牌数，但不能超过桶的容量
        currentTokens = Math.min(currentTokens + newTokens, maxTokens);

        // 更新上次令牌填充的时间
        lastGenerateTime = now;
    }

}
