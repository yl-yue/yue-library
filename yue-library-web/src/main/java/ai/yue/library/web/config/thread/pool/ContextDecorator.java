package ai.yue.library.web.config.thread.pool;

import ai.yue.library.base.config.thread.pool.AsyncProperties;
import org.slf4j.MDC;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Locale;
import java.util.Map;

/**
 * <h2>子线程上下文装饰器</h2>
 * <p>https://stackoverflow.com/questions/23732089/how-to-enable-request-scope-in-async-task-executor</p>
 * <p>传递：RequestAttributes and MDC and SecurityContext</p>
 *
 * @author ylyue
 * @since  2020/12/26
 */
public class ContextDecorator extends AbstractContextDecorator {

    public ContextDecorator(AsyncProperties asyncProperties) {
        super(asyncProperties);
    }

    @Override
    public Runnable decorate(Runnable runnable) {
        // Servlet上下文
        ServletRequestAttributes context = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        // i18n上下文
        Locale locale = LocaleContextHolder.getLocale();
        // 日志上下文
        Map<String, String> previous = MDC.getCopyOfContextMap();
        // ServletAsyncContext-enable：异步上下文最长生命周期（最大阻塞父线程多久）
        String enableServletAsyncContext = MDC.get("enableServletAsyncContext");
        if (enableServletAsyncContext == null) {
            MDC.put("enableServletAsyncContext", "true");
            enableServletAsyncContext(context, asyncProperties);
        }

        /**
         * RequestContextHolder.getRequestAttributes();
         * RequestContextHolder.setRequestAttributes(context);
         * RequestContextHolder.resetRequestAttributes();
         * 在高并发时会出现丢失上下文的情况。
         *
         * 解决方案一（有缺陷）：公开为子线程的可继承，手动清理上下文，当使用CompletableFuture时，高并发下仍会丢失上下文。
         * RequestContextHolder.getRequestAttributes();
         * RequestContextHolder.setRequestAttributes(context, true);
         * RequestContextHolder.resetRequestAttributes();
         *
         * 解决方案二（JVM自动回收）：绑定当前线程，无需清理上下文。
         * RequestContextHolder.currentRequestAttributes();
         * RequestContextHolder.setRequestAttributes(context);
         */
        return () -> {
            try {
                RequestContextHolder.setRequestAttributes(context);
                LocaleContextHolder.setLocale(locale);
                if (previous != null) {
                    MDC.setContextMap(previous);
                }
                runnable.run();
            } finally {
                // 日志上下文
                MDC.clear();
                // ServletAsyncContext-complete：完成异步请求处理并关闭响应流
                if (enableServletAsyncContext == null) {
                    completeServletAsyncContext(context, asyncProperties);
                }
            }
        };
    }

}
