package ai.yue.library.base.util;

import ai.yue.library.base.config.thread.pool.AsyncConfig;
import ai.yue.library.base.config.thread.pool.AsyncProperties;
import cn.hutool.v7.core.collection.CollUtil;
import cn.hutool.v7.core.lang.Singleton;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.core.thread.ThreadUtil;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
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
    private static TaskDecorator taskDecorator;

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
    
    // ================= 以下为带上下文状态传递的安全并发隔离池方法 =================
    
    private static ForkJoinPool getForkJoinPool(@Nullable String businessId, Integer parallelism) {
        if (parallelism == null || parallelism < 5 || parallelism > 20) {
            throw new IllegalArgumentException("parallelism 参数必须在 5 到 20 之间");
        }
        
        if (StrUtil.isBlank(businessId)) {
            businessId = "global";
        }
        
        // 单例 Key "前缀-业务名-并发度"
        String poolName = "parallelLimit-" + businessId + "-" + parallelism;
        ForkJoinPool forkJoinPool = Singleton.get(poolName, () -> new ForkJoinPool(parallelism));
        return forkJoinPool;
    }
    
    /**
     * 【数据并发专用】并行处理集合（完美替代 parallelStream，自带上下文不丢失）
     * <p>自带上下文自动传递机制（如 MDC 日志追踪、HttpServletRequest 等）。{@linkplain #taskDecorator}</p>
     * <p><b>特性：</b>方法本身是阻塞的。会将集合中的数据分散到指定线程池中火力全开去处理，直到所有元素处理完毕后才放行主线程。</p>
     * <p><b>场景：</b>适用于大批量数据的快速循环处理，例如：并发组装 100 个商品的详情信息、并发消费一批 MQ 的消息数据等。</p>
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
     * @param businessId  业务标识，用于区分不同业务的并发限制
     * @param parallelism 并发限制数（即最大子线程数。合法区间：5~20）
     * @param source      要处理的数据集合
     * @param action      对单条元素的业务处理逻辑
     */
    public static <T> void execParallelLimit(@Nullable String businessId, Integer parallelism, Collection<T> source, Consumer<T> action) {
        if (CollUtil.isEmpty(source)) {
            return;
        }
        ForkJoinPool forkJoinPool = getForkJoinPool(businessId, parallelism);
        
        // 1. 在当前拥有上下文的主线程中，串行且安全地打包装配任务
        CompletableFuture<?>[] futures = source.stream().map(item -> {
            Runnable runnable = () -> action.accept(item);
            // 给子任务穿上上下文的“外衣”
            Runnable decorated = taskDecorator != null ? taskDecorator.decorate(runnable) : runnable;
            // 扔进线程池开启激进并发
            return CompletableFuture.runAsync(decorated, forkJoinPool);
        }).toArray(CompletableFuture[]::new);
        
        // 2. 阻塞主线程，等待这批数据全部消化完毕
        try {
            CompletableFuture.allOf(futures).join();
        } catch (CompletionException e) {
            // 剥开 CompletableFuture 的包装，将真实的业务异常抛出给 GlobalExceptionHandler 处理
            throw new RuntimeException("执行集合并发任务时发生异常", e.getCause() != null ? e.getCause() : e);
        }
    }
    
    public static <T> void execParallelLimit(Integer parallelism, Collection<T> source, Consumer<T> action) {
        execParallelLimit(null, parallelism, source, action);
    }
    
    /**
     * 【独立无返回任务】向受限并发池提交单个异步任务（带上下文传递）
     * <p>不会由于排队或任务积压而拖垮全局默认的 ThreadPoolTaskExecutor。</p>
     * <p><b>特性：</b>非阻塞方法，立即返回 {@link CompletableFuture}。允许多次调用本方法提交多个动作，然后利用外部进行 {@code .join()} 达到并发效果。</p>
     * <p><b>场景：</b>适用于发通知、写审计日志等隔离耗时操作。</p>
     *
     * @param businessId  业务标识，用于区分不同业务的并发限制
     * @param parallelism 并发限制数（合法区间：5~20）
     * @param runnable    独立执行的无返回值逻辑
     * @return {@link CompletableFuture<Void>}，可手动管理等待 join() 或直接忽略
     */
    public static CompletableFuture<Void> execParallelLimit(@Nullable String businessId, Integer parallelism, Runnable runnable) {
        ForkJoinPool forkJoinPool = getForkJoinPool(businessId, parallelism);
        Runnable actualTask = (taskDecorator != null) ? taskDecorator.decorate(runnable) : runnable;
        return CompletableFuture.runAsync(actualTask, forkJoinPool);
    }
    
    public static CompletableFuture<Void> execParallelLimit(Integer parallelism, Runnable runnable) {
        return execParallelLimit(null, parallelism, runnable);
    }
    
    /**
     * 【独立带返回任务】向受限并发池提交单个含参运算任务（带上下文传递）
     * <p><b>特性：</b>非阻塞方法。提交互不干扰的不同接口调用任务后，再在末尾调用得到的 {@code Future.join()} 获取结果，此举能将串行总耗时大幅度降为其中最长链路的单次耗时。</p>
     * <p><b>场景：</b>并发拼装跨微服务的复杂页面数据。如：并发查基础资料（300ms）、资产信息（400ms）、优惠券列表（200ms），总耗时仅从 900ms 降为 400ms。</p>
     *
     * @param businessId  业务标识，用于区分不同业务的并发限制
     * @param parallelism 并发限制数（合法区间：5~20）
     * @param task        需要执行并最终返回结果的逻辑（如 Feign 请求或复杂的耗时计算）
     * @return {@link CompletableFuture<T>}，用于在外层阻塞等待子任务的确切返回值，你可以通过 join() 或 get() 获取结果。
     */
    public static <T> CompletableFuture<T> execParallelLimit(@Nullable String businessId, Integer parallelism, Callable<T> task) {
        ForkJoinPool forkJoinPool = getForkJoinPool(businessId, parallelism);
        
        // 如果项目中未注册上下文装饰器，则直接退化为原生调用
        if (taskDecorator == null) {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    return task.call();
                } catch (Exception e) {
                    throw new CompletionException(e);
                }
            }, forkJoinPool);
        }
        
        // 运行在主线程，利用原子引用穿透闭包，捕获业务计算结果和异常
        AtomicReference<T> resultRef = new AtomicReference<>();
        AtomicReference<Exception> errorRef = new AtomicReference<>();
        
        // 主线程直接调用 decorate，完美捕获主线程的 Request、SaToken、MDC
        Runnable decorated = taskDecorator.decorate(() -> {
            try {
                resultRef.set(task.call());
            } catch (Exception e) {
                errorRef.set(e);
            }
        });
        
        // 丢入并发池执行已经包装好的任务
        return CompletableFuture.supplyAsync(() -> {
            // 在子线程中执行装甲包裹后的逻辑
            decorated.run();
            
            // 原路抛出异常，让外层 join() 能够接到
            if (errorRef.get() != null) {
                throw new CompletionException(errorRef.get());
            }
            return resultRef.get();
        }, forkJoinPool);
    }
    
    public static <T> CompletableFuture<T> execParallelLimit(Integer parallelism, Callable<T> task) {
        return execParallelLimit(null, parallelism, task);
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
