package ai.yue.library.base.crypto.dto;

import ai.yue.library.base.crypto.constant.key.exchange.ExchangeKeyEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 密钥交换存储DTO
 */
@Data
@AllArgsConstructor
public class KeyExchangeStorageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对称密钥
     */
    String symmetricKey;
    /**
     * 交换密钥类型
     */
    ExchangeKeyEnum exchangeKeyType;

}
