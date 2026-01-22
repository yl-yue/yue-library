package ai.yue.library.data.mybatis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页请求参数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageIPO {

    /**
     * 当前页
     */
    private Integer pageNum;
    /**
     * 每页显示条数
     */
    private Integer pageSize;
    /**
     * 排序字段
     */
    private String orderBy;

}
