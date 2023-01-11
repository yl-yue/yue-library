package ai.yue.library.template.boot.ipo;

import ai.yue.library.base.validation.annotation.Birthday;
import ai.yue.library.base.validation.annotation.Cellphone;
import ai.yue.library.template.boot.constant.user.SexEnum;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 用户CRUD实体入参校验示例
 *
 * @author	ylyue
 * @since	2019年9月25日
 */
@Data
public class UserIPO implements Serializable {

	private static final long serialVersionUID = -3892585526805663196L;

	@NotBlank
	@Cellphone
	String cellphone;// 用户手机号码
	
	@NotBlank
	@Length(min = 8, max = 20, message = "密码长度范围：8 - 20")
	String password;// 密码
	
	String nickname;// 用户昵称

	@NotBlank
	@Email
	String email;// 邮箱
	
	String head_img;// 用户头像
	
	SexEnum sex;// 用户性别
	
	@Birthday
	LocalDate birthday;// 用户生日
	
}
