package ai.yue.library.base.crypto.dao.key.exchange;

import ai.yue.library.base.crypto.dto.KeyExchangeStorageDTO;

/**
 * 密钥交换存储策略
 *
 * @author ylyue
 * @since 2021/4/12
 */
public interface KeyExchangeStorage {

    /**
     * 获得对称密钥
     *
     * @param sessionKey 会话key，存储对称密钥（如：UUID、token、userId等）
     * @return 密钥交换存储DTO
     */
    KeyExchangeStorageDTO getKeyExchangeStorageDTO(String sessionKey);

    /**
     * 设置对称密钥
     *
     * @param sessionKey            会话key，存储对称密钥（如：UUID、token、userId等）
     * @param keyExchangeStorageDTO 密钥交换存储DTO
     */
    void setKeyExchangeStorageDTO(String sessionKey, KeyExchangeStorageDTO keyExchangeStorageDTO);

    /**
     * 删除对称密钥
     *
     * @param sessionKey      会话key，存储对称密钥（如：UUID、token、userId等）
     */
    void delKeyExchangeStorageDTO(String sessionKey);

}
