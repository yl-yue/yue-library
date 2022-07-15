package ai.yue.library.web.grpc.config.properties;

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
	public static final String PREFIX = "yue.web-grpc";

}
