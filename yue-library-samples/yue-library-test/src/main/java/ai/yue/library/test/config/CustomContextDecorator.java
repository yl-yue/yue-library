package ai.yue.library.test.config;

import ai.yue.library.base.config.thread.pool.AsyncProperties;
import ai.yue.library.web.config.thread.pool.AbstractContextDecorator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 自定义上下文装饰器
 *
 * @author ylyue
 * @since 2021/5/21
 */
@Slf4j
//@Component
public class CustomContextDecorator extends AbstractContextDecorator {

    public CustomContextDecorator(AsyncProperties asyncProperties) {
        super(asyncProperties);
    }

    @Override
    public Runnable decorate(Runnable runnable) {
        // Servlet上下文
        ServletRequestAttributes context = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        // ServletAsyncContext-enable：异步上下文最长生命周期（最大阻塞父线程多久）
        log.info("自定义上下文装饰器-ServletAsyncContext-enable：异步上下文最长生命周期（最大阻塞父线程多久）");
        enableServletAsyncContext(context, asyncProperties);
        return () -> {
            try {
                RequestContextHolder.setRequestAttributes(context);
                runnable.run();
            } finally {
                RequestContextHolder.resetRequestAttributes();
                // ServletAsyncContext-complete：完成异步请求处理并关闭响应流
                completeServletAsyncContext(context, asyncProperties);
                log.info("自定义上下文装饰器-ServletAsyncContext-complete：完成异步请求处理并关闭响应流");
            }
        };
    }

}
