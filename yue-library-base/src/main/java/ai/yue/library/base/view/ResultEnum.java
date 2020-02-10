package ai.yue.library.base.view;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Result默认枚举值定义
 * <p>参考{@linkplain HttpStatus}
 * 
 * @author	ylyue
 * @since	2017年10月8日
 */
@Getter
@AllArgsConstructor
public enum ResultEnum {
	
	// 200 - 正确结果
	SUCCESS(200, "成功"),
	LOGGED_IN(210, "会话未注销，无需登录"),
	
    // 300 - 资源、重定向、定位等提示
	RESOURCE_ALREADY_INVALID(300, "资源已失效"),
	FILE_EMPTY(310, "文件上传请求错误，获得文件信息为空，求先使用测试工具（如：postman）校验，同时文件必须有明确的匹配类型（如文本类型：.txt）"),
    
    // 400 - 客户端错误
	BAD_REQUEST(400, "错误的请求，参数校验未通过，请参照API核对后重试"),
	UNAUTHORIZED(401, "未登录或登录已失效（Unauthorized）"),
	ATTACK(402, "非法访问"),
	FORBIDDEN(403, "无权限（Forbidden）"),
	NOT_FOUND(404, "Not Found"),
	METHOD_NOT_ALLOWED(405, "方法不允许（Method Not Allowed）"),
	UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
	TOO_MANY_REQUESTS(429, "频繁请求限流，请稍后重试（Too Many Requests）"),
	PARAM_VOID(432, "参数为空"),
	PARAM_CHECK_NOT_PASS(433, "参数校验未通过，请参照API核对后重试"),
	PARAM_VALUE_INVALID(434, "参数校验未通过，无效的value"),
	PARAM_DECRYPT_ERROR(435, "参数解密错误"),
	
    // 500 - 服务器错误
	INTERNAL_SERVER_ERROR(500, "服务器内部错误（Internal Server Error）"),
	ERROR(501, "请求错误"),
	BAD_GATEWAY(502, "Bad Gateway"),
	/**
	 * {@code 503 Service Unavailable}.
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.6.4">HTTP/1.1: Semantics and Content, section 6.6.4</a>
	 */
	SERVICE_UNAVAILABLE(503, "Service Unavailable"),
	/**
	 * {@code 504 Gateway Timeout}.
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.6.5">HTTP/1.1: Semantics and Content, section 6.6.5</a>
	 */
	GATEWAY_TIMEOUT(504, "Gateway Timeout"),
	DATA_STRUCTURE(505, "数据结构异常"),
	DB_ERROR(506, "数据结构异常，请检查相应数据结构一致性"),
    CLIENT_FALLBACK(507, "哎哟喂！网络开小差了，请稍后重试..."),
    CLIENT_FALLBACK_ERROR(508, "哎哟喂！服务都被您挤爆了..."),
    TYPE_CONVERT_ERROR(509, "类型转换错误"),
    
	// 600 - 自定义错误提示
    /**
     * 开发者自定义类型提示，请覆盖： {@linkplain #msg}
     */
	DEV_DEFINED(600, "开发者自定义类型提示，请覆盖。");
	
	private Integer code;
	private String msg;
	
}
