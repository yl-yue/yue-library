package ai.yue.library.test.base.crypto;

import ai.yue.library.base.crypto.constant.key.exchange.ExchangeKeyEnum;
import ai.yue.library.base.crypto.dto.key.exchange.KeyExchangeDTO;
import ai.yue.library.base.crypto.util.EncryptParamUtils;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 加密测试
 *
 * @author ylyue
 * @since 2021/4/14
 */
public class CryptoTest {

    @Test
    public void aes() {
        System.out.println(SecureUtil.aes("1234567890111111".getBytes()).decryptStr("VS6PufDhyuLoraax9QwySQ=="));
        System.out.println(SecureUtil.aes("1234567890111111".getBytes()).decryptStr("GvWL6o+mEkz52hZ9vhEmJA=="));
        System.out.println(SecureUtil.aes("1234567890111111".getBytes()).decryptStr("dmYzU+/ToD+UNf+gqqjQjg=="));
    }

    @Test
    public void toEncryptByteTest() {
        AES aes = SecureUtil.aes("1234567890111111".getBytes());
        String encryptedBase64 = aes.encryptBase64(EncryptParamUtils.toEncryptByte("encrypt"));
        System.out.println(encryptedBase64);
        System.out.println(aes.decryptStr(encryptedBase64));
        System.out.println();

        JSONObject paramJson = new JSONObject();
        paramJson.put("1", 666666);
        paramJson.put("2", 6336666);
        String encryptedBase642 = aes.encryptBase64(EncryptParamUtils.toEncryptByte(paramJson));
        System.out.println(encryptedBase642);
        System.out.println(aes.decryptStr(encryptedBase642));
        System.out.println();

        List<String> stringList = new ArrayList<>();
        stringList.add("1111");
        stringList.add("222");
        String encryptedBase643 = aes.encryptBase64(EncryptParamUtils.toEncryptByte(stringList));
        System.out.println(encryptedBase643);
        System.out.println(aes.decryptStr(encryptedBase643));
        System.out.println();

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(paramJson);
        String encryptedBase644 = aes.encryptBase64(EncryptParamUtils.toEncryptByte(jsonArray));
        System.out.println(encryptedBase644);
        System.out.println(aes.decryptStr(encryptedBase644));
        System.out.println();

        List<JSONObject> jsonObjectList = new ArrayList<>();
        jsonObjectList.add(paramJson);
        String encryptedBase646 = aes.encryptBase64(EncryptParamUtils.toEncryptByte(jsonObjectList));
        System.out.println(encryptedBase646);
        System.out.println(aes.decryptStr(encryptedBase646));
        System.out.println();

        KeyExchangeDTO keyExchangeDTO = new KeyExchangeDTO();
        keyExchangeDTO.setExchangeKey("111111111");
        keyExchangeDTO.setPrivateKeyBase64("222222222222");
        String encryptedBase645 = aes.encryptBase64(EncryptParamUtils.toEncryptByte(keyExchangeDTO));
        System.out.println(encryptedBase645);
        System.out.println(aes.decryptStr(encryptedBase645));
        System.out.println();

        ExchangeKeyEnum exchangeKeyEnum = ExchangeKeyEnum.SM2_SM4;
        String encryptedBase647 = aes.encryptBase64(EncryptParamUtils.toEncryptByte(exchangeKeyEnum));
        System.out.println(encryptedBase647);
        System.out.println(aes.decryptStr(encryptedBase647));
        System.out.println();

        String encryptedBase648 = aes.encryptBase64(EncryptParamUtils.toEncryptByte(-0.2));
        System.out.println(encryptedBase648);
        System.out.println(aes.decryptStr(encryptedBase648));
        System.out.println();

        double[] doubles = {1, 0.2, -0.3, -9};
        String encryptedBase649 = aes.encryptBase64(EncryptParamUtils.toEncryptByte(doubles));
        System.out.println(encryptedBase649);
        System.out.println(aes.decryptStr(encryptedBase649));
        System.out.println();
    }

}
