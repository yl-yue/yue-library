package ai.yue.library.test.spring.jdbc.ipo;

import ai.yue.library.base.ipo.ValidationGroups;
import ai.yue.library.base.validation.annotation.Birthday;
import ai.yue.library.base.validation.annotation.Cellphone;
import ai.yue.library.base.validation.annotation.Chinese;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;

/**
 * 用户分组测试IPO
 *
 * @author	ylyue
 * @since	2019年9月25日
 */
@Data
public class UserGroupIPO extends ValidationGroups {

	@Null(groups = {Create.class})
	@NotNull(groups = {Delete.class, Update.class, Read.class})
	private Long id;

	/** 手机号码 */
	@Cellphone(groups = {Create.class})
	@Cellphone(notNull = false, groups = {Delete.class, Update.class, Read.class})
	String cellphone;

	@NotBlank(groups = {Create.class})
	@Length(min = 8, max = 20, message = "密码长度范围：8 - 20")
	String password;

	String nickname;// 用户昵称

	@NotBlank(groups = {Create.class})
	@Email
	String email;// 邮箱

	String head_img;// 用户头像

	@Chinese(groups = {Create.class})
	@Chinese(notNull = false, groups = {Delete.class, Update.class, Read.class})
	Character sex;// 用户性别

	@Birthday(groups = {Create.class})
	@Birthday(notNull = false, groups = {Delete.class, Update.class, Read.class})
	LocalDate birthday;// 用户生日

}
