package ai.yue.library.web.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import ai.yue.library.base.config.properties.CorsProperties;
import ai.yue.library.web.config.argument.resolver.CustomArgumentResolversConfig;
import ai.yue.library.web.config.handler.ExceptionHandlerConfig;
import ai.yue.library.web.config.properties.WebProperties;
import ai.yue.library.web.env.WebMvcEnv;
import ai.yue.library.web.util.servlet.multipart.UploadProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * web bean 自动配置
 * 
 * @author	ylyue
 * @since	2018年11月26日
 */
@Slf4j
@Configuration
@Import({ WebMvcConfig.class, WebMvcRegistrationsConfig.class, ExceptionHandlerConfig.class,
		CustomArgumentResolversConfig.class, WebMvcEnv.class })
@EnableConfigurationProperties({ WebProperties.class, UploadProperties.class })
public class WebAutoConfig {
	
	// CorsConfig-跨域
	
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = "yue.cors", name = "allow", havingValue = "true", matchIfMissing = true)
	public CorsFilter corsFilter(CorsProperties corsProperties) {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		final CorsConfiguration config = new CorsConfiguration();
		
		config.setAllowCredentials(true);
		config.setAllowedHeaders(Arrays.asList("*"));
		config.setAllowedMethods(Arrays.asList("*"));
		config.setAllowedOrigins(Arrays.asList("*"));
		config.setMaxAge(3600L);
		
		// 设置response允许暴露的Headers
		List<String> exposedHeaders = corsProperties.getExposedHeaders();
		if (exposedHeaders != null) {
			config.setExposedHeaders(exposedHeaders);
		} else {
			config.addExposedHeader("token");
		}
		
		source.registerCorsConfiguration("/**", config);
		
		log.info("【初始化配置-跨域】默认配置为true，当前环境为true：默认任何情况下都允许跨域访问 ... 已初始化完毕。");
		return new CorsFilter(source);
	}
	
}
