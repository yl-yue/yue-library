package ai.yue.library.base.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

/**
 * Result HTTP 状态码枚举
 * <p>参考{@linkplain HttpStatus}
 * 
 * @author	ylyue
 * @since	2017年10月8日
 */
@Getter
@AllArgsConstructor
public enum ResultEnum {
	
	// 200 - 正确结果
	
	/**
	 * {@link HttpStatus#OK}
	 * <p>{@code 200 OK}.
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.3.1">HTTP/1.1: Semantics and Content, section 6.3.1</a>
	 */
	SUCCESS(200, "成功"),
	LOGGED_IN(210, "会话未注销，无需登录"),
	
    // 300 - 资源、重定向、定位等提示
	
	RESOURCE_ALREADY_INVALID(300, "资源已失效"),
	
	/**
	 * {@link HttpStatus#MOVED_PERMANENTLY}
	 * <p>{@code 301 Moved Permanently}.
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.4.2">HTTP/1.1: Semantics and Content, section 6.4.2</a>
	 */
	MOVED_PERMANENTLY(301, "Moved Permanently"),
	
	FILE_EMPTY(310, "文件上传请求错误，获得文件信息为空，求先使用测试工具（如：postman）校验，同时文件必须有明确的匹配类型（如文本类型：.txt）"),
    
    // 400 - 客户端错误
	
	/**
	 * {@link HttpStatus#BAD_REQUEST}
	 * <p>{@code 400 Bad Request}.
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.1">HTTP/1.1: Semantics and Content, section 6.5.1</a>
	 */
	BAD_REQUEST(400, "错误的请求，参数校验未通过，请参照API核对后重试"),
	UNAUTHORIZED(401, "未登录或登录已失效（Unauthorized）"),
	ATTACK(402, "非法访问"),
	FORBIDDEN(403, "无权限（Forbidden）"),
	
	/**
	 * {@link HttpStatus#NOT_FOUND}
	 * <p>{@code 404 Not Found}.
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.4">HTTP/1.1: Semantics and Content, section 6.5.4</a>
	 */
	NOT_FOUND(404, "Not Found"),
	
	/**
	 * {@link HttpStatus#METHOD_NOT_ALLOWED}
	 * <p>{@code 405 Method Not Allowed}.
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.5">HTTP/1.1: Semantics and Content, section 6.5.5</a>
	 */
	METHOD_NOT_ALLOWED(405, "方法不允许（Method Not Allowed），客户端使用服务端不支持的 Http Request Method 进行接口调用。"),
	
	/**
	 * {@link HttpStatus#GONE}
	 * <p>{@code 410 Gone}.
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.9">
	 *     HTTP/1.1: Semantics and Content, section 6.5.9</a>
	 */
	GONE(410, "当前API接口版本已弃用，请客户端更新接口调用方式"),
	UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
	
	/**
	 * {@link HttpStatus#TOO_MANY_REQUESTS}
	 * <p>{@code 429 Too Many Requests}.
	 * @see <a href="https://tools.ietf.org/html/rfc6585#section-4">Additional HTTP Status Codes</a>
	 */
	TOO_MANY_REQUESTS(429, "频繁请求限流，请稍后重试（Too Many Requests）"),
	PARAM_VOID(432, "参数为空"),
	PARAM_CHECK_NOT_PASS(433, "参数校验未通过，请参照API核对后重试"),
	PARAM_VALUE_INVALID(434, "参数校验未通过，无效的value"),
	PARAM_DECRYPT_ERROR(435, "参数解密错误"),
	
    // 500 - 服务器错误
	
	/**
	 * {@link HttpStatus#INTERNAL_SERVER_ERROR}
	 * <p>{@code 500 Internal Server Error}.
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.6.1">HTTP/1.1: Semantics and Content, section 6.6.1</a>
	 */
	INTERNAL_SERVER_ERROR(500, "服务器内部错误（Internal Server Error）"),
	
	REQUEST_ERROR(501, "请求错误"),
	
	BAD_GATEWAY(502, "Bad Gateway"),
	
	/**
	 * {@link HttpStatus#SERVICE_UNAVAILABLE}
	 * <p>{@code 503 Service Unavailable}.
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.6.4">HTTP/1.1: Semantics and Content, section 6.6.4</a>
	 */
	SERVICE_UNAVAILABLE(503, "服务不可用（Service Unavailable）"),
	
	/**
	 * {@link HttpStatus#GATEWAY_TIMEOUT}
	 * <p>{@code 504 Gateway Timeout}.
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.6.5">HTTP/1.1: Semantics and Content, section 6.6.5</a>
	 */
	GATEWAY_TIMEOUT(504, "Gateway Timeout"),
	DATA_STRUCTURE(505, "数据结构异常"),
	DB_ERROR(506, "数据结构异常，请检查相应数据结构一致性"),
    CLIENT_FALLBACK(507, "网络开小差了，请稍后重试..."),
    CLIENT_FALLBACK_ERROR(508, "当前阶段服务压力过大，请稍后重试..."),
    TYPE_CONVERT_ERROR(509, "类型转换错误"),
    
	// 600 - 自定义错误提示
    
    /**
     * 错误提示，请使用具体的错误提示信息覆盖此 {@link #msg}
     */
    ERROR_PROMPT(600, "错误提示，请使用具体的错误提示信息覆盖此msg");
    
	private Integer code;
	private String msg;
	
	/**
	 * 如果可能，将给定的状态代码解析为 {@code ResultEnum}
	 * 
	 * @param code HTTP状态码(可能是非标准的)
	 * @return 对应的 {@code ResultEnum}，如果没有找到，则为 {@code null}
	 */
	@Nullable
	public static ResultEnum valueOf(int code) {
		for (ResultEnum value : values()) {
			if (value.code == code) {
				return value;
			}
		}
		
		return null;
	}
	
}
