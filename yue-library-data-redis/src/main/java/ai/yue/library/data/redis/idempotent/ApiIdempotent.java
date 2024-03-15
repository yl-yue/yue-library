package ai.yue.library.data.redis.idempotent;

import ai.yue.library.data.redis.config.properties.RedisProperties;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口幂等性
 *
 * @author ylyue
 * @since 2021/5/21
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiIdempotent {

    /**
     * 唯一索引条件（加锁、幂等条件）
     * <pre>
     *     如：userId、token等 -- 基于用户进行加锁，同一用户对同一接口的请求，必须执行完毕后才能发起新的请求，否则视为重复请求（无效请求）
     *     key的value会依次从Header、Query、Body中获取
     *     支持联合唯一约束，即多个参数作为条件
     *     此参数如果为空，即全局加锁，同一时间只全局处理一个请求，其他请求将返回重复请求提示
     * </pre>
     */
    String[] paramKeys() default {};

    /**
     * 幂等锁过期时间（单位：毫秒）
     * <p>超过此时间，如果幂等锁还未被释放，那么锁也将自动失效，解决幂等锁未被正确释放导致的阻塞问题。</p>
     * <p>此时间应该比接口的执行时间更长，避免锁的提前释放，导致的幂等问题</p>
     * <p>默认值：{@link RedisProperties#getApiIdempotentExpire()}</p>
     */
    int expire() default -1;

}
