package ai.yue.library.web.config.thread.pool;

import ai.yue.library.base.config.thread.pool.AsyncProperties;
import cn.hutool.core.convert.Convert;
import lombok.AllArgsConstructor;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <h2>子线程上下文装饰器</h2>
 * <p>https://stackoverflow.com/questions/23732089/how-to-enable-request-scope-in-async-task-executor</p>
 * <p>传递：RequestAttributes and MDC and SecurityContext</p>
 *
 * @author ylyue
 * @since  2020/12/26
 */
@AllArgsConstructor
public class ContextDecorator implements TaskDecorator {

    protected AsyncProperties asyncProperties;

    @Override
    public Runnable decorate(Runnable runnable) {
        // Servlet上下文
        ServletRequestAttributes context = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        // 日志上下文
        Map<String, String> previous = MDC.getCopyOfContextMap();
        // 异步上下文最长生命周期（最大阻塞父线程多久）
        enableServletAsyncContext(context, asyncProperties);
        return () -> {
            try {
                RequestContextHolder.setRequestAttributes(context);
                if (previous != null) {
                    MDC.setContextMap(previous);
                }
                runnable.run();
            } finally {
                RequestContextHolder.resetRequestAttributes();
                MDC.clear();
                if (asyncProperties.isEnableServletAsyncContext()) {
                    context.getRequest().getAsyncContext().complete();
                }
            }
        };
    }

    /**
     * 启用 ServletAsyncContext，异步上下文最长生命周期（最大阻塞父线程多久）
     * <p>用于阻塞父线程 Servlet 的关闭（调用 destroy() 方法），导致子线程获取的上下文为空</p>
     *
     * @param context         父线程上下文
     * @param asyncProperties 异步属性配置
     */
    public static void enableServletAsyncContext(ServletRequestAttributes context, AsyncProperties asyncProperties) {
        if (!asyncProperties.isEnableServletAsyncContext()) {
            return;
        }

        HttpServletRequest request = context.getRequest();
        request.startAsync();
        Object servletAsyncContextTimeoutMillis = request.getAttribute(AsyncProperties.SERVLET_ASYNC_CONTEXT_TIMEOUT_MILLIS);
        if (servletAsyncContextTimeoutMillis == null) {
            servletAsyncContextTimeoutMillis = asyncProperties.getServletAsyncContextTimeoutMillis();
        }

        request.getAsyncContext().setTimeout(Convert.toLong(servletAsyncContextTimeoutMillis));
    }

}
