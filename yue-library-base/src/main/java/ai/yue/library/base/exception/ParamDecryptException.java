package ai.yue.library.base.exception;

import lombok.NoArgsConstructor;

/**
 * 解密异常
 * 
 * @author	ylyue
 * @since	2018年2月3日
 */
@NoArgsConstructor
public class ParamDecryptException extends RuntimeException{
	
	private static final long serialVersionUID = 5325379409661261173L;

	public ParamDecryptException(String message) {
		super(message);
	}
	
}
