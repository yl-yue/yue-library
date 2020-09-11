package ai.yue.library.web.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import ai.yue.library.web.config.argument.resolver.RepeatedlyReadServletRequestFilter;
import lombok.Data;

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
	private boolean enabledRepeatedlyReadServletRequest = true;
	
	/**
	 * {@link RepeatedlyReadServletRequestFilter} 优先级
	 * <p>值越小优先级越高
	 * <p>默认：-999，比常规过滤器更高的优先级，防止输入流被更早读取
	 */
	private int repeatedlyReadServletRequestFilterOrder = -999;
	
}
