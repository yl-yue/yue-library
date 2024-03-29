package ai.yue.library.test.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;

/**
 * 表示例测试
 *
 * @author	ylyue
 * @since	2019年9月25日
 */
@Data
@TableName(value = "table_example_eliminate", autoResultMap = true)
public class TableExampleEliminate {

	private static final long serialVersionUID = 6404495051119680239L;

	/** 用户ID */
	@TableId
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
