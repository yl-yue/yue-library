package ai.yue.library.base.exception;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import lombok.Getter;

/**
 * {@linkplain Result} 结果异常定义
 * 
 * @author	ylyue
 * @since	2018年2月3日
 */
@Getter
public class ResultException extends RuntimeException {
	
	private static final long serialVersionUID = -4332073495864145387L;
	
	private Result<?> result;
	
	public <T> ResultException(String msg) {
		super(msg);
		this.result = R.errorPrompt(msg);
	}
	
	public <T> ResultException(Result<T> result) {
		super(result.getMsg());
		this.result = result;
	}
	
}
