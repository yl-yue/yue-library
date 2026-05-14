package ai.yue.library.data.log.ipo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 操作日志分页查询参数
 *
 * @author ylyue
 * @since 2025/5/14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperLogPageIPO {

    /**
     * 操作标题（模糊匹配）
     */
    private String title;

    /**
     * 业务分类
     */
    private String bizType;

    /**
     * 操作类型：C=新增，R=查询，U=更新，D=删除
     */
    private String operType;

    /**
     * 操作账号（模糊匹配）
     */
    private String username;

    /**
     * 操作状态：0=失败，1=成功
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

}
