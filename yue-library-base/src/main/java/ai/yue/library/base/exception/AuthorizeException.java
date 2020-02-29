package ai.yue.library.base.exception;

import lombok.NoArgsConstructor;

/**
 * Admin登录异常
 * 
 * @author	ylyue
 * @since	2018年2月3日
 */
@NoArgsConstructor
public class AuthorizeException extends RuntimeException {
	
	private static final long serialVersionUID = -4374582170487392015L;

	public AuthorizeException(String message) {
		super(message);
	}
	
}
