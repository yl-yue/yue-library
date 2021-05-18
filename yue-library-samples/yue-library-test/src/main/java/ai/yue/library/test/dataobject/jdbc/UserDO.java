package ai.yue.library.test.dataobject.jdbc;

import ai.yue.library.data.jdbc.dataobject.BaseCamelCaseDO;
import ai.yue.library.test.constant.RoleEnum;
import ai.yue.library.test.constant.UserStatusEnum;
import cn.hutool.core.annotation.Alias;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

/**
 * @author	ylyue
 * @since	2019年9月25日
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserDO extends BaseCamelCaseDO {

	private static final long serialVersionUID = 6404495051119680239L;
	
	/** 用户ID */
	Long userId;
	/** 用户所属角色 */
	RoleEnum role;
	/** 用户手机号码 */
	String cellphone;
	/** 密码 */
//	@Alias(value = "pwd")
//	@JSONField(name = "pwd")
	String password;
	/** 用户昵称 */
	String nickname;
	/** 邮箱 */
	String email;
	/** 用户头像 */
	String headImg;
	/** 用户性别 */
	Character sex;
	/** 用户生日 */
	LocalDate birthday;
	/** 用户状态 */
	UserStatusEnum userStatus;
	/** 是否临时用户 */
	boolean tempUser;
	
}
