package ai.yue.library.base.config;

import java.util.Arrays;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import ai.yue.library.base.config.factory.HttpsRequestFactory;
import ai.yue.library.base.config.properties.ConstantProperties;
import ai.yue.library.base.config.properties.RestProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * @author	孙金川
 * @version 创建时间：2018年11月26日
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({ConstantProperties.class, RestProperties.class})
public class BeanAutoConfig {
	
	// restTemplate-HTTPS客户端
	
    @Bean
    @ConditionalOnMissingBean
    public ClientHttpRequestFactory httpsRequestFactory(RestProperties restProperties){
    	HttpsRequestFactory factory = new HttpsRequestFactory();
        factory.setReadTimeout(restProperties.getReadTimeout());
        factory.setConnectTimeout(restProperties.getConnectTimeout());
        return factory;
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
	public CorsFilter corsFilter() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		final CorsConfiguration config = new CorsConfiguration();
		
		config.setAllowCredentials(true);
		config.setAllowedHeaders(Arrays.asList("*"));
		config.setAllowedMethods(Arrays.asList("*"));
		config.setAllowedOrigins(Arrays.asList("*"));
		config.setMaxAge(300L);
		
		source.registerCorsConfiguration("/**", config);
		
		log.info("【初始化配置-跨域】正在初始化Bean：CorsFilter ...");
		return new CorsFilter(source);
	}
	
}
