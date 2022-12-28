package ai.yue.library.base.crypto.constant.key.exchange;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.AbstractAsymmetricCrypto;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

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
        public AbstractAsymmetricCrypto getAsymmetricCrypto() {
            return SecureUtil.rsa();
        }

        @Override
        public AbstractAsymmetricCrypto getAsymmetricCrypto(String privateKeyBase64, String publicKeyBase64) {
            return SecureUtil.rsa(privateKeyBase64, publicKeyBase64);
        }

        @Override
        public SymmetricCrypto getSymmetricCrypto(byte[] key) {
            return SecureUtil.aes(key);
        }

    },

    /**
     * <p>使用SM2进行密钥交换的通信过程加密</p>
     * <p>使用SM4作为最终交换密钥的加密算法</p>
     */
    SM2_SM4 {

        @Override
        public AbstractAsymmetricCrypto getAsymmetricCrypto() {
            return SmUtil.sm2();
        }

        @Override
        public AbstractAsymmetricCrypto getAsymmetricCrypto(String privateKeyBase64, String publicKeyBase64) {
            return SmUtil.sm2(privateKeyBase64, publicKeyBase64);
        }

        @Override
        public SymmetricCrypto getSymmetricCrypto(byte[] key) {
            return SmUtil.sm4(key);
        }

    };

    /**
     * 获得非对称加密实例
     */
    public abstract AbstractAsymmetricCrypto getAsymmetricCrypto();

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
    public abstract SymmetricCrypto getSymmetricCrypto(byte[] key);

}
