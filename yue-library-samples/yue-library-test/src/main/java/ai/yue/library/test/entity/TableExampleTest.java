package ai.yue.library.test.entity;

import ai.yue.library.data.mybatis.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class TableExampleTest extends BaseEntity {

	private static final long serialVersionUID = 6404495051119680239L;

	String fieldOne;
	String fieldTwo;
	String fieldThree;
	@TableField(fill = FieldFill.INSERT)
	String tenantSys;
	String tenantCo;

}
