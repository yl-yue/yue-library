package ai.yue.library.test.ipo;

import ai.yue.library.base.validation.annotation.Cellphone;
import ai.yue.library.base.validation.annotation.Chinese;
import ai.yue.library.base.validation.annotation.Exclusion;
import ai.yue.library.base.validation.annotation.Mutual;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.time.LocalDate;

/**
 * 校验
 *
 * @author  ylyue
 * @version 创建时间：2018年9月25日
 */
@Data
@Mutual({"birthday", "idcard", "email"})
@Exclusion({"age", "cellphone", "qq"})
public class ValidationMutualIPO {

    @Chinese
    @NotEmpty(message = "姓名不能为空")
    @Length(max = 20, message = "姓名不能超过20个字")
    private String name;
    
    private LocalDate birthday;
    
    private String idcard;
    
    @Email
    @Length(max = 50)
    private String email;

    @Max(30)
    @Min(12)
    private Integer age;

    @Cellphone(notNull = false)
    private String cellphone;
    
    @Pattern(regexp = "[1-9]([0-9]{5,11})")
    private String qq;
    
}
