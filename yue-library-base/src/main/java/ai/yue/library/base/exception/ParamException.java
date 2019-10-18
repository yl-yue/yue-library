package ai.yue.library.base.exception;

/**
 * 参数校验不通过异常
 * 
 * @author	ylyue
 * @since	2017年10月9日
 */
public class ParamException extends RuntimeException {
	
	private static final long serialVersionUID = -7818277682527873103L;
	
	public ParamException(String msg) {
        super(msg);
    }
	
}
