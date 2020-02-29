package ai.yue.library.base.exception;

import lombok.NoArgsConstructor;

/**
 * 非法访问异常
 * 
 * @author	ylyue
 * @since	2018年2月3日
 */
@NoArgsConstructor
public class AttackException extends RuntimeException {
	
	private static final long serialVersionUID = 8503754532487989211L;
	
	public AttackException(String message) {
		super(message);
	}
	
}
