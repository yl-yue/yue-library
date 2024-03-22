package ai.yue.library.data.redis.annotation;

import ai.yue.library.data.redis.custom.LockFailureStrategy;
import ai.yue.library.data.redis.custom.LockKeyBuilder;
import ai.yue.library.data.redis.config.properties.RedisProperties.LockProperties;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分布式锁
 *
 * @author yl-yue
 * @since  2024/3/15
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Lock {

    /**
     * 用于多个方法使用同一把锁，可以理解为锁资源名称，为空则会使用：包名 + 类名 + 方法名
     */
    String name() default "";

    /**
     * 支持 SPEL 表达式（引用方法参数作为key，支持对象点点点）
     * <p>锁的key = name + keys</p>
     * <pre>
     *     user#id
     *     user#cllphone
     * </pre>
     */
    String[] keys() default "";

    /**
     * 锁过期时间（单位：毫秒）
     * <p>锁若一直未释放，多长时间后将强制释放</p>
     * <pre>
     *     过期时间一定是要长于业务的执行时间，未设置则默认为30秒：{@link LockProperties#getExpire()}
     * </pre>
     */
    long expire() default -1;

    /**
     * 锁获取时的超时时间（单位：毫秒）
     * <pre>
     *     结合业务，建议该时间不宜设置过长，特别在并发高的情况下。
     *     未设置则为默认3秒：{@link LockProperties#getAcquireTimeout()}
     * </pre>
     */
    long acquireTimeout() default -1;

    /**
     * 是否自动释放锁
     * <p>业务方法执行完后（方法内抛异常也算执行完）自动释放锁，如果为false，锁将不会自动释放直至到达过期时间才释放：{@link #expire()}</p>
     */
    boolean autoUnlock() default true;

    /**
     * 失败策略
     */
    Class<? extends LockFailureStrategy> failStrategy() default LockFailureStrategy.class;

    /**
     * key生成器策略
     */
    Class<? extends LockKeyBuilder> keyBuilderStrategy() default LockKeyBuilder.class;

}
