package ai.yue.library.base.crypto.dao.key.exchange;

/**
 * 密钥交换存储策略
 *
 * @author ylyue
 * @since 2021/4/12
 */
public interface KeyExchangeStorage {

    /**
     * 获得服务器与客户端第一步密钥交换所生成的私钥
     *
     * @param storageKey 存储时的唯一键，如：UUID、token、userId等。
     * @return 第一步密钥交换所生成的私钥
     */
    String getPrivateKeyBase64(String storageKey);

    /**
     * 设置服务器与客户端第一步密钥交换所生成的私钥
     *
     * @param storageKey       存储时的唯一键，如：UUID、token、userId等。
     * @param privateKeyBase64 第一步密钥交换所生成的私钥
     */
    void setPrivateKeyBase64(String storageKey, String privateKeyBase64);

    /**
     * 获得交换密钥
     *
     * @param storageKey 存储时的唯一键，如：UUID、token、userId等。
     * @return 交换密钥
     */
    String getExchangeKey(String storageKey);

    /**
     * 设置交换密钥
     *
     * @param storageKey  存储时的唯一键，如：UUID、token、userId等。
     * @param exchangeKey 交换密钥
     */
    void setExchangeKey(String storageKey, String exchangeKey);

    /**
     * 添加存储key别名
     *
     * @param storageKey      存储时的唯一键，如：UUID、token、userId等。
     * @param storageKeyAlias 存储别名
     */
    void addAlias(String storageKey, String storageKeyAlias);

}
