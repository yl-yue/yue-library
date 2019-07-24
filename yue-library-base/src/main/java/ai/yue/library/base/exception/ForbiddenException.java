package ai.yue.library.base.exception;

/**
 * 无权限异常
 * 
 * @author	孙金川
 * @since	2018年3月28日
 */
@SuppressWarnings("serial")
public class ForbiddenException extends RuntimeException {

	public ForbiddenException(String message) {
		super(message);
	}
}
