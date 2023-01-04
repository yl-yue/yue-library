package ai.yue.library.test.ipo;

import ai.yue.library.data.mybatis.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 参数解析IPO
 *
 * @author ylyue
 * @since 2021/1/13
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ParamParseIPO extends BaseEntity {

    List<String> stringList;

}
