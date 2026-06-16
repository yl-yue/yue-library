package ai.yue.library.test.controller.base.async;

import ai.yue.library.base.util.AsyncUtils;
import ai.yue.library.test.entity.TableExampleStandard;
import ai.yue.library.test.service.TableExampleStandardService;
import ai.yue.library.web.util.ServletUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 嵌套异步上下文隔离回归验证 Service。
 * <p>
 * 验证场景：HTTP 请求 → {@code @Async} 方法 → {@link AsyncUtils#execParallelLimit}（并行 DB 查询）。
 * 修复后每个 ForkJoinPool 子线程应获得独立的 {@code ServletRequestAttributes} 包装实例，
 * 而非共享同一引用。
 * <p>
 * 对应修复：ContextDecorator 在 Runnable 内通过 {@code new ServletRequestAttributes(request)}
 * 为每个子线程创建独立包装实例。
 *
 * @author ylyue
 * @since 2026/6/16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NestedAsyncService {

    private final TableExampleStandardService tableExampleStandardService;

    /**
     * 嵌套异步 + 并行 DB 查询，返回各子线程的上下文诊断信息。
     * 回归验证点：每个子线程的 {@code RequestContextHolder.getRequestAttributes()} identityHashCode
     * 应各不相同（每个子线程持独立包装实例）。
     *
     * @param parallelism 并发度
     * @param queryCount  每个子任务执行的 DB 查询次数
     */
    @Async
    public CompletableFuture<List<String>> nestedAsyncParallelDb(int parallelism, int queryCount) {
        log.info("[嵌套异步] 异步线程启动，thread={}", Thread.currentThread().getName());

        List<TableExampleStandard> items = tableExampleStandardService.list();
        List<Long> ids = items.stream()
                .map(TableExampleStandard::getId)
                .limit(parallelism)
                .toList();

        List<String> diagnosticInfos = new CopyOnWriteArrayList<>();
        AtomicInteger completedCount = new AtomicInteger(0);

        // 第二层并发：每个子任务执行 DB 查询，记录各自的 requestAttr hash
        AsyncUtils.execParallelLimit(parallelism, ids, id -> {
            String threadName = Thread.currentThread().getName();
            int requestAttrHash = System.identityHashCode(RequestContextHolder.getRequestAttributes());
            diagnosticInfos.add(threadName + " → requestAttrHash=" + requestAttrHash);

            for (int i = 0; i < queryCount; i++) {
                try {
                    tableExampleStandardService.getById(id);
                } catch (Exception e) {
                    diagnosticInfos.add(threadName + " → DB异常: "
                            + e.getClass().getSimpleName() + ": "
                            + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
                    log.error("[嵌套异步-并发] DB查询失败, thread={}, id={}", threadName, id, e);
                    break;
                }
            }
            completedCount.incrementAndGet();
        });

        log.info("[嵌套异步] 全部子任务完成，完成数={}", completedCount.get());
        return CompletableFuture.completedFuture(diagnosticInfos);
    }

    /**
     * 验证嵌套异步场景下，每个子线程不仅能获取独立的 {@code ServletRequestAttributes}，
     * 其内部封装的 {@code HttpServletRequest} 仍可正常访问。
     * <p>
     * 在调用方 Controller 中先往请求设置诊断属性，本方法在子线程中读取这些属性来验证上下文正确性。
     *
     * @param parallelism     并发度
     * @param diagnosticValue 诊断属性值（由 Controller 写入 request）
     * @return 各子线程的上下文验证结果（threadName → {requestAttrHash, attributeValue, clientIP}）
     */
    @Async
    public CompletableFuture<List<Map<String, Object>>> verifyContextCorrectness(int parallelism, String diagnosticValue) {
        List<TableExampleStandard> items = tableExampleStandardService.list();
        List<TableExampleStandard> pool = items.stream().limit(parallelism).toList();

        List<Map<String, Object>> results = new CopyOnWriteArrayList<>();

        AsyncUtils.execParallelLimit(parallelism, pool, item -> {
            Map<String, Object> info = new LinkedHashMap<>();
            info.put("thread", Thread.currentThread().getName());
            info.put("requestAttrHash", System.identityHashCode(RequestContextHolder.getRequestAttributes()));
            // 验证 HttpServletRequest 仍可正常使用
            info.put("clientIP", ServletUtils.getClientIP());
            info.put("diagnosticAttr", ServletUtils.getRequest().getAttribute("diagnosticAttr"));
            info.put("id", item.getId());

            // 真实 DB 操作
            tableExampleStandardService.getById(item.getId());

            results.add(info);
        });

        return CompletableFuture.completedFuture(results);
    }

}
