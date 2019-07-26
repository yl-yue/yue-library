package ai.yue.library.base.exception;

import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.ResultInfo;
import lombok.Getter;

/**
 * @author	孙金川
 * @since	2018年2月3日
 */
@Getter
@SuppressWarnings("serial")
public class ResultException extends RuntimeException{
	
	Result<?> result;
	
	public <T> ResultException(String msg) {
		this.result = ResultInfo.dev_defined(msg);
	}
	
	public <T> ResultException(Result<T> result) {
		this.result = result;
	}
	
}
