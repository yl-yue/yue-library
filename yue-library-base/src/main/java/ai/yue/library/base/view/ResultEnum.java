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
	SUCCESS(200, "ai.yue.library.base.view.ResultEnum.SUCCESS.msg"),
	LOGGED_IN(210, "ai.yue.library.base.view.ResultEnum.LOGGED_IN.msg"),
	
    // 300 - 资源、重定向、定位等提示
	
	RESOURCE_ALREADY_INVALID(300, "ai.yue.library.base.view.ResultEnum.RESOURCE_ALREADY_INVALID.msg"),
	
	/**
	 * {@link HttpStatus#MOVED_PERMANENTLY}
	 * <p>{@code 301 Moved Permanently}.
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.4.2">HTTP/1.1: Semantics and Content, section 6.4.2</a>
	 */
	MOVED_PERMANENTLY(301, "ai.yue.library.base.view.ResultEnum.MOVED_PERMANENTLY.msg"),
	
	FILE_EMPTY(310, "ai.yue.library.base.view.ResultEnum.FILE_EMPTY.msg"),
    
    // 400 - 客户端错误
	
	/**
	 * {@link HttpStatus#BAD_REQUEST}
	 * <p>{@code 400 Bad Request}.
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.1">HTTP/1.1: Semantics and Content, section 6.5.1</a>
	 */
	BAD_REQUEST(400, "ai.yue.library.base.view.ResultEnum.BAD_REQUEST.msg"),
	UNAUTHORIZED(401, "ai.yue.library.base.view.ResultEnum.UNAUTHORIZED.msg"),
	ATTACK(402, "ai.yue.library.base.view.ResultEnum.ATTACK.msg"),
	FORBIDDEN(403, "ai.yue.library.base.view.ResultEnum.FORBIDDEN.msg"),
	
	/**
	 * {@link HttpStatus#NOT_FOUND}
	 * <p>{@code 404 Not Found}.
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.4">HTTP/1.1: Semantics and Content, section 6.5.4</a>
	 */
	NOT_FOUND(404, "ai.yue.library.base.view.ResultEnum.NOT_FOUND.msg"),
	
	/**
	 * {@link HttpStatus#METHOD_NOT_ALLOWED}
	 * <p>{@code 405 Method Not Allowed}.
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.5">HTTP/1.1: Semantics and Content, section 6.5.5</a>
	 */
	METHOD_NOT_ALLOWED(405, "ai.yue.library.base.view.ResultEnum.METHOD_NOT_ALLOWED.msg"),
	
	/**
	 * {@link HttpStatus#GONE}
	 * <p>{@code 410 Gone}.
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.9">
	 *     HTTP/1.1: Semantics and Content, section 6.5.9</a>
	 */
	GONE(410, "ai.yue.library.base.view.ResultEnum.GONE.msg"),
	UNSUPPORTED_MEDIA_TYPE(415, "ai.yue.library.base.view.ResultEnum.UNSUPPORTED_MEDIA_TYPE.msg"),
	
	/**
	 * {@link HttpStatus#TOO_MANY_REQUESTS}
	 * <p>{@code 429 Too Many Requests}.
	 * @see <a href="https://tools.ietf.org/html/rfc6585#section-4">Additional HTTP Status Codes</a>
	 */
	TOO_MANY_REQUESTS(429, "ai.yue.library.base.view.ResultEnum.TOO_MANY_REQUESTS.msg"),
	PARAM_VOID(432, "ai.yue.library.base.view.ResultEnum.PARAM_VOID.msg"),
	PARAM_CHECK_NOT_PASS(433, "ai.yue.library.base.view.ResultEnum.PARAM_CHECK_NOT_PASS.msg"),
	PARAM_VALUE_INVALID(434, "ai.yue.library.base.view.ResultEnum.PARAM_VALUE_INVALID.msg"),
	PARAM_DECRYPT_ERROR(435, "ai.yue.library.base.view.ResultEnum.PARAM_DECRYPT_ERROR.msg"),
	API_IDEMPOTENT(436, "ai.yue.library.base.view.ResultEnum.API_IDEMPOTENT.msg"),

    // 500 - 服务器错误
	
	/**
	 * {@link HttpStatus#INTERNAL_SERVER_ERROR}
	 * <p>{@code 500 Internal Server Error}.
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.6.1">HTTP/1.1: Semantics and Content, section 6.6.1</a>
	 */
	INTERNAL_SERVER_ERROR(500, "ai.yue.library.base.view.ResultEnum.INTERNAL_SERVER_ERROR.msg"),
	
	REQUEST_ERROR(501, "ai.yue.library.base.view.ResultEnum.REQUEST_ERROR.msg"),
	
	BAD_GATEWAY(502, "ai.yue.library.base.view.ResultEnum.BAD_GATEWAY.msg"),
	
	/**
	 * {@link HttpStatus#SERVICE_UNAVAILABLE}
	 * <p>{@code 503 Service Unavailable}.
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.6.4">HTTP/1.1: Semantics and Content, section 6.6.4</a>
	 */
	SERVICE_UNAVAILABLE(503, "ai.yue.library.base.view.ResultEnum.SERVICE_UNAVAILABLE.msg"),
	
	/**
	 * {@link HttpStatus#GATEWAY_TIMEOUT}
	 * <p>{@code 504 Gateway Timeout}.
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.6.5">HTTP/1.1: Semantics and Content, section 6.6.5</a>
	 */
	GATEWAY_TIMEOUT(504, "ai.yue.library.base.view.ResultEnum.GATEWAY_TIMEOUT.msg"),
	DATA_STRUCTURE(505, "ai.yue.library.base.view.ResultEnum.DATA_STRUCTURE.msg"),
	DB_ERROR(506, "ai.yue.library.base.view.ResultEnum.DB_ERROR.msg"),
    CLIENT_FALLBACK(507, "ai.yue.library.base.view.ResultEnum.CLIENT_FALLBACK.msg"),
    CLIENT_FALLBACK_ERROR(508, "ai.yue.library.base.view.ResultEnum.CLIENT_FALLBACK_ERROR.msg"),
    TYPE_CONVERT_ERROR(509, "ai.yue.library.base.view.ResultEnum.TYPE_CONVERT_ERROR.msg"),
    
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
