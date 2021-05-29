package ai.yue.library.test.remove.redis.aspect;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author	ylyue
 * @version 创建时间：2018年12月3日
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	/**
	 * 注册自定义拦截器，添加拦截路径和排除拦截路径
	 * <p>
	 * addPathPatterns("/**") 表示拦截所有的请求。<br>
	 * excludePathPatterns("/login", "/register") 表示除了登陆与注册之外，因为登陆注册不需要登陆也可以访问。<br>
	 * 这里可以添加多个拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new HttpRequestInterceptor());
		WebMvcConfigurer.super.addInterceptors(registry);
	}
	
}
