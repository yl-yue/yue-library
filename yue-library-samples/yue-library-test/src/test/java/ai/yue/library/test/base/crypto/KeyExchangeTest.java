package ai.yue.library.test.base.crypto;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.crypto.constant.key.exchange.ExchangeKeyEnum;
import ai.yue.library.base.util.IdUtils;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.ipo.ValidationIPO;
import com.alibaba.fastjson2.JSONObject;
import cn.hutool.v7.core.codec.binary.HexUtil;
import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.crypto.SecureUtil;
import cn.hutool.v7.crypto.asymmetric.KeyType;
import cn.hutool.v7.crypto.asymmetric.RSA;
import cn.hutool.v7.crypto.asymmetric.SM2;
import cn.hutool.v7.crypto.bc.SmUtil;
import cn.hutool.v7.crypto.symmetric.AES;
import cn.hutool.v7.crypto.symmetric.SymmetricCrypto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

/**
 * 密钥交换加解密
 * 1. 前端生成会话key与RSA密钥对 → 前端将会话key与RSA公钥传输给后端
 * 2. 后端生成AES密钥，并使用会话key作为键存储AES密码 → 后端使用前端的RSA公钥加密AES密钥响应给前端
 *
 * @author ylyue
 * @since 2021/4/12
 */
public class KeyExchangeTest {

    private RestTemplate restTemplate = new RestTemplate();
    private String serverUrl = "http://localhost:8080";

    @Test
    public void aes() {
        // 密钥交换
        String sessionKey = IdUtils.getSimpleUUID();
        String url = serverUrl + "/open/v2.6/keyExchange/getSymmetricKey";
        RSA rsa = SecureUtil.rsa();
        JSONObject paramJson = new JSONObject();
        paramJson.put("sessionKey", sessionKey);
        paramJson.put("exchangeKeyType", ExchangeKeyEnum.RSA_AES);
        paramJson.put("clientPublicKey", rsa.getPublicKeyBase64());
        Result exchangeKeyResult = restTemplate.postForObject(url, paramJson, Result.class);
        exchangeKeyResult.successValidate();
        JSONObject exchangeKeyResultData = exchangeKeyResult.dataToJSONObject();
        Boolean enabled = exchangeKeyResultData.getBoolean("enabled");
        System.out.println("密钥交换启用：" + enabled);
        String clientPublicKeyEncryptSymmetricKey = exchangeKeyResultData.getString("clientPublicKeyEncryptSymmetricKey");
        String clientAesKey = rsa.decryptStr(clientPublicKeyEncryptSymmetricKey, KeyType.PrivateKey);
        AES aes = SecureUtil.aes(clientAesKey.getBytes());
        String encryptBase64 = aes.encryptBase64("123456");
        System.out.println(encryptBase64);
        String decryptStr = aes.decryptStr(encryptBase64);
        System.out.println(decryptStr);
        String privateKeyBase64 = rsa.getPrivateKeyBase64();
        System.out.println("privateKeyBase64:　" + privateKeyBase64);
        Assertions.assertEquals("123456", decryptStr);

        // 业务接口请求解密测试
        ValidationIPO validationIPO = new ValidationIPO();
        validationIPO.setName("张三");
        validationIPO.setBirthday(LocalDate.now());
        validationIPO.setIdcard("110115202502138335");
        validationIPO.setAge(18);
        validationIPO.setEmail("aa@qq.com");
        validationIPO.setCellphone("18523446366");
        String userIPOToEncryptBase64 = aes.encryptBase64(JSONObject.toJSONString(validationIPO));
        System.out.println(userIPOToEncryptBase64);
        MultiValueMap headers = new LinkedMultiValueMap();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity httpEntity = new HttpEntity<>(userIPOToEncryptBase64, headers);
        Result exchangeKeyResult4 = restTemplate.postForObject(serverUrl + "/open/v1/keyExchange/keyExchangeEncryptDecrypt?access_token=" + sessionKey, httpEntity, Result.class);
        exchangeKeyResult4.successValidate();
        System.out.println(exchangeKeyResult4);

        // 业务接口响应加密测试
        String serverEncryptContent = (String) exchangeKeyResult4.getData();
        ValidationIPO validationIPO2 = Convert.toJavaBean(aes.decryptStr(serverEncryptContent), ValidationIPO.class);
        Assertions.assertEquals(validationIPO, validationIPO2);

        // 注销交换密钥
        JSONObject paramJson2 = new JSONObject();
        paramJson2.put("sessionKey", sessionKey);
        Result exchangeKeyResult2 = restTemplate.postForObject(serverUrl + "/open/v2.6/keyExchange/logoutSymmetricKey", paramJson2, Result.class);
        exchangeKeyResult2.successValidate();
    }

