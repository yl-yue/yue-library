package ai.yue.library.base.config.api.version;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author	ylyue
 * @since	2020年2月27日
 */
@Configuration
@EnableConfigurationProperties(ApiVersionProperties.class)
@ConditionalOnProperty(prefix = "yue.api-version", name = "enabled", havingValue = "true", matchIfMissing = true)
public class WebMvcRegistrationsConfig implements WebMvcRegistrations {

	@Autowired
	private ApiVersionProperties apiVersionProperties;
	
	@Override
	public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
		return new ApiVersionRequestMappingHandlerMapping(apiVersionProperties);
	}
    
}
