package ai.yue.library.web.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import ai.yue.library.base.config.properties.CorsProperties;
import ai.yue.library.web.config.argument.resolver.CustomArgumentResolversConfig;
import ai.yue.library.web.config.argument.resolver.RepeatedlyReadServletRequestFilter;
import ai.yue.library.web.config.properties.FastJsonHttpMessageConverterProperties;
import ai.yue.library.web.config.properties.JacksonHttpMessageConverterProperties;
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
@Import({ WebMvcConfig.class, WebMvcRegistrationsConfig.class, CustomArgumentResolversConfig.class, WebMvcEnv.class })
@EnableConfigurationProperties({ WebProperties.class, JacksonHttpMessageConverterProperties.class,
		FastJsonHttpMessageConverterProperties.class, UploadProperties.class })
public class WebAutoConfig {
	
	@Autowired
	WebProperties webProperties;
	
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
	
	// 注册Filter并配置执行顺序
	
	/**
	 * 配置输入流可反复读取的HttpServletRequest
	 */
	@Bean
	@ConditionalOnProperty(prefix = "yue.web", name = "enabled-repeatedly-read-servlet-request", havingValue = "true", matchIfMissing = true)
	public FilterRegistrationBean<RepeatedlyReadServletRequestFilter> registerRepeatedlyReadRequestFilter() {
		FilterRegistrationBean<RepeatedlyReadServletRequestFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		// 设置比常规过滤器更高的优先级，防止输入流被更早读取
		filterRegistrationBean.setOrder(webProperties.getRepeatedlyReadServletRequestFilterOrder());
		filterRegistrationBean.setFilter(new RepeatedlyReadServletRequestFilter());
		log.info("【初始化配置-HttpServletRequest】默认配置为true，当前环境为true：默认启用输入流可反复读取的HttpServletRequest ... 已初始化完毕。");
		return filterRegistrationBean;
	}
	
}
