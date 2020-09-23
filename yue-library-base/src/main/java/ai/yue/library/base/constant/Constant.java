package ai.yue.library.base.constant;

/**
 * yue-library 定义的标识常量
 * 
 * @author	ylyue
 * @since	2020年7月25日
 */
public interface Constant {

	/** yue-library前缀 */
	String PREFIX = "yue-library-";
	/**
	 * body参数传递
	 * @deprecated ai.yue.library.web.config.argument.resolver.RepeatedlyReadServletRequestFilter 自动传递可反复读取的HttpServletRequest
	 */
	@Deprecated
	String BODY_PARAM_TRANSMIT = PREFIX + "Body-Param-Transmit";
	
}
