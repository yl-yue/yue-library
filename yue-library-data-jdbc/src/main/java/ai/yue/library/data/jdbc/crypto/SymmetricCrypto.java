package ai.yue.library.data.jdbc.crypto;

import cn.hutool.core.util.CharsetUtil;

import java.io.Serializable;

/**
 * 数据加密对称加密算法
 *
 * @author ylyue
 * @since  2021/4/20
 */
public interface SymmetricCrypto extends Serializable {

    /**
     * 初始化加密算法
     *
     * @param dataEncryptKey 数据加密密钥
     */
    void initEncryptAlgorithm(String dataEncryptKey);

    /**
     * 加密，使用UTF-8编码
     *
     * @param data 被加密的字符串
     * @return 加密后的Base64
     */
    String encryptBase64(String data);

    /**
     * 解密Hex（16进制）或Base64表示的字符串，默认UTF-8编码
     *
     * @param data 被解密的String
     * @return 解密后的String
     */
    String decryptStr(String data);

}
