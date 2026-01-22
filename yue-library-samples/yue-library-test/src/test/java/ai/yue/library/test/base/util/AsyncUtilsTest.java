package ai.yue.library.test.base.util;

import ai.yue.library.base.util.AsyncUtils;
import ai.yue.library.base.util.ListUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.v7.core.text.StrUtil;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinTask;

@Slf4j
public class AsyncUtilsTest {

    @SneakyThrows
    @Test
    public void test() {
        CompletableFuture<Void> future1 = AsyncUtils.execAsync(() -> {
            AsyncUtils.sleep(500);
            System.out.println("异步线程执行3，线程名：" + Thread.currentThread().getName());
        });

        CompletableFuture<Void> future2 = AsyncUtils.execAsync(() -> {
            AsyncUtils.sleep(300);
            System.out.println("异步线程执行1，线程名：" + Thread.currentThread().getName());
        });

        CompletableFuture<Void> future3 = AsyncUtils.execAsync(() -> {
            AsyncUtils.sleep(400);
            System.out.println("异步线程执行2，线程名：" + Thread.currentThread().getName());
        });

        // 等待任意一个任务完成
        CompletableFuture.anyOf(future1, future2, future3).join();

        // 等待所有任务完成
        CompletableFuture.allOf(future1, future2, future3).join();
        System.out.println("执行完毕");
    }

    @SneakyThrows
    @Test
    public void testCompletableFuture() {
        CompletableFuture<Integer> future1 = AsyncUtils.execAsync(() -> {
            AsyncUtils.sleep(500);
            System.out.println("异步线程执行3，线程名：" + Thread.currentThread().getName());
            return 3;
        });

        CompletableFuture<Integer> future2 = AsyncUtils.execAsync(() -> {
            System.out.println("异步线程执行1，线程名：" + Thread.currentThread().getName());
            return 1;
        });

        CompletableFuture<Integer> future3 = AsyncUtils.execAsync(() -> {
            System.out.println("异步线程执行2，线程名：" + Thread.currentThread().getName());
            return 2;
        });

        List<Integer> integerList = AsyncUtils.futuresSuccessfulAsList(future1, future2, future3);
        integerList.forEach(System.out::println);
    }

    @SneakyThrows
    @Test
    public void testParallelStream() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(i + "");
        }
        List<List<String>> split = ListUtils.partition(list, 10);
        Set<String> set = new HashSet<>();
        Set<String> set2 = new HashSet<>();

        AsyncUtils.execParallelLimit(5, () -> {
            split.parallelStream().forEach(item -> {
                log.info("{}", item);
                String name = Thread.currentThread().getName();
                String s = StrUtil.subAfter(name, "-", true);
                set.add(s);
                AsyncUtils.sleep(20);
            });
        });

        AsyncUtils.execParallelLimit(8, () -> {
            split.parallelStream().forEach(item -> {
                log.info("{}", item);
                String name = Thread.currentThread().getName();
                String s = StrUtil.subAfter(name, "-", true);
                set2.add(s);
                AsyncUtils.sleep(20);
            });
        });

        ForkJoinTask<String> forkJoinTask = AsyncUtils.execParallelLimit(5, () -> {
            split.parallelStream().forEach(item -> {
                log.info("{}", item);
                String name = Thread.currentThread().getName();
                String s = StrUtil.subAfter(name, "-", true);
                set.add(s);
                AsyncUtils.sleep(20);
            });

            return "aaaa";
        });

        String join = forkJoinTask.join();
        System.out.println(join);
        System.out.println(set.size());
        System.out.println(set2.size());
    }

    @Test
    @SneakyThrows
    public void testParallelStreamAdd() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(i + "");
        }
        List<List<String>> split = ListUtils.partition(list, 10);
        Set<String> setResult = new HashSet<>();
        List<String> listResult = new ArrayList<>();
        List<String> synchronizedListResult = Collections.synchronizedList(new ArrayList<>());
        List<String> copyOnWriteArrayListResult = new CopyOnWriteArrayList<>();

        AsyncUtils.execParallelLimit(5, () -> {
            split.parallelStream().forEach(item -> {
                log.info("{}", item);
                String name = Thread.currentThread().getName();
                String s = StrUtil.subAfter(name, "-", true);
                setResult.add(s);
                listResult.add(s);
                synchronizedListResult.add(s);
                copyOnWriteArrayListResult.add(s);
                AsyncUtils.sleep(20);
            });
        });

        System.out.println(setResult.size());  // 线程不安全
        System.out.println(listResult.size());  // 线程不安全
        System.out.println(synchronizedListResult.size());  // 线程安全
        System.out.println(copyOnWriteArrayListResult.size());  // 线程安全
    }

    @SneakyThrows
    @Test
    public void testNoParallelStream() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(i + "");
        }
        List<List<String>> split = ListUtils.partition(list, 10);
        Set<String> set = new HashSet<>();
        Set<String> set2 = new HashSet<>();

        AsyncUtils.execParallelLimit(5, () -> {
            split.forEach(item -> {
                log.info("{}", item);
                String name = Thread.currentThread().getName();
                String s = StrUtil.subAfter(name, "-", true);
                set.add(s);
                AsyncUtils.sleep(20);
            });
        });

        AsyncUtils.execParallelLimit(8, () -> {
            split.forEach(item -> {
                log.info("{}", item);
                String name = Thread.currentThread().getName();
                String s = StrUtil.subAfter(name, "-", true);
                set2.add(s);
                AsyncUtils.sleep(20);
            });
        });

        ForkJoinTask<String> forkJoinTask = AsyncUtils.execParallelLimit(5, () -> {
            split.forEach(item -> {
                log.info("{}", item);
                String name = Thread.currentThread().getName();
                String s = StrUtil.subAfter(name, "-", true);
                set.add(s);
                AsyncUtils.sleep(20);
            });

            return "aaaa";
        });

        String join = forkJoinTask.join();
        System.out.println(join);
        System.out.println(set.size());
        System.out.println(set2.size());
    }

    @SneakyThrows
    @Test
    public void testParallelStreamParallel() {
        for (int i = 0; i < 200; i++) {
            AsyncUtils.execAsync(() -> testParallelStream());
        }

//        AsyncUtils.getExecutor().shutdown();
    }

    @SneakyThrows
    @Test
    public void testParallelStreamException() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(i + "");
        }
        List<List<String>> split = ListUtils.partition(list, 10);
        Set<String> set = new HashSet<>();

        try {
            AsyncUtils.execParallelLimit(5, () -> {
                split.parallelStream().forEach(item -> {
                    log.info("{}", item);
                    String name = Thread.currentThread().getName();
                    String s = StrUtil.subAfter(name, "-", true);
                    set.add(s);
                    throw new NullPointerException("1111");
                });
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println(set.size());
        }
    }

}
