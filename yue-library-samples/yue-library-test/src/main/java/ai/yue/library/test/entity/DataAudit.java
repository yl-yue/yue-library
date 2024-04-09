package ai.yue.library.test.entity;

import ai.yue.library.data.mybatis.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 数据审计
 *
 * @author	ylyue
 * @since	2019年9月25日
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("data_audit")
public class DataAudit extends BaseEntity {

	private static final long serialVersionUID = 6404495051129680239L;

	/**
	 * 有序主键：单表时数据库自增、分布式时雪花自增
	 */
	@TableId
	protected Long id;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	protected Long createTime;
	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	protected Long updateTime;

	/** 用户手机号码 */
	String cellphone;
	/** 密码 */
	String password;
	/** 邮箱 */
	String email;
	/** 用户性别 */
	Character sex;
	/** 用户生日 */
	LocalDate birthday;

}
