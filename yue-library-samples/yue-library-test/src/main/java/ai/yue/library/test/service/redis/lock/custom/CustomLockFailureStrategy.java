package ai.yue.library.test.service.redis.lock.custom;

import ai.yue.library.data.redis.custom.LockFailureStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 自定义获取锁异常处理
 */
@Slf4j
//@Component
public class CustomLockFailureStrategy implements LockFailureStrategy, Ordered {

    @Override
    public void onLockFailure(String key, Method method, Object[] arguments) {
        log.error("获取锁失败了, key={}, method={}, arguments={}", key, method, arguments);
        // 此处可以抛出指定异常，配合全局异常拦截包装统一格式返回给调用端
        throw new BusinessException("请求太快啦~");
    }

    @Override
    public int getOrder() {
        return 1;
    }

}
