package ai.yue.library.test.dataobject.jdbc;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 人员
 * 
 * @author ylyue
 * @since 2020年10月13日
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasePersonDO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id，单表时自增
	 */
	private Long id;

	/**
	 * 数据插入时间
	 */
	private Date createTime;

	/**
	 * 数据更新时间
	 */
	private Date updateTime;

	/**
	 * 是否删除（将废弃）
	 */
	private boolean deleted;

	/**
	 * 排序
	 */
	private Integer sortIdx;

	/**
	 * 数据删除时间戳：默认为0，未删除
	 */
	private Long deleteTime;

	/**
	 * uuid5标准，36位有符号uuid：c8206ecd-2d86-5671-8bff-6d7c77b96b2b
	 */
	private String key;

	/**
	 * 身份证号码：身份证；勿动、勿修改、勿排序
	 */
	private String idCard;

	/**
	 * 身份证签发时间
	 */
	private Date cardStartTime;

	/**
	 * 身份证签发的到期时间
	 */
	private Date cardEndTime;

	/**
	 * 姓名：身份证；勿动、勿修改、勿排序
	 */
	private String name;

	/**
	 * 性别（man：男，woman：女，unknown：未知）：身份证；勿动、勿修改、勿排序
	 */
	private String sex;

	/**
	 * 性别key
	 */
	private String sexKey;

	/**
	 * 性别name
	 */
	private String sexName;

	/**
	 * 民族：身份证；勿动、勿修改、勿排序
	 */
	private String nation;

	/**
	 * 民族key
	 */
	private String nationKey;

	/**
	 * 籍贯：身份证；勿动、勿修改、勿排序
	 */
	private String nativePlace;

	/**
	 * 籍贯key
	 */
	private String nativePlaceKey;

	/**
	 * 是否实名认证(1-已实名 0-未实名)
	 */
	private boolean isCertified;

	/**
	 * 照片：身份证；勿动、勿修改、勿排序
	 */
	private String photo;

	/**
	 * 人员照片集（逗号分割）
	 */
	private JSONArray personPhotos;

	/**
	 * 生日：身份证；勿动、勿修改、勿排序
	 */
	private Date birthday;

	/**
	 * 户籍地址：身份证；勿动、勿修改、勿排序
	 */
	private String address;

	/**
	 * 名称拼音：优先身份证计算；勿动、勿修改、勿排序
	 */
	private String namePinyin;

	/**
	 * 年龄：优先身份证计算；勿动、勿修改、勿排序
	 */
	private Integer age;

	/**
	 * 工作电话
	 */
	private String workPhone;

	/**
	 * 单位code
	 */
	private String unitCode;

	/**
	 * 单位name
	 */
	private String unitName;

	/**
	 * 单位key
	 */
	private String unitKey;

	/**
	 * 参加工作时间
	 */
	private Date workTime;

	/**
	 * 居住地址
	 */
	private String residentialAddress;

	/**
	 * 居住地经度
	 */
	private String lng;

	/**
	 * 居住地纬度
	 */
	private String lat;

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
	 * 其它联系方式
	 */
	private String contact;

	/**
	 * 婚姻
	 */
	private String marriage;

	/**
	 * 婚姻key
	 */
	private String marriageKey;

	/**
	 * 毕业学校
	 */
	private String school;

	/**
	 * 专业
	 */
	private String speciality;

	/**
	 * 最高学历
	 */
	private String education;

	/**
	 * 学历
	 */
	private String educationKey;

	/**
	 * 是否激活为登录用户
	 */
	private boolean isActUser;

	/**
	 * 激活为登录用户时间
	 */
	private Date actUserTime;

	/**
	 * 人员状态（normal：正常，miss：失踪，died：死亡）
	 */
	private String personStatus;

	/**
	 * 扩展信息
	 */
	private JSONObject extendField;

	/**
	 * 是否权控数据（只能由直属上级修改该数据）
	 */
	private boolean isPermissionControlData;

	/**
	 * 是否需要审核数据（如果该数据需要修改需要由管理员审核后才生效）
	 */
	private boolean isCheckData;

	/**
	 * 数据来源描述
	 */
	private String dataSourceDescription;

	/**
	 * 数据质量完整度
	 */
	private Integer dataQualityIntegrity;

	/**
	 * 数据质量有效性
	 */
	private Integer dataQualityValidity;

	/**
	 * 数据质量有效性告警字段名称
	 */
	private String dataQualityValidityAlarm;

	/**
	 * 证件类型name
	 */
	private String cardTypeName;

	/**
	 * 证件类型key
	 */
	private String cardTypeKey;

	/**
	 * 证件号码
	 */
	private String cardCode;

	/**
	 * 曾用名
	 */
	private String beforeName;

	/**
	 * 宗教信仰name
	 */
	private String zongjiaoName;

	/**
	 * 宗教信仰key
	 */
	private String zongjiaoKey;

	/**
	 * 工作岗位name
	 */
	private String workName;

	/**
	 * 工作岗位key
	 */
	private String workKey;

	/**
	 * 户籍性质name
	 */
	private String hujiName;

	/**
	 * 户籍性质key
	 */
	private String hujiKey;

	/**
	 * 血型name
	 */
	private String bloodName;

	/**
	 * 血型key
	 */
	private String bloodKey;

	/**
	 * 身高（cm）
	 */
	private Integer height;

	/**
	 * 是否农民工
	 */
	private boolean farmer;

	/**
	 * 是否劳务派遣
	 */
	private boolean laowu;

	/**
	 * 是否服役
	 */
	private boolean fuyi;

//	public void setExtendField(JSONObject extendField) {
//		this.extendField = extendField;
//	}

//	public void setExtendField(String extendField) {
//		this.extendField = Convert.toJSONObject(extendField);
//	}
	
//	public void setExtendField(Object extendField) {
//		System.out.println(extendField);
//		this.extendField = Convert.toJSONObject(extendField);
//	}
	
}
