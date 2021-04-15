package ai.yue.library.base.crypto.constant.key.exchange;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.AbstractAsymmetricCrypto;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 交换密钥类型
 *
 * @author ylyue
 * @since 2021/4/13
 */
@Getter
@AllArgsConstructor
public enum ExchangeKeyEnum {

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
    }, SM2_SM4 {
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

    public abstract AbstractAsymmetricCrypto getAsymmetricCrypto();
    public abstract AbstractAsymmetricCrypto getAsymmetricCrypto(String privateKeyBase64, String publicKeyBase64);
    public abstract SymmetricCrypto getSymmetricCrypto(byte[] key);

}
