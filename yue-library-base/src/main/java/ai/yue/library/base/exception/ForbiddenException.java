package ai.yue.library.base.exception;

import lombok.NoArgsConstructor;

/**
 * 无权限异常
 * 
 * @author	孙金川
 * @since	2018年3月28日
 */
@NoArgsConstructor
@SuppressWarnings("serial")
public class ForbiddenException extends RuntimeException {

	public ForbiddenException(String message) {
		super(message);
	}
}
