package ai.yue.library.data.log.service;

import ai.yue.library.base.util.SpringUtils;
import ai.yue.library.data.log.spi.LogMaskStrategy;
import com.alibaba.fastjson2.JSONObject;
import cn.hutool.v7.core.text.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 日志脱敏服务
 * <p>整合默认脱敏策略与自定义 LogMaskStrategy SPI</p>
 * <p>默认策略仅对密码类字段置空处理，其他参数不做脱敏</p>
 *
 * @author ylyue
 * @since 2025/5/13
 */
@Slf4j
@Service("logMaskService")
public class LogMaskService {

    private static final Set<String> PASSWORD_KEYS = Set.of(
            "password", "pwd", "passwd", "secret", "accesstoken", "refreshtoken"
    );

    /**
     * 脱敏处理
     *
     * @param param 原始参数
     * @return 脱敏后参数
     */
    public String maskParam(String param) {
        if (StrUtil.isBlank(param)) {
            return param;
        }

        try {
            LogMaskStrategy customStrategy = getCustomStrategy();
            if (customStrategy != null) {
                return customStrategy.mask(param);
            }

            return defaultMask(param);
        } catch (Exception e) {
            log.warn("【日志脱敏】脱敏失败，使用原始参数：{}", e.getMessage());
            return param;
        }
    }

    private LogMaskStrategy getCustomStrategy() {
        try {
            return SpringUtils.getBean(LogMaskStrategy.class);
        } catch (Exception e) {
            return null;
        }
    }

    private String defaultMask(String param) {
        try {
            JSONObject json = JSONObject.parseObject(param);
            if (json == null) {
                return param;
            }

            for (String key : json.keySet()) {
                if (PASSWORD_KEYS.contains(key.toLowerCase())) {
                    json.put(key, "");
                }
            }

            return json.toJSONString();
        } catch (Exception e) {
            return param;
        }
    }

}