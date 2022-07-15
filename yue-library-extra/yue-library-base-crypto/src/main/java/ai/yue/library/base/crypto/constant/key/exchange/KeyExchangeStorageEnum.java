package ai.yue.library.base.crypto.constant.key.exchange;

/**
 * @author ylyue
 * @since 2021/4/13
 */
public enum KeyExchangeStorageEnum {

    /**
     * 使用Map作为交换密钥的存储策略
     * <p>这是单节点的，不适合多节点（分布式）环境</p>
     */
    LOCAL_MAP,

    /**
     * 使用Redis作为交换密钥的存储策略（推荐）
     * <p>可在多节点（分布式）环境下使用</p>
     */
    REDIS

}
