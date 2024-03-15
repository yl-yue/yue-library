package ai.yue.library.test.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@TableName(value = "user", autoResultMap = true)
public class User implements Serializable {

	private static final long serialVersionUID = 6404495051119680239L;

	/** 用户ID */
	@Id
	Long id;
	/** 用户所属角色 */
	String role;
	/** 用户手机号码 */
	String cellphone;
	/** 密码 */
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
	String userStatus;
	/** 是否临时用户 */
	Boolean isTempUser;

}
