package ai.yue.library.data.log.service;

import ai.yue.library.base.util.AsyncUtils;
import ai.yue.library.data.log.config.LogProperties;
import ai.yue.library.data.log.entity.LoginLogEntity;
import ai.yue.library.data.log.mapper.LoginLogMapper;
import ai.yue.library.data.log.spi.LogMaskStrategy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 登录日志 Service
 *
 * @author ylyue
 * @since 2025/5/13
 */
@Slf4j
@Service
public class LoginLogService {

    @Resource
    private LoginLogMapper loginLogMapper;

    @Resource
    private LogProperties logProperties;

    @Resource(name = "logMaskService")
    private LogMaskService logMaskService;

    /**
     * 记录登录日志
     *
     * @param username      登录账号
     * @param ip            登录IP
     * @param success       是否成功
     * @param msg           登录消息
     * @param requestParam  请求参数
     */
    public void recordLogin(String username, String ip, boolean success, String msg, String requestParam) {
        try {
            LoginLogEntity entity = LoginLogEntity.builder()
                    .username(username)
                    .ip(ip)
                    .loginTime(System.currentTimeMillis())
                    .status(success ? 1 : 0)
                    .msg(msg)
                    .requestParam(maskParam(requestParam))
                    .build();

            if (logProperties.getAsync()) {
                AsyncUtils.exec(() -> {
                    try {
                        loginLogMapper.insert(entity);
                    } catch (Exception e) {
                        log.warn("【登录日志】异步记录失败：{}", e.getMessage());
                    }
                });
            } else {
                loginLogMapper.insert(entity);
            }
        } catch (Exception e) {
            log.warn("【登录日志】记录异常：{}", e.getMessage());
        }
    }

    private String maskParam(String param) {
        if (!logProperties.getMaskEnabled() || param == null) {
            return param;
        }
        return logMaskService.maskParam(param);
    }

}