package ai.yue.library.test.remove.redis.dataobject.performance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(indexName = "user")
public class UserPerformanceDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 表自增id
    */
//    private Long id;

    /**
    * 用户key，唯一，32位uuid
    */
    @Id
    private String key;

    /**
    * 人员id
    */
    private Long personId;

    /**
    * 手机号（冗余字段仅作展示用途）：登录逻辑请查询 user_login_account
    */
    private String cellphone;

    /**
    * 邮箱（冗余字段仅作展示用途）：登录逻辑请查询 user_login_account
    */
    private String email;

    /**
    * 昵称
    */
    private String nickname;

    /**
    * 头像
    */
    private String headImg;

    /**
    * 是否临时用户
    */
    private boolean tempUser;

    /**
    * 临时用户有效开始时间
    */
    private Date tempUserValidStartTime;

    /**
    * 临时用户有效结束时间
    */
    private Date tempUserValidEndTime;

    /**
    * 用户状态（not_enabled：未启用，normal：正常，disabled：禁用）
    */
    private String userStatus;

    /**
    * 用户禁用时间
    */
    private Date disableTime;

    /**
    * 扩展信息
    */
    private String extendField;

    /**
    * 密码最后修改时间
    */
    private Date passwordUpdateTime;

    /**
    * 是否删除
    */
    private boolean isDeleted;

    /**
    * 数据删除时间戳：默认为0，未删除
    */
    private Long deleteTime;

    /**
    * 数据插入时间
    */
    private Date createTime;

    /**
    * 数据更新时间
    */
    private Date updateTime;

    /**
    * 排序码
    */
    private Integer sortIdx;

}