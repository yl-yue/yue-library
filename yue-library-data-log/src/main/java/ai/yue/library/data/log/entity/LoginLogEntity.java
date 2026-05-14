package ai.yue.library.data.log.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 登录日志实体
 *
 * @author ylyue
 * @since 2025/5/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@TableName("log_login")
public class LoginLogEntity extends LogBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 登录账号
     */
    private String username;

    /**
     * 登录IP
     */
    private String ip;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 登录状态：0=失败，1=成功
     */
    private Integer status;

    /**
     * 登录消息
     */
    private String msg;

    /**
     * 请求参数（脱敏后JSON）
     */
    private String requestParam;

}
