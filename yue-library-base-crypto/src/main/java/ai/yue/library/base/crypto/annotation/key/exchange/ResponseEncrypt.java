package ai.yue.library.base.crypto.annotation.key.exchange;

import ai.yue.library.base.crypto.constant.key.exchange.ExchangeKeyEnum;
import org.springframework.core.annotation.AliasFor;

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
     * 加密密钥
     * <p>仅当 {@link #enableExchangeKeyEncrypt()} == false时有效</p>
     */
    @AliasFor("key")
    String value() default "";

    /**
     * 加密密钥
     * <p>仅当 {@link #enableExchangeKeyEncrypt()} == false时有效</p>
     */
    @AliasFor("value")
    String key() default "";

    /**
     * 使用交换密钥加密
     */
    boolean enableExchangeKeyEncrypt() default true;

    /**
     * 交换密钥类型
     */
    ExchangeKeyEnum exchangeKeyType() default ExchangeKeyEnum.RSA_AES;

    /**
     * 使用OAuth2 Token获得交换密钥
     * <p>互斥{@link #headerNameGetExchangeKey()}与{@link #paramNameGetExchangeKey()}</p>
     */
    boolean useAuthTokenGetExchangeKey() default true;

    /**
     * 使用headerName获得交换密钥
     * <p>优先级1</p>
     * <p>互斥{@link #useAuthTokenGetExchangeKey()}</p>
     */
    String headerNameGetExchangeKey() default "Yue-ExchangeKey-StorageKey";

    /**
     * 使用paramName获得交换密钥
     * <p>优先级2</p>
     * <p>互斥{@link #useAuthTokenGetExchangeKey()}</p>
     */
    String paramNameGetExchangeKey() default "Yue-ExchangeKey-StorageKey";

}
