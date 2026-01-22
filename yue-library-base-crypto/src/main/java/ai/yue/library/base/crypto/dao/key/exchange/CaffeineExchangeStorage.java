package ai.yue.library.base.crypto.dao.key.exchange;

import ai.yue.library.base.crypto.config.properties.KeyExchangeProperties;
import ai.yue.library.base.crypto.dto.KeyExchangeStorageDTO;
import ai.yue.library.base.exception.ParamException;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import cn.hutool.v7.core.text.StrUtil;

import java.util.concurrent.TimeUnit;

/**
 * 本地缓存密钥交换存储策略
 *
 * @author ylyue
 * @since 2021/4/12
 */
public class CaffeineExchangeStorage implements KeyExchangeStorage {

    private Cache<String, KeyExchangeStorageDTO> symmetricKeyCache;

    public CaffeineExchangeStorage(KeyExchangeProperties keyExchangeProperties) {
        symmetricKeyCache = Caffeine.newBuilder()
                .maximumSize(Integer.MAX_VALUE)
                .expireAfterAccess(keyExchangeProperties.getCacheExpire().toHours(), TimeUnit.HOURS)
                .build();
    }

    private KeyExchangeStorageDTO getNotEmpty(String sessionKey) {
        KeyExchangeStorageDTO keyExchangeStorageDTO = symmetricKeyCache.getIfPresent(sessionKey);
        if (keyExchangeStorageDTO == null) {
            throw new ParamException(StrUtil.format("CaffeineExchangeStorage未能获取到交换密钥：1. 请确认是否已进行过密钥交换；2. 请确认是否传入正确的sessionKey={}；", sessionKey));
        }

        return keyExchangeStorageDTO;
    }

    @Override
    public KeyExchangeStorageDTO getKeyExchangeStorageDTO(String sessionKey) {
        return getNotEmpty(sessionKey);
    }

    @Override
    public void setKeyExchangeStorageDTO(String sessionKey, KeyExchangeStorageDTO keyExchangeStorageDTO) {
        symmetricKeyCache.put(sessionKey, keyExchangeStorageDTO);
    }

    @Override
    public void delKeyExchangeStorageDTO(String sessionKey) {
        symmetricKeyCache.invalidate(sessionKey);
    }

}
