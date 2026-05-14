package ai.yue.library.data.log.spi;

/**
 * 脱敏策略 SPI 接口
 * <p>实现此接口，配置为Bean即可，用于自定义请求参数脱敏逻辑</p>
 *
 * @author ylyue
 * @since 2025/5/13
 */
public interface LogMaskStrategy {

    /**
     * 脱敏处理
     *
     * @param param 原始参数
     * @return 脱敏后参数
     */
    String mask(String param);

}
