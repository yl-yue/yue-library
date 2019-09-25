package ai.yue.library.base.constant;

/**
 * Token 常量
 * 
 * @deprecated 见 yue-library-data-redis 模块 ConstantProperties
 * @author	ylyue
 * @since	2017年10月8日
 */
@Deprecated
public interface TokenConstant {

	/**
	 * Cookie Token Key
	 */
	public static final String COOKIE_TOKEN_KEY = "token";

	/**
	 * Redis Token 前缀
	 */
	public static final String REDIS_TOKEN_PREFIX = "token_%s";

	/**
	 * IP前缀
	 */
	public static final String IP_PREFIX = "ip_%s";
    
}
