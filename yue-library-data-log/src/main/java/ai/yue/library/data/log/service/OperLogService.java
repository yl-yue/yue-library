package ai.yue.library.data.log.service;

import ai.yue.library.base.util.AsyncUtils;
import ai.yue.library.data.log.config.LogProperties;
import ai.yue.library.data.log.entity.OperLogEntity;
import ai.yue.library.data.log.mapper.OperLogMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 操作日志 Service
 *
 * @author ylyue
 * @since 2025/5/13
 */
@Slf4j
@Service
public class OperLogService {

    @Resource
    private OperLogMapper operLogMapper;

    @Resource
    private LogProperties logProperties;

    @Resource(name = "logMaskService")
    private LogMaskService logMaskService;

    /**
     * 记录操作日志
     *
     * @param entity 操作日志实体
     */
    public void recordOper(OperLogEntity entity) {
        try {
            if (logProperties.getMaskEnabled() && entity.getRequestParam() != null) {
                entity.setRequestParam(logMaskService.maskParam(entity.getRequestParam()));
            }

            if (logProperties.getAsync()) {
                AsyncUtils.exec(() -> {
                    try {
                        operLogMapper.insert(entity);
                    } catch (Exception e) {
                        log.warn("【操作日志】异步记录失败：{}", e.getMessage());
                    }
                });
            } else {
                operLogMapper.insert(entity);
            }
        } catch (Exception e) {
            log.warn("【操作日志】记录异常：{}", e.getMessage());
        }
    }

}