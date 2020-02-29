package ai.yue.library.base.exception;

import lombok.NoArgsConstructor;

/**
 * Restful API接口版本弃用异常
 * 
 * @author	ylyue
 * @since	2018年2月3日
 */
@NoArgsConstructor
public class ApiVersionDeprecatedException extends RuntimeException {
	
	private static final long serialVersionUID = -8929648099790728526L;

	public ApiVersionDeprecatedException(String message) {
		super(message);
	}
	
}
