package ai.yue.library.base.util;

import ai.yue.library.base.config.thread.pool.AsyncConfig;
import ai.yue.library.base.config.thread.pool.AsyncProperties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import cn.hutool.v7.core.lang.Singleton;
import cn.hutool.v7.core.thread.ThreadUtil;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 异步执行工具类
 * - 线程安全、线程池调优
 * - 有界线程池、避免线程溢出
 * 
 * @author	ylyue
 * @since	2019年10月1日
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AsyncUtils {

    private static ThreadPoolTaskExecutor executor;

    static {
        init();
    }

    /**
     * 初始化异步线程池
     */
    private static synchronized void init() {
        if (null != executor) {
            executor.shutdown();
        }

        AsyncProperties asyncProperties;
        TaskDecorator taskDecorator = null;
        if (SpringUtils.getApplicationContext() == null) {
            asyncProperties = new AsyncProperties();
        } else {
            asyncProperties = ObjUtils.clone(SpringUtils.getBean(AsyncProperties.class));
            taskDecorator = SpringUtils.getBean(TaskDecorator.class);
        }

        asyncProperties.setThreadNamePrefix("utils-exec-");
        AsyncConfig asyncConfig = new AsyncConfig(asyncProperties, taskDecorator);
        executor = asyncConfig.getThreadPoolTaskExecutor();
    }

    /**
     * 获得 {@link ThreadPoolTaskExecutor}
     */
    public static ThreadPoolTaskExecutor getExecutor() {
        return executor;
    }

    /**
     * 异步执行代码块
     *
     * @param task 要执行的代码块
     */
    public static void exec(Runnable task) {
        executor.execute(task);
    }

    /**
     * 异步执行代码块-获取CompletableFuture<Void><br>
     * CompletableFuture代表一个异步执行的操作，通过get()方法可以获得操作的结果，如果异步操作还没有完成，则，get()会使当前线程阻塞
     *
     * @param task 要执行的代码块
     * @return {@link CompletableFuture<Void>}
     */
    public static CompletableFuture<Void> execAsync(Runnable task) {
        return CompletableFuture.runAsync(task, executor);
    }

    /**
     * 异步执行代码块-获取有返回值的CompletableFuture<T><br>
     * CompletableFuture代表一个异步执行的操作，通过get()方法可以获得操作的结果，如果异步操作还没有完成，则，get()会使当前线程阻塞
     *
     * @param task 有返回值的代码块
     * @return {@link CompletableFuture<T>}
     */
    public static <T> CompletableFuture<T> execAsync(Supplier<T> task) {
        return CompletableFuture.supplyAsync(task, executor);
    }

    /**
     * 获取多个CompletableFuture执行结果，如果某个CompletableFuture失败，则值为null
     *
     * @param futures 多个CompletableFuture
     * @return 结果列表顺序和入参列表同步
     */
    public static <T> List<T> futuresSuccessfulAsList(CompletableFuture<T>... futures) {
        /*
         * CompletableFuture 比 com.google.common.util.concurrent.Futures 提供更现代的异步计算操作
         * 本方法使用java8 CompletableFuture 实现 Futures.successfulAsList() 一样的效果
         */
        CompletableFuture<List<T>> allFutures = CompletableFuture.allOf(futures)
                .thenApply(v -> Arrays.stream(futures)
                        .map(CompletableFuture::join) // join() 不会抛出 checked 异常
                        .collect(Collectors.toList()));
        return allFutures.join();
    }

    /**
     * 执行并行限制任务
     * <p>parallelStream()使用共享的线程池，如果任务数大于cpu数，则使用cpu数的线程，否则使用任务数的线程。</p>
     * <p>有时我们想将并发数限制在既定的范围内，如：5、10、20，execParallelLimit()方法可以很好的实现，并做好单例线程池管理</p>
     *
     * <pre><code>
     * 线程安全的 List
     *      读多写少，使用 CopyOnWriteArrayList，使用示例：List&lt;String&gt; syncList = Collections.synchronizedList(new ArrayList<>());
     *      读写都多，使用 Collections.synchronizedList，使用示例：List&lt;String&gt; cowList = new CopyOnWriteArrayList<>();
     *
     * 线程安全的 Map
     *      高并发读写，使用 ConcurrentHashMap，使用示例：Map&lt;String, String&gt; concurrentMap = new ConcurrentHashMap<>();
     *      需要有序且并发，使用 ConcurrentSkipListMap，使用示例：ConcurrentNavigableMap&lt;String, String&gt; skipListMap = new ConcurrentSkipListMap<>();
     * </code><pre>
     *
     * @param parallelism 并行限制数（最小值：5，最大值：20）
     * @param runnable    执行任务
     */
    public static void execParallelLimit(Integer parallelism, Runnable runnable) {
        if (parallelism == null || parallelism < 5 || parallelism > 20) {
            throw new IllegalArgumentException("parallelism must be between 5 and 20");
        }

        ForkJoinPool forkJoinPool = Singleton.get("parallelism" + parallelism, () -> new ForkJoinPool(parallelism));
        forkJoinPool.submit(runnable).join();
    }

    /**
     * 执行并行限制任务
     * <p>parallelStream()使用共享的线程池，如果任务数大于cpu数，则使用cpu数的线程，否则使用任务数的线程。</p>
     * <p>有时我们想将并发数限制在既定的范围内，如：5、10、20，execParallelLimit()方法可以很好的实现，并做好单例线程池管理</p>
     *
     * @param parallelism 并行限制数（最小值：5，最大值：20）
     * @param task        执行任务
     */
    public static <T> ForkJoinTask<T> execParallelLimit(Integer parallelism, Callable<T> task) {
        if (parallelism == null || parallelism < 5 || parallelism > 20) {
            throw new IllegalArgumentException("parallelism must be between 5 and 20");
        }

        ForkJoinPool forkJoinPool = Singleton.get("parallelism" + parallelism, () -> new ForkJoinPool(parallelism));
        return forkJoinPool.submit(task);
    }

    /**
     * 挂起当前线程
     *
     * @param millis 挂起的毫秒数
     * @return 被中断返回false，否则true
     */
    public static boolean sleep(long millis) {
        return ThreadUtil.sleep(millis);
    }

    /**
     * 考虑{@link Thread#sleep(long)}方法有可能时间不足给定毫秒数，此方法保证sleep时间不小于给定的毫秒数
     *
     * @param millis 给定的sleep时间
     * @return 被中断返回false，否则true
     * @see ThreadUtil#sleep(Number)
     */
    public static boolean safeSleep(long millis) {
        return ThreadUtil.safeSleep(millis);
    }

}
