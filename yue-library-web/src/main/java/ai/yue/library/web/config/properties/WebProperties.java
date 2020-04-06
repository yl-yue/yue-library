package ai.yue.library.web.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author	ylyue
 * @since	2020年4月6日
 */
@Data
@ConfigurationProperties("yue.web")
public class WebProperties {
	
	private boolean enabledFastJsonHttpMessageConverter = false;
	
}
