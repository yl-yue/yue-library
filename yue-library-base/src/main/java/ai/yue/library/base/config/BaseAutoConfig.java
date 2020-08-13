package ai.yue.library.base.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

import ai.yue.library.base.annotation.api.version.ApiVersionProperties;
import ai.yue.library.base.config.datetime.DateTimeFormatConfig;
import ai.yue.library.base.config.handler.ExceptionHandlerProperties;
import ai.yue.library.base.config.http.RestProperties;
import ai.yue.library.base.config.http.SkipSslVerificationHttpRequestFactory;
import ai.yue.library.base.config.properties.CorsProperties;
import ai.yue.library.base.config.thread.pool.AsyncConfig;
import ai.yue.library.base.util.ApplicationContextUtils;
import ai.yue.library.base.util.SpringUtils;
import ai.yue.library.base.validation.Validator;
import lombok.extern.slf4j.Slf4j;

/**
 * base bean 自动配置
 * 
 * @author	ylyue
 * @since	2018年11月26日
 */
@Slf4j
@Configuration
@Import({ AsyncConfig.class, ApplicationContextUtils.class, SpringUtils.class, DateTimeFormatConfig.class })
@EnableConfigurationProperties({ ApiVersionProperties.class, ExceptionHandlerProperties.class, RestProperties.class,
		CorsProperties.class, })
public class BaseAutoConfig {
	
	// RestTemplate-HTTPS客户端
	
	@Bean
	@ConditionalOnMissingBean
    public RestTemplate restTemplate(RestProperties restProperties){
		SkipSslVerificationHttpRequestFactory factory = new SkipSslVerificationHttpRequestFactory();
    	
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
    	
		log.info("【初始化配置-HTTPS客户端】Bean：RestTemplate ... 已初始化完毕。");
        return new RestTemplate(factory);
    }
	
	// Validator-校验器
	
	@Bean
	@ConditionalOnMissingBean
    public Validator validator(){
		log.info("【初始化配置-校验器】Bean：Validator ... 已初始化完毕。");
        return new Validator();
    }
	
}
