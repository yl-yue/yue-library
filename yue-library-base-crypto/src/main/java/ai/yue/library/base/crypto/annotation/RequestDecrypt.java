package ai.yue.library.base.crypto.annotation;

import java.lang.annotation.*;

/**
 * 请求解密
 *
 * @author ylyue
 * @since 2021/4/14
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface RequestDecrypt {

    /**
     * {@link cn.hutool.v7.crypto.symmetric.SymmetricCrypto} Bean Name
     * - 创建一个SymmetricCrypto实例，将此实例注册成Bean，此处填写Bean名称
     * - 用于使用此SymmetricCrypto对象，进行请求解密
     */
    String symmetricCryptoFactoryBeanName();

    /**
     * 对某些header进行解密
     */
    String[] decryptHeaderNames() default {};

    /**
     * 对某些请求参数进行解密
     * - 与整个body解密互斥，需要进行单个参数解密，就不能进行整个body解密
     * - 此值为空，则进行整个body解密，此值不为空，则为配置的某些参数解密
     */
    String[] decryptParamNames() default {};

}
