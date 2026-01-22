package ai.yue.library.base.crypto.annotation;

import java.lang.annotation.*;

/**
 * 响应加密
 *
 * @author ylyue
 * @since 2021/4/14
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface ResponseEncrypt {

    /**
     * {@link cn.hutool.v7.crypto.symmetric.SymmetricCrypto} Bean Name
     * - 创建一个SymmetricCrypto实例，将此实例注册成Bean，此处填写Bean名称
     * - 用于使用此SymmetricCrypto对象，进行请求解密
     */
    String symmetricCryptoFactoryBeanName();

}
