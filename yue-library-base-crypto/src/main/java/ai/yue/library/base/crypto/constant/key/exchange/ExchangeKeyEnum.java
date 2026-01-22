package ai.yue.library.base.crypto.constant.key.exchange;

import cn.hutool.v7.core.codec.binary.HexUtil;
import cn.hutool.v7.crypto.SecureUtil;
import cn.hutool.v7.crypto.asymmetric.AbstractAsymmetricCrypto;
import cn.hutool.v7.crypto.bc.SmUtil;
import cn.hutool.v7.crypto.symmetric.SymmetricCrypto;

/**
 * 交换密钥类型
 *
 * @author ylyue
 * @since 2021/4/13
 */
public enum ExchangeKeyEnum {

    /**
     * <p>使用RSA进行密钥交换的通信过程加密</p>
     * <p>使用AES作为最终交换密钥的加密算法</p>
     */
    RSA_AES {
        @Override
        public AbstractAsymmetricCrypto getAsymmetricCrypto(String privateKeyBase64, String publicKeyBase64) {
            return SecureUtil.rsa(privateKeyBase64, publicKeyBase64);
        }

        @Override
        public SymmetricCrypto getSymmetricCrypto(String key) {
            return SecureUtil.aes(key.getBytes());
        }

    },

    /**
     * <p>使用SM2进行密钥交换的通信过程加密</p>
     * <p>使用SM4作为最终交换密钥的加密算法</p>
     */
    SM2_SM4 {
        @Override
        public AbstractAsymmetricCrypto getAsymmetricCrypto(String privateKeyBase64, String publicKeyBase64) {
            return SmUtil.sm2(privateKeyBase64, publicKeyBase64);
        }

        @Override
        public SymmetricCrypto getSymmetricCrypto(String key) {
            return SmUtil.sm4(HexUtil.decode(key));
        }

    };

    /**
     * 获得非对称加密实例
     *
     * @param privateKeyBase64 Base64私钥
     * @param publicKeyBase64  Base64公钥
     */
    public abstract AbstractAsymmetricCrypto getAsymmetricCrypto(String privateKeyBase64, String publicKeyBase64);

    /**
     * 获得对称加密实例
     */
    public abstract SymmetricCrypto getSymmetricCrypto(String key);

}
