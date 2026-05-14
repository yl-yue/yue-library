package ai.yue.library.data.log.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 操作日志实体
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
@TableName("log_oper")
public class OperLogEntity extends LogBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 操作标题
     */
    private String title;

    /**
     * 操作描述
     */
    private String description;

    /**
     * 业务分类
     */
    private String bizType;

    /**
     * 操作类型：C=新增，R=查询，U=更新，D=删除
     */
    private String operType;

    /**
     * 操作账号
     */
    private String username;

    /**
     * 操作IP
     */
    private String ip;

    /**
     * 操作时间
     */
    private Long operTime;

    /**
     * 请求接口URL
     */
    private String requestUrl;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 请求参数（脱敏后JSON）
     */
    private String requestParam;

    /**
     * 响应结果（JSON）
     */
    private String responseResult;

    /**
     * 操作状态：0=失败，1=成功
     */
    private Integer status;

}
