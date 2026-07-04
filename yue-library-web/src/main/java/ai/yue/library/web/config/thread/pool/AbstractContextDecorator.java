package ai.yue.library.web.config.thread.pool;

import ai.yue.library.base.config.thread.pool.AsyncProperties;
import ai.yue.library.base.convert.Convert;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.core.task.TaskDecorator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * <h2>子线程上下文装饰器</h2>
 * <p>https://stackoverflow.com/questions/23732089/how-to-enable-request-scope-in-async-task-executor</p>
 * <p>传递：RequestAttributes and MDC and SecurityContext</p>
 *
 * @author ylyue
 * @since  2020/12/26
 */
@AllArgsConstructor
public abstract class AbstractContextDecorator implements TaskDecorator {

    protected AsyncProperties asyncProperties;

    /**
     * 模板方法：统一处理无 Web 请求上下文的场景（定时任务线程、非 Web 环境、测试场景）。
     * <p>当无 Servlet 上下文时直接返回原任务，不进入子类装饰逻辑，从设计上消除子线程 NPE 风险。</p>
     *
     * @param runnable 原始任务
     * @return 装饰后的任务
     */
    @Override
    public final Runnable decorate(Runnable runnable) {
        // 无 Servlet 上下文时（如非 Web 环境或定时任务线程）直接返回原任务
        if (RequestContextHolder.getRequestAttributes() == null) {
            return runnable;
        }
        ServletRequestAttributes context = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return decorateWithWebRequest(runnable, context);
    }

    /**
     * 有 Web 请求上下文时的装饰逻辑（子类实现）
     * <p>父类已确保传入的 context 非 null，子类无需再做 null 检查</p>
     *
     * @param runnable 原始任务
     * @param context  父线程的 Servlet 请求上下文（已确保非 null）
     * @return 装饰后的任务
     */
    protected abstract Runnable decorateWithWebRequest(Runnable runnable, ServletRequestAttributes context);

    /**
     * 启用 ServletAsyncContext，异步上下文最长生命周期（最大阻塞父线程多久）
     * <p>用于阻塞父线程 Servlet 的关闭（调用 destroy() 方法），导致子线程获取的上下文为空</p>
     *
     * @param context         父线程上下文
     * @param asyncProperties 异步属性配置
     */
    protected void enableServletAsyncContext(ServletRequestAttributes context, AsyncProperties asyncProperties) {
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

    /**
     * 完成异步请求处理并关闭响应流
     *
     * @param context         父线程上下文
     * @param asyncProperties 异步属性配置
     */
    protected void completeServletAsyncContext(ServletRequestAttributes context, AsyncProperties asyncProperties) {
        if (asyncProperties.isEnableServletAsyncContext()) {
            context.getRequest().getAsyncContext().complete();
        }
    }

}
