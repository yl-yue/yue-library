package ai.yue.library.base.exception;

/**
 * 解密异常
 * 
 * @author	孙金川
 * @since	2018年2月3日
 */
public class DecryptException extends RuntimeException{
	
	private static final long serialVersionUID = 5325379409661261173L;

	public DecryptException(String message) {
		super(message);
	}
	
}
