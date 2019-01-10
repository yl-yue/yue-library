package ai.yue.library.base.exception;

import ai.yue.library.base.view.Result;
import lombok.Getter;

/**
 * @author  孙金川
 * @version 创建时间：2018年2月3日
 */
@Getter
@SuppressWarnings("serial")
public class ResultException extends RuntimeException{
	
	Result<?> result;
	
	public <T> ResultException(Result<T> result) {
		this.result = result;
	}
	
}
