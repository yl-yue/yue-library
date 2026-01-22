package ai.yue.library.test.config;

import ai.yue.library.base.crypto.annotation.RequestDecryptEncryptFactory;
import jakarta.servlet.http.HttpServletRequest;
import cn.hutool.v7.crypto.bc.SmUtil;
import cn.hutool.v7.crypto.symmetric.SM4;
import cn.hutool.v7.crypto.symmetric.SymmetricCrypto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求加解密SymmetricCrypto实例动态获取工厂
 */
@Component
public class Sm4DecryptEncryptFactory implements RequestDecryptEncryptFactory {

    Map<String, String> keyMap = new HashMap<>();

    {
        // 可根据需求查找数据库数据进行初始化，如：客户端id对应客户端密钥，实现动态内存查找等
        keyMap.put("key1", "NqFVXYzQTUWd6zX1");
        keyMap.put("key2", "NqFVXYzQTUWd6zX2");
        keyMap.put("key3", "NqFVXYzQTUWd6zX3");
    }

    @Override
    public SymmetricCrypto getSymmetricCrypto(HttpServletRequest request) {
        String sessionKey = request.getHeader("sessionKey");
        String key = keyMap.get(sessionKey);
        SM4 sm4 = SmUtil.sm4(key.getBytes());
        return sm4;
    }

}
