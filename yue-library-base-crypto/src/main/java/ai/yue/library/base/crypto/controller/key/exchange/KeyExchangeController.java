package ai.yue.library.base.crypto.controller.key.exchange;

import ai.yue.library.base.annotation.api.version.ApiVersion;
import ai.yue.library.base.crypto.constant.key.exchange.ExchangeKeyEnum;
import ai.yue.library.base.crypto.dao.key.exchange.KeyExchangeStorage;
import ai.yue.library.base.util.IdUtils;
import ai.yue.library.base.validation.Validator;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.AbstractAsymmetricCrypto;
import cn.hutool.crypto.asymmetric.KeyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RSA密钥交换加密
 *
 * @author ylyue
 * @since 2021/4/12
 */
@ApiVersion(2.3)
@RestController
@RequestMapping("/open/{version}/keyExchange")
public class KeyExchangeController {

    @Autowired
    private KeyExchangeStorage keyExchangeStorage;
    @Autowired
    private Validator validator;

    /**
     * 通信过程加密
     * 1. 前端大写UUID     →     后端返回RSA公钥
     * 2. 前端生成RSA密钥对 → 前端通过后端RSA公钥加密前端RSA公钥并传输给后端     →     后端通过私钥解密前端的RSA公钥 → 后端通过前端RSA公钥加密AES密钥响应给前端
     *
     * 好处：
     * 1. 最终的对称加密（AES）密钥未在传输过程中泄露
     * 2. 每次会话生命周期中的加密密钥都是随机的
     */
    @PostMapping("/{storageKey}")
    public Result<?> getExchangeKey(@PathVariable String storageKey, ExchangeKeyEnum exchangeKeyType, @Nullable String encryptedClientPublicKey) {
        // 参数校验
        validator.param(storageKey).uuid(storageKey);

        // 执行第一步逻辑：返回绑定当前UUID的RSA公钥
        if (StrUtil.isEmpty(encryptedClientPublicKey)) {
            AbstractAsymmetricCrypto asymmetricCrypto = exchangeKeyType.getAsymmetricCrypto();
            keyExchangeStorage.setPrivateKeyBase64(storageKey, asymmetricCrypto.getPrivateKeyBase64());
            return R.success(asymmetricCrypto.getPublicKeyBase64());
        }

        // 执行第二步逻辑：通过私钥解密客户端的RSA公钥，然后通过得到的客户端RSA公钥加密随机AES密钥
        String privateKeyBase64 = keyExchangeStorage.getPrivateKeyBase64(storageKey);
        AbstractAsymmetricCrypto asymmetricCrypto = exchangeKeyType.getAsymmetricCrypto(privateKeyBase64,null);
        String clientPublicKey = asymmetricCrypto.decryptStr(encryptedClientPublicKey, KeyType.PrivateKey);
        AbstractAsymmetricCrypto clientAsymmetricCrypto = exchangeKeyType.getAsymmetricCrypto(null, clientPublicKey);
        // 128 bit = 128 / 8 = 16 byte
        String exchangeKey = IdUtils.getRandomCode(16);
        keyExchangeStorage.setExchangeKey(storageKey, exchangeKey);
        String clientPublicKeyEncryptExchangeKey  = clientAsymmetricCrypto.encryptBase64(exchangeKey, KeyType.PublicKey);
        return R.success(clientPublicKeyEncryptExchangeKey);
    }

    /**
     * 添加存储key别名
     *
     * @param storageKey      存储时的唯一键，如：UUID、token、userId等。
     * @param storageKeyAlias 存储别名
     */
    @PostMapping("/{storageKey}/addAlias")
    public Result<?> addAlias(@PathVariable String storageKey, String storageKeyAlias) {
        keyExchangeStorage.addAlias(storageKey, storageKeyAlias);
        return R.success();
    }

}
