package ai.yue.library.webflux.config.thread.pool;

import org.slf4j.MDC;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.task.TaskDecorator;

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
public class ContextDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        // i18n上下文
        Locale locale = LocaleContextHolder.getLocale();
        // 日志上下文
        Map<String, String> previous = MDC.getCopyOfContextMap();
        return () -> {
            try {
                LocaleContextHolder.setLocale(locale);
                if (previous != null) {
                    MDC.setContextMap(previous);
                }
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }

}
