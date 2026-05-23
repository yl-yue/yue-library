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
        return maskParam(param, Set.of());
    }

    /**
     * 脱敏处理（支持追加排除参数名）
     * <p>注解级 excludeParamNames 追加到框架内置排除集合，合并置空处理</p>
     * <p>当存在自定义 LogMaskStrategy SPI 时，先执行框架级排除，再交由 SPI 处理</p>
     *
     * @param param                原始参数
     * @param additionalExcludeKeys 追加排除的参数名集合（来自注解 excludeParamNames）
     * @return 脱敏后参数
     */
    public String maskParam(String param, Set<String> additionalExcludeKeys) {
        if (StrUtil.isBlank(param)) {
            return param;
        }

        try {
            // 先执行框架级排除（内置集合 + 追加集合）
            String masked = excludeKeys(param, additionalExcludeKeys);

            // 再交由自定义 SPI 处理
            LogMaskStrategy customStrategy = getCustomStrategy();
            if (customStrategy != null) {
                return customStrategy.mask(masked);
            }

            return masked;
        } catch (Exception e) {
            log.warn("【日志脱敏】脱敏失败，使用原始参数：{}", e.getMessage());
            return param;
        }
    }

    /**
     * 框架级排除：将内置排除集合 + 追加排除集合中的参数值置空
     */
    private String excludeKeys(String param, Set<String> additionalExcludeKeys) {
        try {
            JSONObject json = JSONObject.parseObject(param);
            if (json == null) {
                return param;
            }

            for (String key : json.keySet()) {
                if (PASSWORD_KEYS.contains(key.toLowerCase())) {
                    json.put(key, "");
                } else if (additionalExcludeKeys.contains(key)) {
                    json.put(key, "");
                }
            }

            return json.toJSONString();
        } catch (Exception e) {
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

}