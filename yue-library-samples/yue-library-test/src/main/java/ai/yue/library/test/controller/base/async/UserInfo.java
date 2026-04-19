package ai.yue.library.test.controller.base.async;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private String name;
    private Integer age;
    private String sex;
    private String address;
    private String phone;
    private String email;
    private String remark;
}
