package ai.yue.library.test.dataobject.jdbc;

import ai.yue.library.data.jdbc.dataobject.BaseCamelCaseDO;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 表示例测试
 *
 * @author	ylyue
 * @since	2019年9月25日
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class TableExampleTestDO extends BaseCamelCaseDO {

	private static final long serialVersionUID = 6404495051119680239L;

	String fieldOne;
	String fieldTwo;
	String fieldThree;
	String tenantSys;
	String tenantCo;

}
