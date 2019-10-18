package ai.yue.library.base.exception;

import lombok.NoArgsConstructor;

/**
 * 登录异常
 * 
 * @author	ylyue
 * @since	2018年2月3日
 */
@NoArgsConstructor
public class LoginException extends RuntimeException {

	private static final long serialVersionUID = -4747910085674257587L;

	public LoginException(String message) {
		super(message);
	}

}
