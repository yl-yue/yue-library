package ai.yue.library.base.crypto.controller.key.exchange;

import ai.yue.library.base.annotation.ApiVersion;
import ai.yue.library.base.crypto.config.properties.KeyExchangeProperties;
import ai.yue.library.base.crypto.constant.key.exchange.ExchangeKeyEnum;
import ai.yue.library.base.crypto.dao.key.exchange.KeyExchangeStorage;
import ai.yue.library.base.crypto.dto.KeyExchangeStorageDTO;
import ai.yue.library.base.util.IdUtils;
import ai.yue.library.base.validation.Validator;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import com.alibaba.fastjson2.JSONObject;
import cn.hutool.v7.core.codec.binary.HexUtil;
import cn.hutool.v7.crypto.asymmetric.AbstractAsymmetricCrypto;
import cn.hutool.v7.crypto.asymmetric.KeyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * RSA密钥交换加解密
 * <p>1. 前端生成会话key与RSA密钥对 → 前端将会话key与RSA公钥传输给后端</p>
 * <p>2. 后端生成AES密钥，并使用会话key作为键存储AES密码 → 后端使用前端的RSA公钥加密AES密钥响应给前端</p>
 *
 * 好处：
 * <p>1. 最终的对称加密（AES）密钥未在传输过程中泄露</p>
 * <p>2. 每次会话生命周期中的加密密钥都是随机的</p>
 *
 * @author ylyue
 * @since 2021/4/12
 */
@ApiVersion(2.6)
@RestController
@RequestMapping("/open/{version}/keyExchange")
@ConditionalOnProperty(prefix = KeyExchangeProperties.PREFIX, name = "enable-controller", havingValue = "true")
public class KeyExchangeController {

    @Autowired
    private Validator validator;
    @Autowired
    private KeyExchangeProperties keyExchangeProperties;
    @Autowired
    private KeyExchangeStorage keyExchangeStorage;

    /**
     * 是否启用密钥交换
     * <p>接口防刷：网关使用ip限流</p>
     */
    @GetMapping("/isEnabled")
    public Result<?> isEnabled() {
        return R.success(keyExchangeProperties.isEnabled());
    }

    /**
     * 获得交换密钥
     * <p>接口防刷：网关使用ip限流</p>
     *
     * @param sessionKey               会话key，用于存储生成的对称密钥（如：token、userId、设备id等）
     * @param exchangeKeyType          交换密钥类型
     * @param clientPublicKey          客户端公钥（用于响应传输时加密对称密钥）
     * @return 客户端公钥加密的对称密钥
     */
    @PostMapping("/getSymmetricKey")
    public Result<?> getSymmetricKey(String sessionKey, ExchangeKeyEnum exchangeKeyType, String clientPublicKey) {
        // 参数校验
        validator.param(sessionKey).notEmpty(sessionKey);

        // 客户端RSA公钥加密随机AES密钥
        AbstractAsymmetricCrypto clientAsymmetricCrypto = exchangeKeyType.getAsymmetricCrypto(null, clientPublicKey);
        String symmetricKey = IdUtils.getRandomCode(16); // 128 bit = 128 / 8 = 16 byte
        if (exchangeKeyType == ExchangeKeyEnum.SM2_SM4) {
            symmetricKey = HexUtil.encodeStr(symmetricKey);
        }
        KeyExchangeStorageDTO keyExchangeStorageDTO = new KeyExchangeStorageDTO(symmetricKey, exchangeKeyType);
        keyExchangeStorage.setKeyExchangeStorageDTO(sessionKey, keyExchangeStorageDTO);
        String clientPublicKeyEncryptSymmetricKey  = clientAsymmetricCrypto.encryptBase64(symmetricKey, KeyType.PublicKey);

        // 返回结果
        JSONObject data = new JSONObject();
        data.put("symmetricKeyExpire", keyExchangeProperties.getCacheExpire().toHours());
        data.put("clientPublicKeyEncryptSymmetricKey", clientPublicKeyEncryptSymmetricKey);
        return R.success(data);
    }

    /**
     * 注销交换密钥
     *
     * @param sessionKey 会话key，用于存储生成的对称密钥（如：token、userId、设备id等）
     */
    @PostMapping("/logoutSymmetricKey")
    public Result<?> logoutSymmetricKey(String sessionKey) {
        keyExchangeStorage.delKeyExchangeStorageDTO(sessionKey);
        return R.success();
    }

    /**
     * 获得白名单
     */
    @GetMapping("/getWhiteList")
    public Result<List<String>> getWhiteList() {
        return R.success(keyExchangeProperties.getWhiteList());
    }

}
