package ai.yue.library.base.exception;

import lombok.NoArgsConstructor;

/**
 * RESTful API接口版本弃用异常
 * 
 * @author	ylyue
 * @since	2018年2月3日
 */
@NoArgsConstructor
public class ApiDeprecatedException extends RuntimeException {
	
	private static final long serialVersionUID = -8929648099790728526L;

	public ApiDeprecatedException(String message) {
		super(message);
	}
	
}
