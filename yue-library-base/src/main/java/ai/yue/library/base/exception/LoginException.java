package ai.yue.library.base.exception;

/**
 * 登录异常
 * 
 * @author	孙金川
 * @since	2018年2月3日
 */
public class LoginException extends RuntimeException {

	private static final long serialVersionUID = -4747910085674257587L;

	public LoginException(String message) {
		super(message);
	}

}
