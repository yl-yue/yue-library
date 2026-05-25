package ai.yue.library.data.log.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Plumelog 日志聚合配置属性
 * <p>为 yue-logback-spring.xml 中引用的 plumelog 属性提供 IDE 配置提示</p>
 *
 * @author ylyue
 * @since 2025/5/25
 */
@Data
@ConfigurationProperties(PlumelogProperties.PREFIX)
public class PlumelogProperties {

    public static final String PREFIX = "plumelog";

    /**
     * Redis 主机地址
     */
    private String redisHost;

    /**
     * Redis 端口
     */
    private Integer redisPort;

    /**
     * Redis 认证密码
     */
    private String redisAuth;

    /**
     * Redis 数据库编号
     */
    private Integer redisDb;

    /**
     * 运行环境标识（如 dev/test/prod）
     */
    private String env;

}
