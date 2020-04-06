package ai.yue.library.webflux.config.api.version;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxRegistrations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

import ai.yue.library.base.annotation.api.version.ApiVersionProperties;

/**
 * @author	ylyue
 * @since	2020年2月27日
 */
@Configuration
@EnableConfigurationProperties(ApiVersionProperties.class)
@ConditionalOnProperty(prefix = "yue.api-version", name = "enabled", havingValue = "true", matchIfMissing = true)
public class WebFluxRegistrationsConfig implements WebFluxRegistrations {

	@Autowired
	private ApiVersionProperties apiVersionProperties;
	
	@Override
	public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
		return new ApiVersionRequestMappingHandlerMapping(apiVersionProperties);
	}
    
}
