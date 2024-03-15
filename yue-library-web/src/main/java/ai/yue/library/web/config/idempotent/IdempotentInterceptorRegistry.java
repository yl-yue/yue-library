package ai.yue.library.web.config.idempotent;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

/**
 * 幂等性拦截器注册
 *
 * @author ylyue
 * @since 2021/5/28
 */
@Component
public class IdempotentInterceptorRegistry {


    /**
     * 添加幂等性拦截器
     */
    public void registry(InterceptorRegistry registry) {
        registry.addInterceptor(new IdempotentInterceptor());
    }

}
