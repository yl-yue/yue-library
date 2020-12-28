package ai.yue.library.web.config.thread.pool;

import ai.yue.library.base.config.thread.pool.AsyncProperties;
import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskDecorator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 子线程上下文装饰器
 *
 * @author ylyue
 * @since  2020/12/26
 */
public class ContextDecorator implements TaskDecorator {

    @Autowired
    AsyncProperties asyncProperties;

    @Override
    public Runnable decorate(Runnable runnable) {
        ServletRequestAttributes context = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        enableServletAsyncContext(context, asyncProperties);

        return () -> {
            try {
                RequestContextHolder.setRequestAttributes(context);
                runnable.run();
            } finally {
                RequestContextHolder.resetRequestAttributes();
            }
        };
    }

    /**
     * 启用 ServletAsyncContext
     * <p>用于阻塞父线程 Servlet 的关闭（调用 destroy() 方法），导致子线程获取的上下文为空</p>
     *
     * @param context 父线程上下文
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
