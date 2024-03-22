package ai.yue.library.data.redis.custom;

import org.aopalliance.intercept.MethodInvocation;

/**
 * 锁key生成器
 */
public interface LockKeyBuilder {

    /**
     * 构建key
     */
    String buildKey(MethodInvocation invocation, String[] definitionKeys);

}
