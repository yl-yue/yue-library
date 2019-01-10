package ai.yue.library.base.exception;

/**
 * 参数校验不通过异常
 * @author  孙金川
 * @version 创建时间：2017年10月9日
 */
@SuppressWarnings("serial")
public class ParamException extends RuntimeException {
	
	public ParamException(String msg) {
        super(msg);
    }
	
}
