package ai.yue.library.test.ipo;

import ai.yue.library.data.mybatis.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Tolerate;

import java.util.List;

/**
 * 参数解析IPO
 *
 * @author ylyue
 * @since 2021/1/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ParamParseIPO extends BaseEntity {

    List<String> stringList;

}
