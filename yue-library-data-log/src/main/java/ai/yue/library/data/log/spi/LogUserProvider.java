package ai.yue.library.data.log.spi;

/**
 * 用户信息提供者 SPI
 * <p>实现此接口，配置为Bean即可，用于获取当前登录账号</p>
 *
 * @author ylyue
 * @since 2025/5/13
 */
public interface LogUserProvider {

    /**
     * 当前登录账号（username/phone/email）
     */
    String getUsername();

}
