package ai.yue.library.data.jdbc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据填充属性定义
 *
 * @author ylyue
 * @since  2022/4/1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataFillDTO {

    String fillFieldName;
    Object fillFieldValue;

}
