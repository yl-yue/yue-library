package ai.yue.library.test.service.redis.lock;

import ai.yue.library.data.redis.annotation.Lock;
import ai.yue.library.test.ipo.LockIPO;

/**
 * 分布式锁
 */
public interface LockService {

    void simple1();

    void simple2(String myKey);

    LockIPO spel1(LockIPO lockIPO);

    LockIPO spel2(LockIPO lockIPO);

    void spel3();

    void programmaticLock(String userId);

    void reentrantMethod1(int counterReentrant);

    void reentrantMethod2(int counterReentrant);

    void nonAutoReleaseLock();

    @Lock(keys = "1", expire = -1)
    void usedInInterface();

    void customLockStrategy();

}
