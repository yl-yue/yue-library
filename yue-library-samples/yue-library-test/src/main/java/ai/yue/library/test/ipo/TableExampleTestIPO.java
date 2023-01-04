package ai.yue.library.test.ipo;

import ai.yue.library.base.ipo.ValidationGroups;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 表示例测试
 *
 * @author ylyue
 * @since 2021/1/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableExampleTestIPO {

    /**
     * 有序主键
     */
    @NotNull(groups = ValidationGroups.Update.class)
    private Long id;
    private String fieldOne;
    private String fieldTwo;
    private String fieldThree;
    private String tenantSys;
    private String tenantCo;

}
