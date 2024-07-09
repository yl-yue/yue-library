package ai.yue.library.base.annotation;

import java.lang.annotation.*;

/**
 * QPS 限流
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface QpsLimit {

    /**
     * 接口 QPS 限制
     * - 每秒请求限制，默认：100
     */
    int qps() default 100;

}
