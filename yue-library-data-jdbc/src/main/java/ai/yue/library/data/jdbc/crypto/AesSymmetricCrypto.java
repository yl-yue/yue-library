package ai.yue.library.data.jdbc.crypto;

import cn.hutool.crypto.SecureUtil;

/**
 * AES数据加密对称加密算法实现
 *
 * @author ylyue
 * @since  2021/4/20
 */
public class AesSymmetricCrypto extends AbstractSymmetricCrypto {

    @Override
    public void initEncryptAlgorithm(String dataEncryptKey) {
        super.symmetricCrypto = SecureUtil.aes(dataEncryptKey.getBytes());
    }

}
