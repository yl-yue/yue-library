package ai.yue.library.data.log.constant;

/**
 * 日志持久化模式枚举
 *
 * @author ylyue
 * @since 2025/5/24
 */
public enum LogModeEnum {

    /**
     * HTTP 转发模式（默认）—— 微服务架构下转发日志至日志服务
     */
    HTTP,

    /**
     * 直连写库模式 —— 单体架构或日志服务本身，自动建表
     */
    DIRECT

}