    @Test
    public void sm4() {
        // 密钥交换
        String sessionKey = IdUtils.getSimpleUUID();
        String url = serverUrl + "/open/v2.6/keyExchange/getSymmetricKey";
        SM2 sm2 = SmUtil.sm2();
        JSONObject paramJson = new JSONObject();
        paramJson.put("sessionKey", sessionKey);
        paramJson.put("exchangeKeyType", ExchangeKeyEnum.SM2_SM4);
        paramJson.put("clientPublicKey", sm2.getPublicKeyBase64());
        Result exchangeKeyResult = restTemplate.postForObject(url, paramJson, Result.class);
        exchangeKeyResult.successValidate();
        JSONObject exchangeKeyResultData = exchangeKeyResult.dataToJSONObject();
        Boolean enabled = exchangeKeyResultData.getBoolean("enabled");
        System.out.println("密钥交换启用：" + enabled);
        String clientPublicKeyEncryptSymmetricKey = exchangeKeyResultData.getString("clientPublicKeyEncryptSymmetricKey");
        String exchangeKey = sm2.decryptStr(clientPublicKeyEncryptSymmetricKey, KeyType.PrivateKey);
        SymmetricCrypto sm4 = SmUtil.sm4(HexUtil.decode(exchangeKey));
        String encryptBase64 = sm4.encryptBase64("123456");
        System.out.println(encryptBase64);
        String decryptStr = sm4.decryptStr(encryptBase64);
        System.out.println(decryptStr);
        Assertions.assertEquals("123456", decryptStr);

        // 业务接口请求解密测试
        ValidationIPO validationIPO = new ValidationIPO();
        validationIPO.setName("张三");
        validationIPO.setBirthday(LocalDate.now());
        validationIPO.setIdcard("110115202502138335");
        validationIPO.setAge(18);
        validationIPO.setEmail("aa@qq.com");
        validationIPO.setCellphone("18523446366");
        String userIPOToEncryptBase64 = sm4.encryptBase64(JSONObject.toJSONString(validationIPO));
        Console.log("sessionKey={}", sessionKey);
        Console.log("exchangeKey={}", exchangeKey);
        Console.log("userIPOToEncryptBase64={}", userIPOToEncryptBase64);
        MultiValueMap headers = new LinkedMultiValueMap();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity httpEntity = new HttpEntity<>(userIPOToEncryptBase64, headers);
        Result exchangeKeyResult4 = restTemplate.postForObject(serverUrl + "/open/v1/keyExchange/keyExchangeEncryptDecrypt?access_token=" + sessionKey, httpEntity, Result.class);
        exchangeKeyResult4.successValidate();
        System.out.println(exchangeKeyResult4);

        // 业务接口响应加密测试
        String serverEncryptContent = (String) exchangeKeyResult4.getData();
        ValidationIPO validationIPO2 = Convert.toJavaBean(sm4.decryptStr(serverEncryptContent), ValidationIPO.class);
        Assertions.assertEquals(validationIPO, validationIPO2);

        // 注销交换密钥
        JSONObject paramJson2 = new JSONObject();
        paramJson2.put("sessionKey", sessionKey);
        Result exchangeKeyResult2 = restTemplate.postForObject(serverUrl + "/open/v2.6/keyExchange/logoutSymmetricKey", paramJson2, Result.class);
        exchangeKeyResult2.successValidate();
    }

}
