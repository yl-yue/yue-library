package ai.yue.library.data.jdbc.crypto;

import cn.hutool.crypto.SmUtil;

/**
 * SM4数据加密对称加密算法实现
 *
 * @author ylyue
 * @since  2021/4/20
 */
public class Sm4SymmetricCrypto extends AbstractSymmetricCrypto {

    @Override
    public void initEncryptAlgorithm(String dataEncryptKey) {
        super.symmetricCrypto = SmUtil.sm4(dataEncryptKey.getBytes());
    }

}
