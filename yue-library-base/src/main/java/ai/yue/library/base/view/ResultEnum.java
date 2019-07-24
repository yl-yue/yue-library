package ai.yue.library.base.view;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author	孙金川
 * @since	2017年10月8日
 */
@Getter
@AllArgsConstructor
public enum ResultEnum {
	
	/* 正确结果 */
    // 200
    SUCCESS(200, "成功"),
    
    /* 错误提示 */
	// 100
	ATTACK(100, "非法访问"),
	UNAUTHORIZED(101, "未登录或登录已失效"),
	LOGGED_IN(102, "会话未注销，无需登录"),
	FORBIDDEN(103, "无权限"),
	FREQUENT_ACCESS_RESTRICTION(104, "频繁访问限制，请稍后重试"),
    // 300
    DECRYPT_ERROR(300, "参数解密错误"),
    JSON_PARSE_ERROR(301, "JSON格式字符串解析错误"),
    FILE_EMPTY(302, "文件上传请求错误，获得文件信息为空，求先使用测试工具（如：postman）校验，同时文件必须有明确的匹配类型（如文本类型：.txt）"),
    // 400
    PARAM_VOID(400, "参数为空"),
    PARAM_CHECK_NOT_PASS(401, "参数校验未通过，请参照API核对后重试"),
    PARAM_VALUE_INVALID(402, "参数校验未通过，无效的value"),
    // 500
	ERROR(500, "请求错误"),
	DATA_STRUCTURE(501, "数据结构异常"),
	DB_ERROR(502, "数据结构异常，请检查相应数据结构一致性"),
    CLIENT_FALLBACK(503, "哎哟喂！网络开小差了，请稍后重试..."),
    CLIENT_FALLBACK_ERROR(504, "哎哟喂！服务都被您挤爆了..."),

    /* 默认自定义提示信息 */
	//600
	DEV_DEFINED(600, "自定义类型提示，请覆盖。（并将提示信息定义成枚举类型）"),

	//610
    USER_EXIST(610, "用户已存在"),
    USER_NO_EXIST(611, "用户不存在"),
    USER_STOP(612, "用户已停用"),
    USER_INVALID(613, "无效的用户"),
    USERNAME_OR_PASSWORD_ERROR(614, "用户名或密码错误"),
    ORIGINAL_PASSWORD_ERROR(615, "原密码错误"),
    PHONE_EXIST(616, "手机号已被注册"),
    
    //620
    PASSWORD_INVALID(620, "无效的口令"),
    CAPTCHA_ERROR(621, "验证码错误"),
    CELLPHONE_ERROR(622, "手机号码错误"),
    ID_CARD_NUMBER_ERROR(623, "身份证号码错误"),
    EMAIL_ERROR(624, "邮箱验证错误"),
    INVITE_CODE_INVALID(625, "邀请码无效"),
    
    //630
    ACCOUNT_EXIST_BINDING(630, "此帐户已被绑定"),
    ACCOUNT_EXIST_BUSINESS_NOT_ALLOW_CHANGE(631, "帐户存在业务，暂时不允许改变状态"),
    ACCOUNT_BALANCE_NOT_ENOUGH(632, "帐户余额不足");
	
	private Integer code;
	private String msg;
	
}
