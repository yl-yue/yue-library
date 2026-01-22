package ai.yue.library.base.crypto.annotation;

import cn.hutool.v7.crypto.symmetric.SymmetricCrypto;

import jakarta.servlet.http.HttpServletRequest;

/**
 * - 用于 {@link RequestDecrypt} 注解，动态获取 SymmetricCrypto 实例
 * - 用于 {@link ResponseEncrypt} 注解，动态获取 SymmetricCrypto 实例
 */
public interface RequestDecryptEncryptFactory {

    /**
     * 动态获取 SymmetricCrypto，解决动态密钥等问题
     * - 密钥可以从请求Header中获取对应Key，然后查找内存或数据库中的密钥，达到按约定动态加解密的效果（如：根据客户端id查找，数据库存储的客户端密码进行加解密）
     */
    SymmetricCrypto getSymmetricCrypto(HttpServletRequest request);

}
