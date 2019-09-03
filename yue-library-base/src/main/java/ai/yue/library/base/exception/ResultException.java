package ai.yue.library.base.exception;

import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.ResultInfo;
import lombok.Getter;

/**
 * @author	孙金川
 * @since	2018年2月3日
 */
@Getter
public class ResultException extends RuntimeException{
	
	private static final long serialVersionUID = -4332073495864145387L;
	
	private Result<?> result;
	
	public <T> ResultException(String msg) {
		this.result = ResultInfo.dev_defined(msg);
	}
	
	public <T> ResultException(Result<T> result) {
		this.result = result;
	}
	
}
