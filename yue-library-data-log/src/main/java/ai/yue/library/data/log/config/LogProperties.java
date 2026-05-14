package ai.yue.library.data.log.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 日志配置属性
 *
 * @author ylyue
 * @since 2025/5/13
 */
@Data
@ConfigurationProperties(LogProperties.PREFIX)
public class LogProperties {

    public static final String PREFIX = "yue.data.log";

    /**
     * 全局开关
     */
    private Boolean enabled = true;

    /**
     * 是否异步记录
     */
    private Boolean async = true;

    /**
     * 是否启用脱敏
     */
    private Boolean maskEnabled = true;

    /**
     * 是否自动建表
     */
    private Boolean autoCreateTable = true;

    /**
     * 是否启用归档
     */
    private Boolean archiveEnabled = true;

    /**
     * 归档时间阈值（月）
     */
    private Integer archiveMonths = 3;

    /**
     * 归档数量阈值
     */
    private Integer archiveCount = 1000000;

    /**
     * 请求参数最大长度（字节）
     */
    private Integer maxParamSize = 10240;

    /**
     * 响应结果最大长度（字节）
     */
    private Integer maxResponseSize = 51200;
    
    /** HTTP 转发配置 */
    private Http http = new Http();
    
    /** 持久化接口配置 */
    private StorageApi storageApi = new StorageApi();
    
    /** 查询接口配置 */
    private QueryApi queryApi = new QueryApi();
    
    @Data
    public static class Http {
        /** 是否启用 HTTP 转发模式 */
        private Boolean enabled = false;
        /** 日志服务地址（如 http://log-service:8080） */
        private String baseUrl;
    }
    
    @Data
    public static class StorageApi {
        /** 是否启用持久化接口（日志服务端开启） */
        private Boolean enabled = false;
    }
    
    @Data
    public static class QueryApi {
        /** 是否启用查询接口（业务管理后台需显式开启） */
        private Boolean enabled = false;
    }
    
}
