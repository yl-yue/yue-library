package ai.yue.library.base.util;

import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.RejectPolicy;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RuntimeUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

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

    private static ExecutorService executor;

    static {
        init();
    }

    /**
     * 初始化异步线程池
     */
    private static synchronized void init() {
        if (null != executor) {
            executor.shutdownNow();
        }

        int corePoolSize = RuntimeUtil.getProcessorCount();
        int maxPoolSize = corePoolSize * 2;
        int maximumQueueSize = 200;
        executor = ExecutorBuilder.create()
                .setCorePoolSize(corePoolSize)
                .setMaxPoolSize(maxPoolSize)
                .setWorkQueue(new LinkedBlockingQueue<>(maximumQueueSize))
                .setHandler(RejectPolicy.BLOCK.getValue())
                .setThreadFactory(ThreadUtil.createThreadFactory("AsyncUtils-exec-"))
                .build();
    }

    /**
     * 获得 {@link ExecutorService}
     *
     * @return {@link ExecutorService}
     */
    public static ExecutorService getExecutor() {
        return executor;
    }

    /**
     * 执行有返回值的异步方法<br>
     * Future代表一个异步执行的操作，通过get()方法可以获得操作的结果，如果异步操作还没有完成，则，get()会使当前线程阻塞
     *
     * @param runnable 无返回值运行对象
     * @return {@link Future}
     */
    public static Future<?> execAsync(Runnable runnable) {
        return executor.submit(runnable);
    }

    /**
     * 执行有返回值的异步方法<br>
     * Future代表一个异步执行的操作，通过get()方法可以获得操作的结果，如果异步操作还没有完成，则，get()会使当前线程阻塞
     *
     * @param task 有返回值运行对象
     * @return {@link Future}
     */
    public static <T> Future<T> execAsync(Callable<T> task) {
        return executor.submit(task);
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
