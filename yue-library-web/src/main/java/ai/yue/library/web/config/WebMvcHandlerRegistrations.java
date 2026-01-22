package ai.yue.library.web.config;

import ai.yue.library.base.config.properties.BaseProperties;
import ai.yue.library.web.config.api.version.ApiVersionRequestMappingHandlerMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * WebMvcRegistrations
 *
 * @author	ylyue
 * @since	2020年2月27日
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebMvcHandlerRegistrations implements WebMvcRegistrations {

	private final BaseProperties baseProperties;
	
	@Override
	public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
		BaseProperties.ApiVersionProperties apiVersion = baseProperties.getApiVersion();
		if (!apiVersion.isEnabled()) {
			return WebMvcRegistrations.super.getRequestMappingHandlerMapping();
		}
		
		log.info("【初始化配置-ApiVersionRequestMappingHandlerMapping】默认配置为true，当前环境为true：RESTful API接口版本控制，执行初始化 ...");
		return new ApiVersionRequestMappingHandlerMapping(apiVersion);
	}
    
}
