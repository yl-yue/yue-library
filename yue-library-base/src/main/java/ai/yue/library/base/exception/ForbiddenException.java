package ai.yue.library.base.exception;

import lombok.NoArgsConstructor;

/**
 * 无权限异常
 * 
 * @author	孙金川
 * @since	2018年3月28日
 */
@NoArgsConstructor
public class ForbiddenException extends RuntimeException {

	private static final long serialVersionUID = -477721736529522496L;
	
	public ForbiddenException(String message) {
		super(message);
	}
	
}
