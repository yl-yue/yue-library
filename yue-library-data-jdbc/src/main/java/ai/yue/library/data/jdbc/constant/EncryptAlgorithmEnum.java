package ai.yue.library.data.jdbc.constant;

import ai.yue.library.base.util.SpringUtils;
import ai.yue.library.data.jdbc.crypto.AesSymmetricCrypto;
import ai.yue.library.data.jdbc.crypto.Sm4SymmetricCrypto;
import ai.yue.library.data.jdbc.crypto.SymmetricCrypto;

/**
 * 数据加密对称加密算法
 *
 * @author ylyue
 * @since 2021/4/19
 */
public enum EncryptAlgorithmEnum {

    /**
     * 使用AES算法进行数据对称加密
     */
    AES {
        @Override
        public SymmetricCrypto getSymmetricCrypto(String dataEncryptKey) {
            SymmetricCrypto symmetricCrypto = new AesSymmetricCrypto();
            symmetricCrypto.initEncryptAlgorithm(dataEncryptKey);
            return symmetricCrypto;
        }
    },

    /**
     * 使用SM4算法进行数据对称加密
     */
    SM4 {
        @Override
        public SymmetricCrypto getSymmetricCrypto(String dataEncryptKey) {
            SymmetricCrypto symmetricCrypto = new Sm4SymmetricCrypto();
            symmetricCrypto.initEncryptAlgorithm(dataEncryptKey);
            return symmetricCrypto;
        }
    },

    /**
     * 使用自定义算法（如：加密机）进行数据对称加密
     */
    CUSTOM {
        @Override
        public SymmetricCrypto getSymmetricCrypto(String dataEncryptKey) {
            SymmetricCrypto symmetricCrypto = SpringUtils.getBean(SymmetricCrypto.class);
            symmetricCrypto.initEncryptAlgorithm(dataEncryptKey);
            return symmetricCrypto;
        }
    };

    /**
     * 获得对称加密算法实例
     *
     * @param dataEncryptKey 数据加密密钥
     */
    public abstract SymmetricCrypto getSymmetricCrypto(String dataEncryptKey);

}
