package ai.yue.library.base.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import ai.yue.library.base.config.factory.HttpsRequestFactory;
import ai.yue.library.base.config.properties.ConstantProperties;
import ai.yue.library.base.config.properties.CorsProperties;
import ai.yue.library.base.config.properties.RestProperties;
import ai.yue.library.base.config.thread.pool.AsyncConfig;
import ai.yue.library.base.validation.Validator;
import lombok.extern.slf4j.Slf4j;

/**
 * @author	孙金川
 * @version 创建时间：2018年11月26日
 */
@Slf4j
@Configuration
@Import({ AsyncConfig.class })
@EnableConfigurationProperties({ConstantProperties.class, RestProperties.class, CorsProperties.class})
public class BeanAutoConfig {
	
	// restTemplate-HTTPS客户端
	
    @Bean
    @ConditionalOnMissingBean
    public ClientHttpRequestFactory httpsRequestFactory(RestProperties restProperties){
    	HttpsRequestFactory factory = new HttpsRequestFactory();
    	
    	// 设置链接超时时间
    	Integer connectTimeout = restProperties.getConnectTimeout();
		if (connectTimeout != null) {
			factory.setConnectTimeout(connectTimeout);
		}
		
    	// 设置读取超时时间
    	Integer readTimeout = restProperties.getReadTimeout();
    	if (readTimeout != null) {
    		factory.setReadTimeout(readTimeout);
    	}
    	
        return factory;
    }
    
	@Bean
	@ConditionalOnMissingBean
    public Validator validator(){
		log.info("【初始化配置-校验器】正在初始化Bean：Validator ...");
        return new Validator();
    }
	
	@Bean
	@ConditionalOnMissingBean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory){
		log.info("【初始化配置-HTTPS客户端】正在初始化Bean：RestTemplate ...");
        return new RestTemplate(factory);
    }
	
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
		
		log.info("【初始化配置-跨域】正在初始化Bean：CorsFilter ...");
		return new CorsFilter(source);
	}
	
}
