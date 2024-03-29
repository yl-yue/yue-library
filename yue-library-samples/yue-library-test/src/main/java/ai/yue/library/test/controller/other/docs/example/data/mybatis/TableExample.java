package ai.yue.library.test.controller.other.docs.example.data.mybatis;

import ai.yue.library.data.mybatis.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Entity示例
 *
 * @author	ylyue
 * @since	2019年9月25日
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class TableExample extends BaseEntity {

	private static final long serialVersionUID = 6404495051119680239L;

	@TableField(fill = FieldFill.INSERT)
	String tenantSys;
	String tenantCo;
	String fieldOne;
	String fieldTwo;
	String fieldThree;

}
