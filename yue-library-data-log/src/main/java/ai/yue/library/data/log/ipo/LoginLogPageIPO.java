package ai.yue.library.data.log.ipo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录日志分页查询参数
 *
 * @author ylyue
 * @since 2025/5/14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginLogPageIPO {

    /**
     * 登录账号（模糊匹配）
     */
    private String username;

    /**
     * 登录状态：0=失败，1=成功
     */
    private Integer status;

    /**
     * 开始时间
     */
    private Long startTime;

    /**
     * 结束时间
     */
    private Long endTime;

    /**
     * 仅查当前用户自己的日志（框架自动通过LogUserProvider获取当前登录账号，精确匹配username）
     */
    private Boolean onlyMine;

}
