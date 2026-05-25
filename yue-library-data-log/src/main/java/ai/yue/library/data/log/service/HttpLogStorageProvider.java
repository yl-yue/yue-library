package ai.yue.library.data.log.service;

import ai.yue.library.data.log.entity.LoginLogEntity;
import ai.yue.library.data.log.entity.OperLogEntity;
import ai.yue.library.data.log.forest.LogStorageForestClient;
import ai.yue.library.data.log.spi.LogStorageProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * HTTP 转发持久化实现（mode=http 默认）
 * <p>通过 Forest HTTP 客户端将日志转发至日志服务的持久化接口</p>
 * <p>HTTP 转发失败采用异常隔离策略，仅打印 WARN 日志，不阻塞主流程</p>
 *
 * @author ylyue
 * @since 2025/5/13
 */
@Slf4j
@RequiredArgsConstructor
public class HttpLogStorageProvider implements LogStorageProvider {

    private final LogStorageForestClient forestClient;

    @Override
    public void storeLoginLog(LoginLogEntity entity) {
        try {
            forestClient.storeLoginLog(entity);
        } catch (Exception e) {
            log.warn("【日志HTTP转发】登录日志转发失败：{}", e.getMessage());
        }
    }

    @Override
    public void storeOperLog(OperLogEntity entity) {
        try {
            forestClient.storeOperLog(entity);
        } catch (Exception e) {
            log.warn("【日志HTTP转发】操作日志转发失败：{}", e.getMessage());
        }
    }

}
