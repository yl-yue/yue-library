package ai.yue.library.webflux.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxRegistrations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

import ai.yue.library.base.annotation.api.version.ApiVersionProperties;
import ai.yue.library.webflux.config.api.version.ApiVersionRequestMappingHandlerMapping;
import lombok.extern.slf4j.Slf4j;

/**
 * @author	ylyue
 * @since	2020年2月27日
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(ApiVersionProperties.class)
public class WebFluxRegistrationsConfig implements WebFluxRegistrations {

	@Autowired
	private ApiVersionProperties apiVersionProperties;
	
	@Override
	public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
		if (!apiVersionProperties.isEnabled()) {
			return WebFluxRegistrations.super.getRequestMappingHandlerMapping();
		}
		
		log.info("【初始化配置-ApiVersionRequestMappingHandlerMapping】默认配置为true，当前环境为true：Restful API接口版本控制，执行初始化 ...");
		return new ApiVersionRequestMappingHandlerMapping(apiVersionProperties);
	}
    
}
