package ai.yue.library.data.redis.custom;

import java.lang.reflect.Method;

/**
 * 锁失败事件监听
 */
public interface LockFailureStrategy {

    /**
     * 锁失败事件
     */
    void onLockFailure(String key, Method method, Object[] arguments);

}
