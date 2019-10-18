package ai.yue.library.base.exception;

import lombok.NoArgsConstructor;

/**
 * 服务降级异常
 * 
 * @author	ylyue
 * @since	2018年2月3日
 */
@NoArgsConstructor
public class ClientFallbackException extends RuntimeException{
	
	private static final long serialVersionUID = -3620957053991110208L;

	public ClientFallbackException(String message) {
		super(message);
	}
	
}
