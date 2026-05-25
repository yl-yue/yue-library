package ai.yue.library.data.log.service;

import ai.yue.library.data.log.entity.LoginLogEntity;
import ai.yue.library.data.log.entity.OperLogEntity;
import ai.yue.library.data.log.spi.LogStorageProvider;

/**
 * Forest 依赖缺失时的兜底空实现
 * <p>确保启动不报错，但日志不会被记录（WARN 已在注册时打印）。
 * 绝不静默降级为直连写库。</p>
 *
 * @author ylyue
 * @since 2025/5/25
 */
public class NoOpLogStorageProvider implements LogStorageProvider {

    @Override
    public void storeLoginLog(LoginLogEntity entity) {
        // Forest 依赖缺失，日志不记录
    }

    @Override
    public void storeOperLog(OperLogEntity entity) {
        // Forest 依赖缺失，日志不记录
    }

}
