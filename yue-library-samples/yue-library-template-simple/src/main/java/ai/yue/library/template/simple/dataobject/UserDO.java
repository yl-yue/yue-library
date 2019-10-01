package ai.yue.library.template.simple.dataobject;

import java.time.LocalDate;

import ai.yue.library.data.jdbc.dataobject.DBDO;
import ai.yue.library.template.simple.constant.RoleEnum;
import ai.yue.library.template.simple.constant.UserStatusEnum;
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
public class UserDO extends DBDO {

	Long user_id;
	RoleEnum role;
	String cellphone;
	String wx_unionid;
	String qq_openid;
	String email;
	String nickname;
	String head_img;
	Character sex;
	LocalDate birthday;
	Integer age;
	UserStatusEnum user_status;
	String register_source;
	
}
