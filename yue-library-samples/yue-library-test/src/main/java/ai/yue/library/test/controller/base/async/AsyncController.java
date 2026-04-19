package ai.yue.library.test.controller.base.async;

import ai.yue.library.base.util.AsyncUtils;
import ai.yue.library.base.util.DateUtils;
import ai.yue.library.base.util.ListUtils;
import ai.yue.library.base.util.time.interval.TimeInterval;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.web.util.ServletUtils;
import cn.hutool.v7.core.text.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;

/**
 * 异步测试
 *
 * @author ylyue
 * @since 2020/12/14
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/async")
public class AsyncController {

    final AsyncService asyncService;

    @GetMapping("/async")
    public Result<?> async() {
        String asyncContext = ServletUtils.getRequest().getHeader("asyncContext");
        log.info("1. 异步测试-开始调用异步方法，asyncContext：{}", asyncContext);
//        ServletUtils.getRequest().setAttribute(AsyncProperties.SERVLET_ASYNC_CONTEXT_TIMEOUT_MILLIS,1);
        asyncService.async();
        log.info("2. 异步测试-异步方法正在执行");
        AsyncUtils.sleep(5000);
        asyncService.sync();
        HttpServletRequest request = ServletUtils.getRequest();
        log.info("7. 异步测试-异步方法执行完毕。{}",  ServletUtils.getHeaderMap(request));
        return R.success();
    }

    @SneakyThrows
    @GetMapping("/asyncFuture")
    public Result<?> asyncFuture() {
        String asyncContext = ServletUtils.getRequest().getHeader("asyncContext");
        log.info("1. 异步测试-开始调用异步方法，asyncContext：{}", asyncContext);
        CompletableFuture<Result<?>> resultAsyncResult = asyncService.asyncFuture();
        log.info("2. 异步测试-异步方法正在执行");
        Result<?> result = resultAsyncResult.get();
        log.info("6. 异步测试-异步方法执行完毕。{}",  result);
        return result;
    }

    @GetMapping("/asyncException")
    public Result<?> asyncException() {
        String asyncContext = ServletUtils.getRequest().getHeader("asyncContext");
        log.info("1. 异步测试-开始调用异步方法，asyncContext：{}", asyncContext);
        asyncService.asyncException();
        log.info("2. 异步测试-异步方法正在执行");
        return R.success();
    }

    @GetMapping("/sync")
    public Result<?> sync() {
        log.info("1. 同步-开始调用同步方法");
        return asyncService.sync();
    }

    @SneakyThrows
    @GetMapping("/testAsyncUtils")
    public void testAsyncUtils() {
        MDC.put("aaa", "bbb");
        Future<?> future = AsyncUtils.execAsync(() -> {
            asyncService.sync();
            AsyncUtils.sleep(500);
            log.info("异步线程执行3，线程名：" + Thread.currentThread().getName());
            log.info(MDC.get("aaa"));
        });

        AsyncUtils.execAsync(() -> {
            log.info("异步线程执行1，线程名：" + Thread.currentThread().getName());
            log.info(MDC.get("aaa"));
        });

        AsyncUtils.execAsync(() -> {
            log.info("异步线程执行2，线程名：" + Thread.currentThread().getName());
            log.info(MDC.get("aaa"));
        });
        future.get();

        for (int i = 0; i < 200; i++) {
            AsyncUtils.execAsync(() -> testParallelStream());
        }
//        testParallelStream();
    }

    @SneakyThrows
    @GetMapping("/testRequestContextHolderPerformance")
    public Result<String> testRequestContextHolderPerformance() {
        String header = ServletUtils.getRequest().getHeader("random");
        log.info("链路-开始: header={}, {}", header, ServletUtils.getRequest().getHeader("random"));
        MDC.put("aaa", "bbb");
        Future<?> future = AsyncUtils.execAsync(() -> {
            asyncService.sync2();
            AsyncUtils.sleep(500);
            log.info("header={}, 异步线程执行3，线程名：" + Thread.currentThread().getName(), header);
            log.info(MDC.get("aaa"));
        });

        AsyncUtils.execAsync(() -> {
            log.info("header={}, 异步线程执行1，线程名：" + Thread.currentThread().getName(), header);
            log.info(MDC.get("aaa"));
        });

        AsyncUtils.execAsync(() -> {
            log.info("header={}, 异步线程执行2，线程名：" + Thread.currentThread().getName(), header);
            log.info(MDC.get("aaa"));
            log.info("链路-中间: header={}, {}", header, ServletUtils.getRequest().getHeader("random"));
        });
        future.get();

        AsyncUtils.sleep(2000);
        asyncService.sync2();
        HttpServletRequest request = ServletUtils.getRequest();
        String headerVerify = ServletUtils.getRequest().getHeader("random");
        if (header.equals(headerVerify) == false) {
            throw new IllegalArgumentException("header不一致");
        }
        log.info("header={}, 6. 异步测试-异步方法执行完毕。{}", header,  ServletUtils.getHeaderMap(request));
        log.info("链路-结束: header={}, {}", header, headerVerify);
        return R.success(headerVerify);
    }
    
    /**
     * 并发上下文测试
     */
    @PostMapping("/execParallelLimitContext")
    public Result<?> execParallelLimitContext() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(i + "");
        }
        List<List<String>> split = ListUtils.partition(list, 10);
        List<String> list1 = new ArrayList<>();
        List<String> cowList = new CopyOnWriteArrayList<>();
        
        // ========== 并发上下文测试-集合【数据并发】==========
        AsyncUtils.execParallelLimit(5, split, item -> {
            String name = Thread.currentThread().getName();
            String s = StrUtil.subAfter(name, "-", true);
            list1.add(s);
            cowList.add(s);
            AsyncUtils.sleep(500);
            String syncContext = ServletUtils.getRequest().getHeader("syncContext");
            log.info("{}", item);
            log.info("syncContext Runnable: {}", syncContext);
        });
        System.out.println(list1.size());
        System.out.println(cowList.size());
        
        // ========== 并发上下文测试-任务并发 ==========
        TimeInterval timeInterval = DateUtils.timer();
        
        // 动作 1：获取用户信息（耗时 500ms）
        CompletableFuture<UserInfo> userTask = AsyncUtils.execParallelLimit(10, () -> {
            AsyncUtils.sleep(500);
            String clientIP = ServletUtils.getClientIP();
            UserInfo userInfo = new UserInfo();
            userInfo.setAddress(clientIP);
            return userInfo;
        });

        // 动作 2：获取订单信息（耗时 600ms）
        CompletableFuture<OrderInfo> orderTask = AsyncUtils.execParallelLimit(10, () -> {
            AsyncUtils.sleep(600);
            String acceptLanguage = MDC.get(HttpHeaders.ACCEPT_LANGUAGE);
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderDesc(acceptLanguage);
            return orderInfo;
        });

        // 动作 3：异步发一个通知（耗时 300ms，不需要返回值）
        AsyncUtils.execParallelLimit(5, () -> {
            AsyncUtils.sleep(300);
            String clientIP = ServletUtils.getClientIP();
            log.info("用户 {} 访问了该页面", clientIP);
        });

        // ---------- 这里是分水岭 ----------
        // 此时，userTask 和 orderTask 正在 ForkJoinPool(10) 里面【并发齐射】！

        // 主线程阻塞，等待两个独立任务的结果：
        UserInfo userInfo = userTask.join();
        OrderInfo orderInfo = orderTask.join();
        System.out.println(userInfo);
        System.out.println(orderInfo);
        // 总耗时不是 500+600 = 1100ms，而是 Max(500, 600) = 600ms！
        System.out.println("总耗时：" + timeInterval.intervalMs() + "ms");
        return R.success();
    }
    
    public void testParallelStream() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(i + "");
        }

        List<List<String>> split = ListUtils.partition(list, 10);
        Set<String> set = new HashSet<>();
        Set<String> set2 = new HashSet<>();

        AsyncUtils.execParallelLimit(5, split,item   -> {
            log.info("{}", item);
            String name = Thread.currentThread().getName();
            String s = StrUtil.subAfter(name, "-", true);
            set.add(s);
        });

        AsyncUtils.execParallelLimit(8, split, item -> {
            log.info("{}", item);
            String name = Thread.currentThread().getName();
            String s = StrUtil.subAfter(name, "-", true);
            set2.add(s);
        });
        
        CompletableFuture<String> forkJoinTask = AsyncUtils.execParallelLimit(5, () -> {
            String name = Thread.currentThread().getName();
            String s = StrUtil.subAfter(name, "-", true);
            set.add(s);
            return "aaaa";
        });

        String join = forkJoinTask.join();
        System.out.println(join);
        System.out.println(set.size());
        System.out.println(set2.size());
    }

}
