package ai.yue.library.template.boot.dataobject.user;

import java.time.LocalDate;

import ai.yue.library.data.jdbc.dataobject.BaseCamelCaseDO;
import ai.yue.library.template.boot.constant.user.RoleEnum;
import ai.yue.library.template.boot.constant.user.UserStatusEnum;
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
public class UserDO extends BaseCamelCaseDO {

	private static final long serialVersionUID = 908912816212643805L;
	
	Long userId;// 用户ID
	RoleEnum role;// 用户所属角色
	String cellphone;// 用户手机号码
	String password;// 密码
	String nickname;// 用户昵称
	String email;// 邮箱
	String headImg;// 用户头像
	Character sex;// 用户性别
	LocalDate birthday;// 用户生日
	UserStatusEnum userStatus;// 用户状态
	
}
