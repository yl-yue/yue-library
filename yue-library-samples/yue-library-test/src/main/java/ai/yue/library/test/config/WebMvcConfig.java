package ai.yue.library.test.config;

import ai.yue.library.test.aspect.HttpRequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * WebMvc配置
 *
 * @author	ylyue
 * @since	2018年12月3日
 */
@Configuration
public class WebMvcConfig {

	/**
	 * 请求日志打印拦截器
	 */
	@Bean
	public FilterRegistrationBean<HttpRequestFilter> registerHttpRequestFilter() {
		FilterRegistrationBean<HttpRequestFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setOrder(-998);
		filterRegistrationBean.setFilter(new HttpRequestFilter());
		return filterRegistrationBean;
	}

}
