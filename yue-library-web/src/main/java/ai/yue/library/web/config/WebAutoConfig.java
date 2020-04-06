package ai.yue.library.web.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ai.yue.library.base.util.ApplicationContextUtils;
import ai.yue.library.web.config.argument.resolver.CustomArgumentResolversConfig;
import ai.yue.library.web.config.handler.ExceptionHandlerConfig;
import ai.yue.library.web.config.properties.WebProperties;
import ai.yue.library.web.util.servlet.multipart.UploadProperties;

/**
 * web bean 自动配置
 * 
 * @author	ylyue
 * @since	2018年11月26日
 */
@Configuration
@Import({ WebMvcConfig.class, WebMvcRegistrationsConfig.class, ExceptionHandlerConfig.class,
		CustomArgumentResolversConfig.class, ApplicationContextUtils.class, })
@EnableConfigurationProperties({ WebProperties.class, UploadProperties.class })
public class WebAutoConfig {
	
}
