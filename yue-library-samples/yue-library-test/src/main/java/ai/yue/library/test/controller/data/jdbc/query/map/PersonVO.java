package ai.yue.library.test.controller.data.jdbc.query.map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 人员dto
 */
@Data
public class PersonVO implements Serializable {

    private static final long serialVersionUID = 3987648902475498726L;

    /**
     * 主键id，单表时自增
     */
    private Long id;

    /**
     * uuid5标准，36位有符号uuid：c8206ecd-2d86-5671-8bff-6d7c77b96b2b
     */
    private String key;

    /**
     * 身份证号码：身份证；勿动、勿修改、勿排序
     */
    private String idCard;

    /**
     * 姓名：身份证；勿动、勿修改、勿排序
     */
    private String name;

    /**
     * 性别：身份证；勿动、勿修改、勿排序
     */
    private String sex;

    /**
     * 民族：身份证；勿动、勿修改、勿排序
     */
    private String nation;

    /**
     * 籍贯：身份证；勿动、勿修改、勿排序
     */
    private String nativePlace;

    /**
     * 生日：身份证；勿动、勿修改、勿排序
     */
    private LocalDate birthday;

    /**
     * 户籍地址：身份证；勿动、勿修改、勿排序
     */
    private String address;

    /**
     * 名称拼音：优先身份证计算；勿动、勿修改、勿排序
     */
    private String namePinyin;

    /**
     * 是否已实名认证(1-已实名  0-未实名)
     */
    private Integer isCertified;

    /**
     * 年龄：优先身份证计算；勿动、勿修改、勿排序
     */
    private int age;

    /**
     * 工作电话
     */
    private String workPhone;

    /**
     * 单位
     */
    private String unit;

    /**
     * 参加工作时间
     */
    private LocalDate workTime;

    /**
     * 居住地址
     */
    private String residentialAddress;

    /**
     * 住宅小区名称
     */
    private String residenceCommunityName;

    /**
     * 住宅小区代码
     */
    private String residenceCommunityCode;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String cellphone;

    /**
     * 经度
     */
    private String lng;

    /**
     * 纬度
     */
    private String lat;

    /**
     * 其它联系方式
     */
    private String contact;

    /**
     * 人员照片集,json数组
     */
    private JSONArray personPhotos;

    /**
     * 婚姻
     */
    private String marriage;

    /**
     * 毕业学校
     */
    private String school;

    /**
     * 专业
     */
    private String speciality;

    /**
     * 学历
     */
    private String education;

    /**
     * 是否激活为登录用户
     */
    private boolean isActUser;

    /**
     * 激活为登录用户时间
     */
    private LocalDateTime actUserTime;

    /**
     * 人员状态
     */
    private String personStatus;

    /**
     * 扩展信息
     */
    private JSONObject extendField;

}
