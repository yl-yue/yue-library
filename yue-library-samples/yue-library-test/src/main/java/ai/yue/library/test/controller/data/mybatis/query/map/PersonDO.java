//package ai.yue.library.test.controller.data.jdbc.query.map;
//
//import ai.yue.library.data.jdbc.dataobject.BaseCamelCaseDO;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import lombok.*;
//import lombok.experimental.SuperBuilder;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
///**
// * 人员DO
// *
// * @author ylyue
// * @since 2020年2月22日
// */
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@SuperBuilder(toBuilder = true)
//@ToString(callSuper = true)
//@EqualsAndHashCode(callSuper = true)
//public class PersonDO extends BaseCamelCaseDO {
//
//    private static final long serialVersionUID = -5880107144355168990L;
//
//    /**
//     * 是否删除
//     */
//    private Boolean isDeleted;
//    /**
//     * uuid5标准，36位有符号uuid：c8206ecd-2d86-5671-8bff-6d7c77b96b2b
//     */
//    private String key;
//
//    /**
//     * 身份证号码：身份证；勿动、勿修改、勿排序
//     */
//    private String idCard;
//
//    /**
//     * 姓名：身份证；勿动、勿修改、勿排序
//     */
//    private String name;
//
//    /**
//     * 性别：身份证；勿动、勿修改、勿排序
//     */
//    private String sex;
//
//    /**
//     * 民族：身份证；勿动、勿修改、勿排序
//     */
//    private String nation;
//
//    /**
//     * 籍贯：身份证；勿动、勿修改、勿排序
//     */
//    private String nativePlace;
//
//    /**
//     * 照片：身份证；勿动、勿修改、勿排序
//     */
//    private JSONObject photo;
//    /**
//     * 人员照片集,json数组
//     */
//    private JSONArray personPhotos;
//
//    /**
//     * 生日：身份证；勿动、勿修改、勿排序
//     */
//    private LocalDate birthday;
//
//    /**
//     * 户籍地址：身份证；勿动、勿修改、勿排序
//     */
//    private String address;
//
//    /**
//     * 名称拼音：优先身份证计算；勿动、勿修改、勿排序
//     */
//    private String namePinyin;
//
//    /**
//     * 是否已实名认证(1-已实名  0-未实名)
//     */
//    private Integer isCertified;
//
//
//    /**
//     * 年龄：优先身份证计算；勿动、勿修改、勿排序
//     */
//    private Integer age;
//
//    /**
//     * 工作电话
//     */
//    private String workPhone;
//
//    /**
//     * 单位
//     */
//    private String unit;
//
//    /**
//     * 参加工作时间
//     */
//    private LocalDate workTime;
//
//    /**
//     * 居住地址
//     */
//    private String residentialAddress;
//
//    /**
//     * 住宅小区名称
//     */
//    private String residenceCommunityName;
//
//    /**
//     * 住宅小区代码
//     */
//    private String residenceCommunityCode;
//
//    /**
//     * 邮箱
//     */
//    private String email;
//
//    /**
//     * 手机号
//     */
//    private String cellphone;
//
//    /**
//     * 经度
//     */
//    private String lng;
//
//    /**
//     * 纬度
//     */
//    private String lat;
//
//    /**
//     * 其它联系方式
//     */
//    private String contact;
//    /**
//     * 婚姻
//     */
//    private String marriage;
//
//    /**
//     * 毕业学校
//     */
//    private String school;
//
//    /**
//     * 专业
//     */
//    private String speciality;
//
//    /**
//     * 学历
//     */
//    private String education;
//
//    /**
//     * 是否激活为登录用户
//     */
////    @JSONField(name = "is_act_user")
////    @FieldName("is_act_user")
//    private boolean actUser;
//
//    /**
//     * 激活为登录用户时间
//     */
//    private LocalDateTime actUserTime;
//
//    /**
//     * 人员状态
//     */
//    private PersonStatusEnum personStatus;
//
//    /**
//     * 扩展信息
//     */
//    private JSONObject extendField;
//
//    /**
//     * 是否权控数据
//     */
////    @FieldName("is_permission_control_data")
//    private boolean permissionControlData;
//
//    /**
//     * 是否需要审核数据
//     */
////    @FieldName("is_check_data")
//    private boolean checkData;
//
//    /**
//     * 数据来源描述
//     */
//    private String dataSourceDescription;
//
//    /**
//     * 数据质量完整度
//     */
//    private Integer dataQualityIntegrity;
//
//    /**
//     * 数据质量有效性
//     */
//    private Integer dataQualityValidity;
//
//    /**
//     * 数据质量有效性告警字段名称
//     */
//    private String dataQualityValidityAlarm;
//    /**
//     * 性别key
//     */
//    private String sexKey;
//    /**
//     * 性别name
//     */
//    private String sexName;
//
//    /**
//     * 民族key
//     */
//    private String nationKey;
//
//    /**
//     * 籍贯key
//     */
//    private String nativePlaceKey;
//
//    /**
//     * 婚姻key
//     */
//    private String marriageKey;
//
//    /**
//     * 学历
//     */
//    private String educationKey;
//    /**
//     * 证件类型name
//     */
//    private String cardTypeName;
//    /**
//     * 证件类型key
//     */
//    private String cardTypeKey;
//    /**
//     * 证件号码
//     */
//    private String cardCode;
//    /**
//     * 曾用名
//     */
//    private String beforeName;
//    /**
//     * 宗教信仰name
//     */
//    private String zongjiaoName;
//    /**
//     * 宗教信仰key
//     */
//    private String zongjiaoKey;
//    /**
//     * 工作岗位name
//     */
//    private String workName;
//    /**
//     * 工作岗位key
//     */
//    private String workKey;
//    /**
//     * 户籍性质name
//     */
//    private String hujiName;
//    /**
//     * 户籍性质key
//     */
//    private String hujiKey;
//    /**
//     * 血型name
//     */
//    private String bloodName;
//    /**
//     * 血型key
//     */
//    private String bloodKey;
//    /**
//     * 身高（cm）
//     */
//    private Integer height;
//    /**
//     * 是否农民工
//     */
//    private Boolean isFarmer;
//    /**
//     * 是否劳务派遣
//     */
//    private Boolean isLaowu;
//    /**
//     * 是否服役
//     */
//    private Boolean isFuyi;
//
//}
