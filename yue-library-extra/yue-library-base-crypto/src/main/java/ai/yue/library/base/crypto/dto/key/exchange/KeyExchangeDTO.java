package ai.yue.library.base.crypto.dto.key.exchange;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 密钥交换过程传输变量定义
 *
 * @author ylyue
 * @since 2021/4/12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeyExchangeDTO implements Serializable {

    private static final long serialVersionUID = -6260130048712948980L;

    /**
     * 服务器与客户端第一步密钥交换所生成的私钥
     */
    private String privateKeyBase64;

    /**
     * 最终交换的对称加密密钥
     */
    private String exchangeKey;

}
