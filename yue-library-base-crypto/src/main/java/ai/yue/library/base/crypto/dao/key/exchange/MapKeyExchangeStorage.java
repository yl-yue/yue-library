package ai.yue.library.base.crypto.dao.key.exchange;

import ai.yue.library.base.crypto.dto.key.exchange.KeyExchangeDTO;
import ai.yue.library.base.exception.ParamException;

import java.util.HashMap;
import java.util.Map;

/**
 * Map密钥交换存储策略
 *
 * @author ylyue
 * @since 2021/4/12
 */
public class MapKeyExchangeStorage implements KeyExchangeStorage {

    private Map<String, KeyExchangeDTO> keyExchangeStorage = new HashMap<>();

    private KeyExchangeDTO getNotEmpty(String storageKey) {
        KeyExchangeDTO keyExchangeDTO = keyExchangeStorage.get(storageKey);
        if (keyExchangeDTO == null) {
            throw new ParamException("未能获取到交换密钥：1. 请确认是否已进行过密钥交换，2. 请确认是否传入keyExchangeStorageKey（如：token别名、UUID等）并指定了对应的Get方式。");
        }

        return keyExchangeDTO;
    }

    private KeyExchangeDTO getOrInit(String storageKey) {
        KeyExchangeDTO keyExchangeDTO = keyExchangeStorage.get(storageKey);
        keyExchangeDTO = keyExchangeDTO == null ? new KeyExchangeDTO() : keyExchangeDTO;
        return keyExchangeDTO;
    }

    /**
     * 服务器与客户端第一步密钥交换所生成的私钥
     *
     * @param storageKey 存储时的唯一键，如：UUID、token、userId等。
     * @return 第一步密钥交换所生成的私钥
     */
    @Override
    public String getPrivateKeyBase64(String storageKey) {
        return getNotEmpty(storageKey).getPrivateKeyBase64();
    }

    /**
     * 设置服务器与客户端第一步密钥交换所生成的私钥
     *
     * @param storageKey       存储时的唯一键，如：UUID、token、userId等。
     * @param privateKeyBase64 第一步密钥交换所生成的私钥
     */
    @Override
    public void setPrivateKeyBase64(String storageKey, String privateKeyBase64) {
        KeyExchangeDTO keyExchangeDTO = getOrInit(storageKey);
        keyExchangeDTO.setPrivateKeyBase64(privateKeyBase64);
        keyExchangeStorage.put(storageKey, keyExchangeDTO);
    }

    /**
     * 获得交换密钥
     *
     * @param storageKey 存储时的唯一键，如：UUID、token、userId等。
     * @return 交换密钥
     */
    @Override
    public String getExchangeKey(String storageKey) {
        return getNotEmpty(storageKey).getExchangeKey();
    }

    /**
     * 设置交换密钥
     *
     * @param storageKey  存储时的唯一键，如：UUID、token、userId等。
     * @param exchangeKey 交换密钥
     */
    @Override
    public void setExchangeKey(String storageKey, String exchangeKey) {
        KeyExchangeDTO keyExchangeDTO = getOrInit(storageKey);
        keyExchangeDTO.setExchangeKey(exchangeKey);
        keyExchangeStorage.put(storageKey, keyExchangeDTO);
    }

    /**
     * 添加存储key别名
     *
     * @param storageKey      存储时的唯一键，如：UUID、token、userId等。
     * @param storageKeyAlias 存储别名
     */
    @Override
    public void addAlias(String storageKey, String storageKeyAlias) {
        KeyExchangeDTO keyExchangeDTO = getOrInit(storageKey);
        keyExchangeStorage.put(storageKeyAlias, keyExchangeDTO);
    }

}
