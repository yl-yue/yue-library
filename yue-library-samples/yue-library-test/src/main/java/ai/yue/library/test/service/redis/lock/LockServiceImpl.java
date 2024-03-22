package ai.yue.library.test.service.redis.lock;

import ai.yue.library.data.redis.annotation.Lock;
import ai.yue.library.data.redis.client.LockClient;
import ai.yue.library.test.ipo.LockIPO;
import ai.yue.library.test.service.redis.lock.custom.CustomLockFailureStrategy;
import ai.yue.library.test.service.redis.lock.custom.CustomLockKeyBuilder;
import cn.hutool.core.thread.ThreadUtil;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 分布式锁
 */
@Service
public class LockServiceImpl implements LockService {

    @Autowired
    LockClient lockClient;
    private int counter = 1;

    @Override
    @Lock
    public void simple1() {
        System.out.println("执行简单方法1, 当前线程: " + Thread.currentThread().getName() + ", counter：" + (counter++));
    }

    @Override
    @Lock(keys = "#myKey")
    public void simple2(String myKey) {
        System.out.println("执行简单方法2, 当前线程: " + Thread.currentThread().getName() + ", counter：" + (counter++));
    }

    @Override
    @Lock(keys = "#lockIPO.id", acquireTimeout = 15000, expire = 1000)
    public LockIPO spel1(LockIPO lockIPO) {
        System.out.println("执行spel方法1, 当前线程: " + Thread.currentThread().getName() + ", counter：" + (counter++));
        // 模拟锁占用
        ThreadUtil.sleep(1000);
        return lockIPO;
    }

    @Override
    @Lock(keys = {"#lockIPO.id", "#lockIPO.name"}, acquireTimeout = 5000, expire = 5000)
    public LockIPO spel2(LockIPO lockIPO) {
        System.out.println("执行spel方法2, 当前线程: " + Thread.currentThread().getName() + ", counter：" + (counter++));
        // 模拟锁占用
        ThreadUtil.sleep(4000);
        return lockIPO;
    }

    /**
     * 使用上面@Autowired注入的Bean值
     */
    @Override
    @Lock(keys = {"@lockBeanIPO.id", "@lockBeanIPO.name"}, acquireTimeout = 5000, expire = 5000)
    public void spel3() {
        System.out.println("执行spel方法3, 当前线程: " + Thread.currentThread().getName() + ", counter：" + (counter++));
        // 模拟锁占用
        ThreadUtil.sleep(4000);
    }

    /**
     * 编程式锁
     */
    @Override
    public void programmaticLock(String userId) {
        // 各种查询操作 不上锁
        // ...
        // 获取锁
        final RLock lockInstance = lockClient.lock(userId, 30000L, 5000L);
        if (lockInstance == null) {
            throw new RuntimeException("业务处理中,请稍后再试");
        }

        // 获取锁成功，处理业务
        try {
            System.out.println("执行programmaticLock, 当前线程: " + Thread.currentThread().getName() + ", counter: " + (counter++));
        } finally {
            // 释放锁
            lockClient.unlock(lockInstance);
        }
    }

    @Override
    @Lock(keys = "1", autoUnlock = false, expire = 60000)
    public void reentrantMethod1(int counterReentrant) {
        System.out.println("执行reentrantMethod方法1: " + getClass());
        counterReentrant++;
        if (counterReentrant >= 10) {
            return;
        }
        ThreadUtil.sleep(1000);
        // 内部方法递归调用，不触发AOP拦截，及不触发重入锁特性
//        reentrantMethod2(counterReentrant);
    }

    @Override
    @Lock(keys = "1", autoUnlock = false)
    public void reentrantMethod2(int counterReentrant) {
        System.out.println("执行reentrantMethod方法2: " + getClass());
        counterReentrant++;
        if (counterReentrant >= 10) {
            return;
        }
        ThreadUtil.sleep(1000);
//        reentrantMethod1(counterReentrant);
    }

    @Override
    @Lock(acquireTimeout = 0, expire = 15000, autoUnlock = false)
    public void nonAutoReleaseLock() {
        System.out.println("执行nonAutoReleaseLock方法, 当前线程: " + Thread.currentThread().getName() + ", counter: " + (counter++));
    }

    @Override
    @Lock(keys = "1", expire = -1)
    public void usedInInterface() {
        System.out.println("执行usedInInterface方法, 当前线程: " + Thread.currentThread().getName() + ", counter: " + (counter++));
    }

    /**
     * 暂不支持直接使用自定义策略类，需要将类注册为Bean，后续考虑反射new实例实现
     */
    @Override
    @Lock(keys = "1", expire = -1, failStrategy = CustomLockFailureStrategy.class, keyBuilderStrategy = CustomLockKeyBuilder.class)
    public void customLockStrategy() {
        System.out.println("执行customLockStrategy方法, 当前线程: " + Thread.currentThread().getName() + ", counter: " + (counter++));
        ThreadUtil.sleep(3000);
    }

}
