package ai.yue.library.test.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 表示例测试
 *
 * @author	ylyue
 * @since	2019年9月25日
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "table_id_alias", autoResultMap = true)
public class TableIdAlias {

	private static final long serialVersionUID = 6404495051119680239L;

	@TableId
	protected Long userId;
	String fieldOne;
	String fieldTwo;
	String fieldThree;
	@TableField(fill = FieldFill.INSERT)
	String tenantCo;
	@TableField(fill = FieldFill.INSERT)
	String tenantSys;

}
