package ai.yue.library.web.grpc.config;

import ai.yue.library.web.grpc.config.exception.ResultExceptionHandler;
import ai.yue.library.web.grpc.config.properties.WebProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * web bean 自动配置
 * 
 * @author	ylyue
 * @since	2018年11月26日
 */
@Slf4j
@Configuration
@Import({ResultExceptionHandler.class})
@EnableConfigurationProperties({WebProperties.class})
public class WebGrpcAutoConfig {
	
	@Autowired
	WebProperties webProperties;
	
	// CorsConfig-跨域
	
//	@Bean
//	@ConditionalOnMissingBean
//	@ConditionalOnProperty(prefix = "yue.cors", name = "allow", havingValue = "true", matchIfMissing = true)
//	public CorsFilter corsFilter(CorsProperties corsProperties) {
//		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		final CorsConfiguration config = new CorsConfiguration();
//
//		config.setAllowCredentials(true);
//		config.setAllowedHeaders(Arrays.asList("*"));
//		config.setAllowedMethods(Arrays.asList("*"));
//		config.setAllowedOriginPatterns(Arrays.asList("*"));
//		config.setMaxAge(3600L);
//
//		// 设置response允许暴露的Headers
//		List<String> exposedHeaders = corsProperties.getExposedHeaders();
//		if (exposedHeaders != null) {
//			config.setExposedHeaders(exposedHeaders);
//		} else {
//			config.addExposedHeader("token");
//		}
//
//		source.registerCorsConfiguration("/**", config);
//
//		log.info("【初始化配置-跨域】默认配置为true，当前环境为true：默认任何情况下都允许跨域访问 ... 已初始化完毕。");
//		return new CorsFilter(source);
//	}

}
