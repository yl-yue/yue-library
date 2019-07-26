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
	
	// 100 - 访问限制
	ATTACK(100, "非法访问"),
	UNAUTHORIZED(101, "未登录或登录已失效"),
	LOGGED_IN(102, "会话未注销，无需登录"),
	FORBIDDEN(103, "无权限"),
	FREQUENT_ACCESS_RESTRICTION(104, "频繁访问限制，请稍后重试"),
	
	// 200 - 正确结果
	SUCCESS(200, "成功"),
	
    // 300 - 资源、重定向、定位等提示
	RESOURCE_ALREADY_INVALID(300, "资源已失效"),
	FILE_EMPTY(301, "文件上传请求错误，获得文件信息为空，求先使用测试工具（如：postman）校验，同时文件必须有明确的匹配类型（如文本类型：.txt）"),
	TYPE_CONVERT_ERROR(302, "类型转换错误"),
    
    // 400 - 客户端错误
    PARAM_VOID(400, "参数为空"),
    PARAM_CHECK_NOT_PASS(401, "参数校验未通过，请参照API核对后重试"),
    PARAM_VALUE_INVALID(402, "参数校验未通过，无效的value"),
    PARAM_DECRYPT_ERROR(403, "参数解密错误"),
    
    // 500 - 服务器错误
	ERROR(500, "请求错误"),
	DATA_STRUCTURE(501, "数据结构异常"),
	DB_ERROR(502, "数据结构异常，请检查相应数据结构一致性"),
    CLIENT_FALLBACK(503, "哎哟喂！网络开小差了，请稍后重试..."),
    CLIENT_FALLBACK_ERROR(504, "哎哟喂！服务都被您挤爆了..."),
    
	// 600 - 自定义错误提示
	DEV_DEFINED(600, "自定义类型提示，请覆盖。");
	
	private Integer code;
	private String msg;
	
}
