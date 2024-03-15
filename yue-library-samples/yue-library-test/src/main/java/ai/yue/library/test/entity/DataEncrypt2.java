package ai.yue.library.test.entity;

import ai.yue.library.data.mybatis.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

/**
 * 数据脱敏
 *
 * @author	ylyue
 * @since	2019年9月25日
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("data_encrypt_2")
public class DataEncrypt2 extends BaseEntity {

	private static final long serialVersionUID = 6404495051129680239L;
	
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
