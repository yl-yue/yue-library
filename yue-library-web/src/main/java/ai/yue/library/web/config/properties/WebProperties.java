package ai.yue.library.web.config.properties;

import ai.yue.library.web.config.argument.resolver.RepeatedlyReadServletRequestFilter;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Web自动配置属性
 * 
 * @author	ylyue
 * @since	2020年4月6日
 */
@Data
@ConfigurationProperties(WebProperties.PREFIX)
public class WebProperties {
	
	/**
	 * Prefix of {@link WebProperties}.
	 */
	public static final String PREFIX = "yue.web";
	
	/**
	 * 启用输入流可反复读取的HttpServletRequest
	 * <p>默认：true
	 */
	private boolean enableRepeatedlyReadServletRequest = true;
	
	/**
	 * {@link RepeatedlyReadServletRequestFilter} 优先级
	 * <p>值越小优先级越高
	 * <p>默认：-999，比常规过滤器更高的优先级，防止输入流被更早读取
	 */
	private int repeatedlyReadServletRequestFilterOrder = -999;

	/**
	 * 启用接口幂等拦截
	 * <p>默认：true
	 */
	private boolean enableApiIdempotent = true;

	/**
	 * 启用接口QPS拦截
	 * <p>默认：true
	 */
	private boolean enableApiQpsLimit = true;

	/**
	 * 启用全局接口QPS拦截
	 * <p>默认：false
	 */
	private boolean enableGlobalApiQpsLimit = false;

	/**
	 * 全局接口最大QPS值
	 * <p>默认：3000
	 */
	private int globalApiQps = 3000;

}
