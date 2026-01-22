package ai.yue.library.base.crypto.constant.key.exchange;

/**
 * 密钥交换存储类型
 */
public enum KeyExchangeStorageEnum {

    /**
     * 使用本地缓存（Caffeine）作为交换密钥的存储策略
     * <p>这是单节点的，不适合多节点（分布式）环境</p>
     */
    LOCAL_CACHE,

    /**
     * 使用二级缓存（Redis）和本地缓存（Caffeine）作为交换密钥的存储策略（推荐）
     * <p>可在多节点（分布式）环境下使用</p>
     */
    BOTH_CACHE

}
