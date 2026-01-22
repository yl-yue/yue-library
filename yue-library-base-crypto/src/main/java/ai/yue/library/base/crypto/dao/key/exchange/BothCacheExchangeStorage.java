package ai.yue.library.base.crypto.dao.key.exchange;

import ai.yue.library.base.crypto.config.properties.KeyExchangeProperties;
import ai.yue.library.base.crypto.dto.KeyExchangeStorageDTO;
import ai.yue.library.base.exception.ParamException;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.template.QuickConfig;
import cn.hutool.v7.core.text.StrUtil;

/**
 * 二级缓存密钥交换存储策略
 *
 * @author ylyue
 * @since 2021/4/12
 */
public class BothCacheExchangeStorage implements KeyExchangeStorage {

    private Cache<String, KeyExchangeStorageDTO> symmetricKeyCache;

    public BothCacheExchangeStorage(KeyExchangeProperties keyExchangeProperties, CacheManager cacheManager) {
        QuickConfig qc = QuickConfig.newBuilder("crypto:key_exchange:")
                .expire(keyExchangeProperties.getCacheExpire())
                .cacheType(CacheType.BOTH) // two level cache
                .syncLocal(true) // invalidate local cache in all jvm process after update
                .build();
        symmetricKeyCache = cacheManager.getOrCreateCache(qc);
    }

    private KeyExchangeStorageDTO getNotEmpty(String sessionKey) {
        KeyExchangeStorageDTO keyExchangeStorageDTO = symmetricKeyCache.get(sessionKey);
        if (keyExchangeStorageDTO == null) {
            throw new ParamException(StrUtil.format("BothCacheExchangeStorage未能获取到交换密钥：1. 请确认是否已进行过密钥交换；2. 请确认是否传入正确的sessionKey={}；", sessionKey));
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
        symmetricKeyCache.remove(sessionKey);
    }

}
