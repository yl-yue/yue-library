package ai.yue.library.data.redis.custom;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * 忽略抢锁失败，常用于：
 * - 并发限制忽略
 * - 阻断后续执行
 */
@Slf4j
public class IgnoreLockFailureStrategy implements LockFailureStrategy {

    @Override
    public void onLockFailure(String key, Method method, Object[] arguments) {
        log.warn("锁获取失败，锁已被其他线程持有，跳过本次任务执行：key={}, method={}, arguments={}", key, method, arguments);
    }

}
