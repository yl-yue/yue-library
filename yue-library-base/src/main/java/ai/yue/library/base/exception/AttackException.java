package ai.yue.library.base.exception;

/**
 * 非法访问异常
 * 
 * @author	孙金川
 * @since	2018年2月3日
 */
public class AttackException extends RuntimeException{
	
	private static final long serialVersionUID = 8503754532487989211L;

	public AttackException(String message) {
		super(message);
	}
	
}
