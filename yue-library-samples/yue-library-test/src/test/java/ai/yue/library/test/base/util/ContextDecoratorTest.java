package ai.yue.library.test.base.util;

import ai.yue.library.base.config.thread.pool.AsyncProperties;
import ai.yue.library.web.config.thread.pool.AbstractContextDecorator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * AbstractContextDecorator 模板方法单元测试。
 * <p>
 * 验证 decorate() final 模板方法的上下文分发行为：
 * <ol>
 *   <li>无 Web 请求上下文（定时任务/非Web环境）→ 直接返回原任务，不进入子类装饰逻辑（消除 NPE 风险）</li>
 *   <li>有 Web 请求上下文 → 调用子类 decorateWithWebRequest 并传入非 null 的 context</li>
 * </ol>
 *
 * @author ylyue
 * @since 2026/7/4
 */
class ContextDecoratorTest {

    @AfterEach
    void clear() {
        RequestContextHolder.resetRequestAttributes();
    }

    /**
     * 无 Web 请求上下文时（定时任务线程/非Web环境），decorate() 直接返回原任务，
     * 不调用子类 decorateWithWebRequest，从设计上消除 NPE 风险。
     */
    @Test
    void decorateWithoutWebRequestReturnsOriginalRunnable() {
        // 确保无 RequestAttributes（模拟定时任务线程）
        RequestContextHolder.resetRequestAttributes();

        AtomicInteger executed = new AtomicInteger(0);
        Runnable original = () -> executed.incrementAndGet();

        // 子类 decorateWithWebRequest 若被调用则抛断言错误
        AbstractContextDecorator decorator = new AbstractContextDecorator(new AsyncProperties()) {
            @Override
            protected Runnable decorateWithWebRequest(Runnable runnable, ServletRequestAttributes context) {
                throw new AssertionError("无 Web 上下文时不应调用 decorateWithWebRequest");
            }
        };

        Runnable decorated = decorator.decorate(original);
        assertSame(original, decorated, "无 Web 上下文时应直接返回原任务");

        decorated.run();
        assertThat(executed.get()).isEqualTo(1);
    }

    /**
     * 有 Web 请求上下文时，decorate() 调用子类 decorateWithWebRequest 并传入非 null 的 context。
     */
    @Test
    void decorateWithWebRequestCalledWhenContextExists() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletRequestAttributes attrs = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attrs);

        AtomicInteger executed = new AtomicInteger(0);
        Runnable original = () -> executed.incrementAndGet();
        ServletRequestAttributes[] capturedContext = new ServletRequestAttributes[1];

        AbstractContextDecorator decorator = new AbstractContextDecorator(new AsyncProperties()) {
            @Override
            protected Runnable decorateWithWebRequest(Runnable runnable, ServletRequestAttributes context) {
                capturedContext[0] = context;
                return runnable;
            }
        };

        Runnable decorated = decorator.decorate(original);
        assertSame(attrs, capturedContext[0], "应将父线程 context 传入 decorateWithWebRequest");

        decorated.run();
        assertThat(executed.get()).isEqualTo(1);
    }

}
