package ai.yue.library.template.simple.ipo.user;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import ai.yue.library.base.validation.annotation.Birthday;
import ai.yue.library.base.validation.annotation.Cellphone;
import ai.yue.library.base.validation.annotation.Chinese;
import lombok.Data;

/**
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
	
	@Chinese
	Character sex;// 用户性别
	
	@Birthday
	LocalDate birthday;// 用户生日
	
}
