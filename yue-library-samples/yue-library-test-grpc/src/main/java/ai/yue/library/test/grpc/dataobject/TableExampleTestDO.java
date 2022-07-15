package ai.yue.library.test.grpc.dataobject;

import ai.yue.library.data.jdbc.dataobject.BaseCamelCaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author	ylyue
 * @since	2019年9月25日
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TableExampleTestDO extends BaseCamelCaseDO {

	private static final long serialVersionUID = 6404495051119680239L;
	
	String field_one;
	String field_two;
	String field_three;

}
