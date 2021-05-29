package ai.yue.library.web.config.idempotent;

import ai.yue.library.data.redis.idempotent.ApiIdempotentProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

/**
 * 幂等性拦截器注册
 *
 * @author ylyue
 * @since 2021/5/28
 */
@Component
@ConditionalOnClass(ApiIdempotentProperties.class)
@ConditionalOnBean(ApiIdempotentProperties.class)
public class IdempotentInterceptorRegistry {

    @Autowired
    ApiIdempotentProperties apiIdempotentProperties;

    /**
     * 添加幂等性拦截器
     */
    public void registry(InterceptorRegistry registry) {
        if (apiIdempotentProperties != null) {
            boolean idempotentEnabled = apiIdempotentProperties.isEnabled();
            if (idempotentEnabled) {
                registry.addInterceptor(new IdempotentInterceptor());
            }
        }
    }

}
