package ai.yue.library.test.dataobject.jdbc;

import ai.yue.library.data.jdbc.dataobject.BaseCamelCaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Table;

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
@Table("data_encrypt_2")
public class DataEncryptDO2 extends BaseCamelCaseDO {

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
