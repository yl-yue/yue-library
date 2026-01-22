package ai.yue.library.test.controller.data.redis;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.redis.annotation.Lock;
import ai.yue.library.data.redis.client.Redis;
import ai.yue.library.data.redis.custom.IgnoreLockFailureStrategy;
import ai.yue.library.data.redis.instance.LockInfo;
import ai.yue.library.data.redis.instance.LockMapInfo;
import ai.yue.library.test.ipo.LockIPO;
import ai.yue.library.test.service.redis.lock.LockService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import cn.hutool.v7.core.thread.ThreadUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 分布式锁测试
 */
@RestController
@RequestMapping("/lock")
@RequiredArgsConstructor
public class LockController {

    final Redis redis;
    final LockService lockService;

    @Lock
//    @Lock(keys = "1")
    @PostMapping("/controller")
    public Result<?> controller() {
        System.out.println("执行逻辑...开始...");
        ThreadUtil.sleep(5000L);
        System.out.println("执行逻辑...结束...");
        return R.success();
    }

    @PostMapping("/testLockInfo")
    public Result<?> testLockInfo() {
        System.out.println("执行逻辑...开始...");
        LockInfo lockInfo = redis.lock("testLockInfo", 5000);
        if (lockInfo.isLock()) {
            ThreadUtil.sleep(2000L);
            System.out.println("执行逻辑...结束...");
            redis.unlock(lockInfo);
            return R.success();
        }

        return R.errorPrompt("未能获取锁");
    }

    @PostMapping("/testLockMapInfo")
    public Result<?> testLockMapInfo() {
        System.out.println("执行逻辑...开始...");
        LockMapInfo lockMapInfo = redis.lockMap("testLockMapInfo", "uuid1", 5000);
        if (lockMapInfo.isLock()) {
            ThreadUtil.sleep(2000L);
            System.out.println("执行逻辑...结束...");
            redis.unlockMap(lockMapInfo);
            return R.success();
        }

        return R.errorPrompt("未能获取锁");
    }

    @Lock
    @PostMapping("/testLockException")
    public Result<?> testLockException() {
        System.out.println("执行逻辑...开始...");
        ThreadUtil.sleep(1000L);
        throw new RuntimeException("测试异常");
//        throw new ResultException("测试异常");
    }

    @PostMapping("/testLockTimeoutException")
    public Result<?> testLockTimeoutException() {
        System.out.println("执行逻辑...开始...");
        LockMapInfo lockMapInfo = redis.lockMap("mapLock", "uuid1", 1000);
//        if (lockMapInfo.isLock()) {
            ThreadUtil.sleep(35000L);
            System.out.println("执行逻辑...结束...");
//        }

        redis.unlockMap(lockMapInfo);
        return R.success();
    }

    @Lock
    @PostMapping("/testDefaultLockFailureStrategy")
    public Result<?> testDefaultLockFailureStrategy() {
        System.out.println("执行逻辑...开始...");
        ThreadUtil.sleep(5000L);
        System.out.println("执行逻辑...结束...");
        return R.success();
    }

    @Lock(acquireTimeout = 0, failStrategy = IgnoreLockFailureStrategy.class)
    @PostMapping("/testIgnoreLockFailureStrategy")
    public Result<?> testIgnoreLockFailureStrategy() {
        System.out.println("执行逻辑...开始...");
        ThreadUtil.sleep(5000L);
        System.out.println("执行逻辑...结束...");
        return R.success();
    }

    @SneakyThrows
    @PostMapping("/testLock")
    public Result<?> testLock(Integer num) {
        switch (num) {
            case 1:
                ThreadUtil.execAsync(() -> testSimple1());
                ThreadUtil.execAsync(() -> testSimple2());
                break;
            case 2:
                ThreadUtil.execAsync(() -> testSpel1());
                ThreadUtil.execAsync(() -> testSpel2());
                ThreadUtil.execAsync(() -> testSpel3());
                break;
            case 3:
                ThreadUtil.execAsync(() -> testProgrammaticLock());
                break;
            case 4:
                ThreadUtil.execAsync(() -> testReentrantLock());
                break;
            case 5:
                ThreadUtil.execAsync(() -> testNonAutoReleaseLock());
                break;
            case 6:
                ThreadUtil.execAsync(() -> testUsedInInterface());
                break;
            case 7:
//                ThreadUtil.execAsync(() -> testCustomLockStrategy());
                break;
            default:
                ThreadUtil.execAsync(() -> testSimple1());
                ThreadUtil.execAsync(() -> testSimple2());

                ThreadUtil.execAsync(() -> testSpel1());
                ThreadUtil.execAsync(() -> testSpel2());
                ThreadUtil.execAsync(() -> testSpel3());

                ThreadUtil.execAsync(() -> testProgrammaticLock());
                ThreadUtil.execAsync(() -> testReentrantLock());
                ThreadUtil.execAsync(() -> testNonAutoReleaseLock());
                ThreadUtil.execAsync(() -> testUsedInInterface());
//                ThreadUtil.execAsync(() -> testCustomLockStrategy());
                System.out.println("执行全部线程...");
        }

        return R.success();
    }

    @SneakyThrows
    public void testSimple1() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> lockService.simple1());
        }
    }

    @SneakyThrows
    public void testSimple2() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> lockService.simple2("xxx_key"));
        }
    }

    @SneakyThrows
    public void testSpel1() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 5; i++) {
            executorService.submit(() -> lockService.spel1(new LockIPO(1L, "yue")));
        }
    }

    @SneakyThrows
    public void testSpel2() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> lockService.spel2(new LockIPO(1L, "yue")));
        }
    }

    @SneakyThrows
    public void testSpel3() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> lockService.spel3());
        }
    }

    /**
     * 编程式锁
     */
    @SneakyThrows
    public void testProgrammaticLock() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> lockService.programmaticLock("admin"));
        }
    }

    /**
     * 重入锁
     */
    public void testReentrantLock() {
        // A类调用B类，锁不自动释放，同一线程调用，触发重入锁特性
        lockService.reentrantMethod1(1);
        lockService.reentrantMethod1(1);
        lockService.reentrantMethod2(1);
        lockService.reentrantMethod2(1);
        lockService.reentrantMethod1(1);
        lockService.reentrantMethod2(1);
        lockService.reentrantMethod2(1);
    }

    /**
     * 不自动解锁
     */
    @SneakyThrows
    public void testNonAutoReleaseLock() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        for (int i = 0; i < 2; i++) {
            executorService.submit(() -> lockService.nonAutoReleaseLock());
        }
    }

    /**
     * Lock注解适用于接口方法
     */
    @SneakyThrows
    public void testUsedInInterface(){
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 5; i++) {
            executorService.submit(() -> lockService.usedInInterface());
        }
    }

    /**
     * 自定义锁策略测试（锁失败处理与锁key生成）
     * - 当存在多个默认策略时，通过order|PriorityOrdered接口控制优先级
     * - 注解上可以指定使用某个策略实现
     */
    @SneakyThrows
    public void testCustomLockStrategy() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 5; i++) {
            executorService.submit(() -> lockService.customLockStrategy());
        }
    }

}
