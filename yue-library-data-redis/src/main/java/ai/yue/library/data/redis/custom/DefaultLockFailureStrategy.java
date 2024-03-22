package ai.yue.library.data.redis.custom;

import ai.yue.library.base.exception.ResultException;
import ai.yue.library.base.view.R;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@Slf4j
public class DefaultLockFailureStrategy implements LockFailureStrategy {

    protected static String DEFAULT_MESSAGE = "request failed, please retry it.";

    @Override
    public void onLockFailure(String key, Method method, Object[] arguments) {
        log.error("获取锁失败了, key={}, method={}, arguments={}", key, method, arguments);
        throw new ResultException(R.lockAcquireFailure(DEFAULT_MESSAGE));
    }

}
