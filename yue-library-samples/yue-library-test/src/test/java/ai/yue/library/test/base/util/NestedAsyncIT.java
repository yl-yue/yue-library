package ai.yue.library.test.base.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 嵌套异步上下文隔离 — 回归测试。
 * <p>
 * 修复要点：ContextDecorator 在 Runnable 内通过 {@code new ServletRequestAttributes(request)}
 * 为每个 ForkJoinPool 子线程创建独立包装实例，避免嵌套异步场景下的并发竞态。
 * <p>
 * 对应修复：yue-library-web ContextDecorator.decorate()
 * 对应工单：[跨项目迭代需求] 嵌套异步连接串用
 *
 * @author ylyue
 * @since 2026/6/16
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NestedAsyncIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    // ======================== 核心路径：上下文隔离 ========================

    /**
     * 验证：嵌套异步场景下，每个 ForkJoinPool 子线程持有独立的 ServletRequestAttributes 实例。
     * 每个子线程的 identityHashCode 应各不相同。
     */
    @Test
    void contextIsolationEachSubThreadHasIndependentAttributes() {
        String url = "http://localhost:" + port + "/async/nestedAsyncParallelDb?parallelism=10&queryCount=1";

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        Map<String, Object> body = response.getBody();
        assertThat(body).isNotNull();
        List<String> diagnostics = (List<String>) body.get("data");
        assertThat(diagnostics).isNotNull().isNotEmpty();

        // 提取所有 requestAttrHash
        Set<String> hashes = new HashSet<>();
        List<String> errorLines = new ArrayList<>();
        for (String line : diagnostics) {
            if (line.contains("DB异常")) {
                errorLines.add(line);
            } else if (line.contains("requestAttrHash=")) {
                hashes.add(line.replaceAll(".*requestAttrHash=", "").trim());
            }
        }

        log.info("子线程数: {}, 不同 requestAttrHash 数: {}, DB 异常数: {}",
                diagnostics.stream().filter(s -> s.contains("requestAttrHash=")).count(),
                hashes.size(), errorLines.size());

        // 核心断言：每个子线程应有独立包装实例（hash 不重复）
        assertThat(hashes.size()).as("每个子线程应有独立的 ServletRequestAttributes 实例")
                .isEqualTo(diagnostics.stream().filter(s -> s.contains("requestAttrHash=")).count());
        assertThat(errorLines).as("DB 操作应零异常").isEmpty();
    }

    // ======================== 上下文正确性：HttpServletRequest 仍可用 ========================

    /**
     * 验证：嵌套异步场景下，子线程中独立包装的 HttpServletRequest 仍可正常访问
     * （getClientIP、getAttribute、getHeader 等）。
     */
    @Test
    void contextCorrectnessRequestAvailableInSubThreads() {
        String url = "http://localhost:" + port + "/async/nestedAsyncContextCorrectness?parallelism=10";

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        Map<String, Object> body = response.getBody();
        assertThat(body).isNotNull();
        List<Map<String, Object>> results = (List<Map<String, Object>>) body.get("data");
        assertThat(results).isNotNull().hasSize(10);

        Set<Integer> requestAttrHashes = new HashSet<>();
        for (Map<String, Object> info : results) {
            // 每个子线程能获取到 clientIP（不是 null）
            assertThat(info.get("clientIP")).as("子线程应能获取 clientIP").isNotNull();
            // 诊断属性应与 Controller 中设置的值一致
            String diagnosticAttr = (String) info.get("diagnosticAttr");
            assertThat(diagnosticAttr).as("子线程应能读取 request attribute").isNotNull().startsWith("ctx-");
            // 收集 hash
            requestAttrHashes.add((Integer) info.get("requestAttrHash"));
            log.info("子线程 {}: clientIP={}, diagnosticAttr={}, requestAttrHash={}",
                    info.get("thread"), info.get("clientIP"), diagnosticAttr, info.get("requestAttrHash"));
        }

        // 核心断言：所有子线程的 hash 互不相同 = 独立实例
        assertThat(requestAttrHashes.size()).as("每个子线程应有独立的上下文实例").isEqualTo(10);
    }

    // ======================== 并发压力：零异常 ========================

    /**
     * 高并发嵌套异步压力测试：8 个并发 HTTP 请求 × 12 并行 × 5 次 DB 查询 = 480 次并发 DB 操作。
     * 重复 5 轮。断言每轮 DB 异常数为零。
     */
    @RepeatedTest(5)
    void concurrentNestedAsyncNoDbException() throws Exception {
        int concurrentRequests = 8;
        int parallelism = 12;
        int queryCount = 5;

        CountDownLatch latch = new CountDownLatch(concurrentRequests);
        List<String> allErrors = new CopyOnWriteArrayList<>();
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int r = 0; r < concurrentRequests; r++) {
            int requestNo = r;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    latch.countDown();
                    latch.await();

                    String url = "http://localhost:" + port
                            + "/async/nestedAsyncParallelDb?parallelism=" + parallelism
                            + "&queryCount=" + queryCount;

                    ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
                    Map<String, Object> body = response.getBody();
                    if (body != null && body.get("data") instanceof List) {
                        @SuppressWarnings("unchecked")
                        List<String> dataList = (List<String>) body.get("data");
                        for (String line : dataList) {
                            if (line.contains("DB异常")) {
                                allErrors.add("[请求" + requestNo + "] " + line);
                            }
                        }
                    }
                } catch (Exception e) {
                    allErrors.add("[请求" + requestNo + "] HTTP异常: "
                            + e.getClass().getSimpleName() + ": "
                            + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
                }
            });
            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        log.info("并发请求数: {}, 每请求并行度: {}, 每任务DB查询: {} 次, 总异常数: {}",
                concurrentRequests, parallelism, queryCount, allErrors.size());
        if (!allErrors.isEmpty()) {
            log.error("压力测试发现异常: {}", allErrors);
        }

        // 修复后断言：零 DB 异常
        assertThat(allErrors).as("嵌套异步并发场景下 DB 操作应零异常").isEmpty();
    }

    // ======================== 非嵌套回归：直接调用 execParallelLimit ========================

    /**
     * 回归验证：非嵌套场景下直接调用 execParallelLimit（无 @Async 包装）仍正常工作。
     * 使用独立 businessId 避免 ForkJoinPool 实例与其他测试共享导致 ThreadLocal 污染。
     */
    @Test
    void nonNestedExecParallelLimitStillWorks() {
        List<String> results = new CopyOnWriteArrayList<>();
        List<Integer> items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            items.add(i);
        }

        // 使用独立 businessId 隔离 ForkJoinPool，避免跨测试 ThreadLocal 污染
        ai.yue.library.base.util.AsyncUtils.execParallelLimit(
                "regression-non-nested-" + System.currentTimeMillis(), 10, items, item -> {
            results.add(Thread.currentThread().getName() + ":" + item);
        });

        assertThat(results).hasSize(10);
        log.info("非嵌套 execParallelLimit 验证通过，执行了 {} 个任务", results.size());
    }

}
