package ai.yue.library.test.data.log;

import ai.yue.library.data.log.service.LogMaskService;
import com.alibaba.fastjson2.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * LogMaskService 脱敏服务测试
 * <p>默认策略仅对密码类字段置空，其他参数不脱敏</p>
 *
 * @author ylyue
 * @since 2025/5/13
 */
class LogMaskServiceTest {

    private final LogMaskService logMaskService = new LogMaskService();

    @Test
    void maskParam_empty() {
        assertEquals("", logMaskService.maskParam(""));
    }

    @Test
    void maskParam_blank() {
        assertEquals("   ", logMaskService.maskParam("   "));
    }

    @Test
    void maskParam_password() {
        String param = "{\"username\":\"admin\",\"password\":\"123456\"}";
        String result = logMaskService.maskParam(param);
        JSONObject json = JSONObject.parseObject(result);
        assertEquals("admin", json.getString("username"));
        assertEquals("", json.getString("password"));
    }

    @Test
    void maskParam_pwd() {
        String param = "{\"pwd\":\"secret\"}";
        String result = logMaskService.maskParam(param);
        JSONObject json = JSONObject.parseObject(result);
        assertEquals("", json.getString("pwd"));
    }

    @Test
    void maskParam_passwd() {
        String param = "{\"passwd\":\"secret\"}";
        String result = logMaskService.maskParam(param);
        JSONObject json = JSONObject.parseObject(result);
        assertEquals("", json.getString("passwd"));
    }

    @Test
    void maskParam_secret() {
        String param = "{\"secret\":\"abc\"}";
        String result = logMaskService.maskParam(param);
        JSONObject json = JSONObject.parseObject(result);
        assertEquals("", json.getString("secret"));
    }

    @Test
    void maskParam_accessToken() {
        String param = "{\"accessToken\":\"tk123\"}";
        String result = logMaskService.maskParam(param);
        JSONObject json = JSONObject.parseObject(result);
        assertEquals("", json.getString("accessToken"));
    }

    @Test
    void maskParam_phoneNotMasked() {
        String param = "{\"phone\":\"13812345678\"}";
        String result = logMaskService.maskParam(param);
        assertTrue(result.contains("13812345678"));
    }

    @Test
    void maskParam_emailNotMasked() {
        String param = "{\"email\":\"test@example.com\"}";
        String result = logMaskService.maskParam(param);
        assertTrue(result.contains("test@example.com"));
    }

    @Test
    void maskParam_idCardNotMasked() {
        String param = "{\"idCard\":\"110101199003071234\"}";
        String result = logMaskService.maskParam(param);
        assertTrue(result.contains("110101199003071234"));
    }

    @Test
    void maskParam_nonJson() {
        String param = "plain text";
        String result = logMaskService.maskParam(param);
        assertEquals("plain text", result);
    }

    @Test
    void maskParam_normalParamNotMasked() {
        String param = "{\"name\":\"张三\",\"age\":\"25\",\"address\":\"北京市\"}";
        String result = logMaskService.maskParam(param);
        JSONObject json = JSONObject.parseObject(result);
        assertEquals("张三", json.getString("name"));
        assertEquals("25", json.getString("age"));
        assertEquals("北京市", json.getString("address"));
    }

    // ===== maskParam(param, additionalExcludeKeys) 测试 =====

    @Test
    void maskParamWithExclude_empty() {
        assertEquals("", logMaskService.maskParam("", Set.of("token")));
    }

    @Test
    void maskParamWithExclude_additionalKey() {
        String param = "{\"username\":\"admin\",\"token\":\"tk123\"}";
        String result = logMaskService.maskParam(param, Set.of("token"));
        JSONObject json = JSONObject.parseObject(result);
        assertEquals("admin", json.getString("username"));
        assertEquals("", json.getString("token"));
    }

    @Test
    void maskParamWithExclude_multipleAdditionalKeys() {
        String param = "{\"username\":\"admin\",\"token\":\"tk123\",\"apiKey\":\"ak456\"}";
        String result = logMaskService.maskParam(param, Set.of("token", "apiKey"));
        JSONObject json = JSONObject.parseObject(result);
        assertEquals("admin", json.getString("username"));
        assertEquals("", json.getString("token"));
        assertEquals("", json.getString("apiKey"));
    }

    @Test
    void maskParamWithExclude_mergedWithPasswordKeys() {
        String param = "{\"username\":\"admin\",\"password\":\"123\",\"token\":\"tk123\"}";
        String result = logMaskService.maskParam(param, Set.of("token"));
        JSONObject json = JSONObject.parseObject(result);
        assertEquals("admin", json.getString("username"));
        assertEquals("", json.getString("password"));
        assertEquals("", json.getString("token"));
    }

    @Test
    void maskParamWithExclude_keyNotInParam() {
        String param = "{\"username\":\"admin\"}";
        String result = logMaskService.maskParam(param, Set.of("token", "apiKey"));
        JSONObject json = JSONObject.parseObject(result);
        assertEquals("admin", json.getString("username"));
    }

    @Test
    void maskParamWithExclude_nonJson() {
        String param = "plain text";
        String result = logMaskService.maskParam(param, Set.of("token"));
        assertEquals("plain text", result);
    }

    @Test
    void maskParamWithExclude_normalParamNotAffected() {
        String param = "{\"name\":\"张三\",\"token\":\"tk123\"}";
        String result = logMaskService.maskParam(param, Set.of("token"));
        JSONObject json = JSONObject.parseObject(result);
        assertEquals("张三", json.getString("name"));
        assertEquals("", json.getString("token"));
    }

}
