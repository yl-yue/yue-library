package ai.yue.library.base.exception;

/**
 * 服务降级异常
 * 
 * @author	孙金川
 * @since	2018年2月3日
 */
public class ClientFallbackException extends RuntimeException{
	
	private static final long serialVersionUID = -3620957053991110208L;

	public ClientFallbackException(String message) {
		super(message);
	}
	
}
