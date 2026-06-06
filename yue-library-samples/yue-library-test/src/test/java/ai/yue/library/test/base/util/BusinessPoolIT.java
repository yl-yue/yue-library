package ai.yue.library.test.base.util;

import ai.yue.library.base.util.AsyncUtils;
import ai.yue.library.base.util.ThreadPoolRegistry;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 业务线程池集成测试
 * <p>
 * 验证 BusinessPoolAutoConfiguration 自动配置、ThreadPoolRegistry 注册、
 * AsyncUtils 业务线程池重载方法的端到端行为。
 * <p>
 * 测试配置见 application-test-confidential.yml 中的 yue.thread-pool.business-pools
 *
 * @author ylyue
 * @since 2026年6月6日
 */
@Slf4j
@SpringBootTest
public class BusinessPoolIT {

    @Autowired
    @Qualifier("test-pool")
    private ThreadPoolTaskExecutor testPoolExecutor;

    // ================= 自动配置验证 =================

    /**
     * 验证业务线程池已通过自动配置注册为 Spring Bean
     */
    @Test
    public void testBusinessPoolBeanRegistered() {
        assertNotNull(testPoolExecutor, "业务线程池 test-pool 应该已注册为 Spring Bean");
        log.info("业务线程池 Bean 验证通过: corePoolSize={}, maxPoolSize={}",
                testPoolExecutor.getCorePoolSize(), testPoolExecutor.getMaxPoolSize());
    }

    /**
     * 验证业务线程池配置参数正确
     */
    @Test
    public void testBusinessPoolConfig() {
        // 对应 test-confidential 配置: core-pool-size: 5, max-pool-size: 10, queue-capacity: 100
        assertEquals(5, testPoolExecutor.getCorePoolSize(), "核心线程数应为 5");
        assertEquals(10, testPoolExecutor.getMaxPoolSize(), "最大线程数应为 10");
        log.info("业务线程池配置验证通过");
    }

    /**
     * 验证 ThreadPoolRegistry 中已注册业务线程池
     */
    @Test
    public void testThreadPoolRegistry() {
        assertTrue(ThreadPoolRegistry.contains("test-pool"), "Registry 应包含 test-pool");
        ThreadPoolTaskExecutor executor = ThreadPoolRegistry.getExecutor("test-pool");
        assertNotNull(executor, "通过 Registry 获取的线程池不应为 null");
        assertTrue(ThreadPoolRegistry.getPoolNames().contains("test-pool"), "poolNames 应包含 test-pool");
        log.info("ThreadPoolRegistry 验证通过, 已注册线程池: {}", ThreadPoolRegistry.getPoolNames());
    }

    /**
     * 验证未注册的线程池名称抛出异常
     */
    @Test
    public void testThreadPoolRegistryNotFound() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ThreadPoolRegistry.getExecutor("not-exist-pool"));
        assertTrue(exception.getMessage().contains("not-exist-pool"), "异常信息应包含线程池名称");
        log.info("未注册线程池异常验证通过: {}", exception.getMessage());
    }

    // ================= AsyncUtils 业务线程池方法验证 =================

    /**
     * 验证 AsyncUtils.execAsync(poolName, Runnable) 指定业务线程池执行
     */
    @Test
    @SneakyThrows
    public void testExecAsyncWithPoolName() {
        CopyOnWriteArrayList<String> threadNames = new CopyOnWriteArrayList<>();

        CompletableFuture<Void> future = AsyncUtils.execAsync("test-pool", () -> {
            threadNames.add(Thread.currentThread().getName());
            log.info("业务线程池执行任务，线程名: {}", Thread.currentThread().getName());
        });

        future.join();
        assertEquals(1, threadNames.size(), "应有一个任务执行");
        assertTrue(threadNames.get(0).startsWith("test-pool-exec-"),
                "线程名应以 test-pool-exec- 开头，实际: " + threadNames.get(0));
        log.info("AsyncUtils.execAsync(poolName, Runnable) 验证通过");
    }

    /**
     * 验证 AsyncUtils.execAsync(poolName, Supplier) 指定业务线程池执行并获取返回值
     */
    @Test
    @SneakyThrows
    public void testExecAsyncWithPoolNameAndReturn() {
        CompletableFuture<String> future = AsyncUtils.execAsync("test-pool", () -> {
            String threadName = Thread.currentThread().getName();
            log.info("业务线程池执行带返回值任务，线程名: {}", threadName);
            return threadName;
        });

        String result = future.join();
        assertTrue(result.startsWith("test-pool-exec-"),
                "返回的线程名应以 test-pool-exec- 开头，实际: " + result);
        log.info("AsyncUtils.execAsync(poolName, Supplier) 验证通过，返回值: {}", result);
    }

    /**
     * 验证 AsyncUtils.exec(poolName, Runnable) 无返回值执行
     */
    @Test
    @SneakyThrows
    public void testExecWithPoolName() {
        AtomicInteger counter = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(1);

        AsyncUtils.exec("test-pool", () -> {
            counter.incrementAndGet();
            latch.countDown();
        });

        latch.await();
        assertEquals(1, counter.get(), "任务应已执行");
        log.info("AsyncUtils.exec(poolName, Runnable) 验证通过");
    }

    /**
     * 验证多个任务并发提交到业务线程池
     */
    @Test
    @SneakyThrows
    public void testMultipleTasksOnBusinessPool() {
        int taskCount = 20;
        CopyOnWriteArrayList<String> threadNames = new CopyOnWriteArrayList<>();
        CountDownLatch latch = new CountDownLatch(taskCount);

        for (int i = 0; i < taskCount; i++) {
            AsyncUtils.exec("test-pool", () -> {
                threadNames.add(Thread.currentThread().getName());
                latch.countDown();
            });
        }

        latch.await();
        assertEquals(taskCount, threadNames.size(), "所有任务应已执行");
        // 验证所有任务都在 test-pool 线程池中执行
        for (String name : threadNames) {
            assertTrue(name.startsWith("test-pool-exec-"),
                    "所有线程名应以 test-pool-exec- 开头，实际: " + name);
        }
        log.info("多任务并发验证通过，任务数: {}, 线程池隔离正确", taskCount);
    }

    // ================= 全局线程池与业务线程池隔离验证 =================

    /**
     * 验证全局线程池和业务线程池是不同的线程池实例
     */
    @Test
    public void testPoolIsolation() {
        ThreadPoolTaskExecutor globalExecutor = AsyncUtils.getExecutor();
        ThreadPoolTaskExecutor businessExecutor = ThreadPoolRegistry.getExecutor("test-pool");

        // 不同实例
        assertTrue(globalExecutor != businessExecutor, "全局线程池和业务线程池应为不同实例");
        // 不同线程名前缀
        assertTrue(globalExecutor.getThreadNamePrefix().startsWith("utils-exec-"),
                "全局线程池前缀应以 utils-exec- 开头");
        assertTrue(businessExecutor.getThreadNamePrefix().startsWith("test-pool-exec-"),
                "业务线程池前缀应以 test-pool-exec- 开头");
        log.info("线程池隔离验证通过");
    }

}
