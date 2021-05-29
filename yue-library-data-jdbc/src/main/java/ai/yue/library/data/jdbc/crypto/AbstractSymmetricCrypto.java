package ai.yue.library.data.jdbc.crypto;

/**
 * 数据加密对称加密算法抽象实现
 *
 * @author ylyue
 * @since  2021/4/20
 */
public abstract class AbstractSymmetricCrypto implements SymmetricCrypto {

    protected cn.hutool.crypto.symmetric.SymmetricCrypto symmetricCrypto;

    @Override
    public String encryptBase64(String data) {
        return symmetricCrypto.encryptBase64(data);
    }

    @Override
    public String decryptStr(String data) {
        return symmetricCrypto.decryptStr(data);
    }

}
