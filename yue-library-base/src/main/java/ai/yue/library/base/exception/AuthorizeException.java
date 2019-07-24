package ai.yue.library.base.exception;

/**
 * Admin登录异常
 * 
 * @author	孙金川
 * @since	2018年2月3日
 */
public class AuthorizeException extends RuntimeException{
	
	private static final long serialVersionUID = -4374582170487392015L;

	public AuthorizeException(String message) {
		super(message);
	}
	
}
