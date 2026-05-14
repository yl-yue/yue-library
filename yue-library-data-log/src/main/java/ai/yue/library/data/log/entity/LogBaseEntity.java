package ai.yue.library.data.log.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 日志基类
 * <p>不继承 BaseEntity，仅保留日志场景必需字段</p>
 *
 * @author ylyue
 * @since 2025/5/13
 */
@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class LogBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 有序主键
     */
    @TableId
    protected Long id;

    /**
     * 创建人：用户名、昵称、人名
     */
    @TableField(fill = FieldFill.INSERT)
    protected String createUser;

    /**
     * 创建人：用户id
     */
    @TableField(fill = FieldFill.INSERT)
    protected Long createUserId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    protected Long createTime;

    /**
     * 系统租户：一级租户
     */
    @TableField(fill = FieldFill.INSERT)
    protected String tenantSysId;

    /**
     * 企业租户：二级租户
     */
    @TableField(fill = FieldFill.INSERT)
    protected String tenantCoId;

}
