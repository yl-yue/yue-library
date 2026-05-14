package ai.yue.library.data.log.service;

import ai.yue.library.data.log.config.LogProperties;
import ai.yue.library.data.mybatis.service.SqlService;
import com.alibaba.fastjson2.JSONObject;
import cn.hutool.v7.core.text.StrUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 日志归档 Service
 * <p>按 ID 范围批量归档，事务内 INSERT + DELETE</p>
 *
 * @author ylyue
 * @since 2025/5/13
 */
@Slf4j
@Service
public class LogArchiveService {

    @Resource
    private LogProperties logProperties;

    @Resource
    private SqlService sqlService;

    /**
     * 归档登录日志（定时任务，每天凌晨2点执行）
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void archiveLoginLog() {
        if (!logProperties.getArchiveEnabled()) {
            return;
        }

        try {
            long archiveTimeThreshold = System.currentTimeMillis() - logProperties.getArchiveMonths() * 30L * 24 * 3600 * 1000;

            JSONObject idRange = sqlService.queryForJson(null,
                    "SELECT MIN(id) AS minId, MAX(id) AS maxId FROM log_login WHERE create_time < " + archiveTimeThreshold, null);

            if (idRange == null || idRange.getLongValue("minId") == 0) {
                log.info("【登录日志归档】无满足条件的数据");
                return;
            }

            long minId = idRange.getLongValue("minId");
            long maxId = idRange.getLongValue("maxId");

            sqlService.execSqlForJson(null,
                    "INSERT INTO log_login_archive SELECT * FROM log_login WHERE id BETWEEN " + minId + " AND " + maxId, null);

            sqlService.execSqlForJson(null,
                    "DELETE FROM log_login WHERE id BETWEEN " + minId + " AND " + maxId, null);

            log.info("【登录日志归档】归档完成，ID范围：{}-{}", minId, maxId);
        } catch (Exception e) {
            log.error("【登录日志归档】归档失败：{}", e.getMessage());
        }
    }

    /**
     * 归档操作日志（定时任务，每天凌晨3点执行）
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void archiveOperLog() {
        if (!logProperties.getArchiveEnabled()) {
            return;
        }

        try {
            long archiveTimeThreshold = System.currentTimeMillis() - logProperties.getArchiveMonths() * 30L * 24 * 3600 * 1000;

            JSONObject idRange = sqlService.queryForJson(null,
                    "SELECT MIN(id) AS minId, MAX(id) AS maxId FROM log_oper WHERE create_time < " + archiveTimeThreshold, null);

            if (idRange == null || idRange.getLongValue("minId") == 0) {
                log.info("【操作日志归档】无满足条件的数据");
                return;
            }

            long minId = idRange.getLongValue("minId");
            long maxId = idRange.getLongValue("maxId");

            sqlService.execSqlForJson(null,
                    "INSERT INTO log_oper_archive SELECT * FROM log_oper WHERE id BETWEEN " + minId + " AND " + maxId, null);

            sqlService.execSqlForJson(null,
                    "DELETE FROM log_oper WHERE id BETWEEN " + minId + " AND " + maxId, null);

            log.info("【操作日志归档】归档完成，ID范围：{}-{}", minId, maxId);
        } catch (Exception e) {
            log.error("【操作日志归档】归档失败：{}", e.getMessage());
        }
    }

}