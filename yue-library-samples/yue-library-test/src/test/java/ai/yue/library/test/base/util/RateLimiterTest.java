package ai.yue.library.test.base.util;

import ai.yue.library.base.util.AsyncUtils;
import ai.yue.library.base.util.rate.limiter.RateLimiterUtils;
import ai.yue.library.base.util.rate.limiter.TokenBucketRateLimiter;
import cn.hutool.v7.core.util.RandomUtil;
import com.google.common.util.concurrent.RateLimiter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.time.Duration;

public class RateLimiterTest {

    @SneakyThrows
    @Test
    public void testRateLimiter() {
        RateLimiter rateLimiter = RateLimiter.create(40);
        // 延迟500ms，等待RateLimiter初始化
        AsyncUtils.sleep(500);
        for (int i = 0; i < 50; i++) {
            AsyncUtils.execAsync(() -> {
                boolean b = rateLimiter.tryAcquire();
                if (b) {
                    System.out.println("获取成功：" + Thread.currentThread().getName());
                } else {
                    System.out.println("获取失败：" + Thread.currentThread().getName());
                }
            });
        }
        AsyncUtils.sleep(2000);
    }

    @SneakyThrows
    @Test
    public void testTokenBucketRateLimiter() {
        TokenBucketRateLimiter rateLimiter = TokenBucketRateLimiter.create(30, Duration.ofMillis(2000));
        for (int i = 0; i < 50; i++) {
            AsyncUtils.execAsync(() -> {
                boolean b = rateLimiter.limit();
                if (b) {
                    System.out.println("请求限流：" + Thread.currentThread().getName());
                } else {
                    System.out.println("正常请求：" + Thread.currentThread().getName());
                }
            });
            if (RandomUtil.randomBoolean()) {
                AsyncUtils.sleep(100);
            }
        }
        AsyncUtils.sleep(2000);
    }

    @SneakyThrows
    @Test
    public void testTokenBucketRateLimiterUtils() {
        TokenBucketRateLimiter rateLimiter = RateLimiterUtils.getCacheTokenBucketRateLimiter("test", 25, Duration.ofMillis(10000));
        for (int i = 0; i < 52; i++) {
            AsyncUtils.execAsync(() -> {
                boolean b = rateLimiter.limit();
                if (b) {
                    System.out.println("请求限流：" + Thread.currentThread().getName());
                } else {
                    System.out.println("正常请求：" + Thread.currentThread().getName());
                }
            });
            if (RandomUtil.randomBoolean()) {
                AsyncUtils.sleep(500);
            }
        }
        AsyncUtils.sleep(2000);
    }

}
