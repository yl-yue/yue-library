package ai.yue.library.webflux.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ai.yue.library.webflux.env.WebFluxEnv;

/**
 * webflux bean 自动配置
 * 
 * @author	ylyue
 * @since	2018年11月26日
 */
@Configuration
@Import({ WebFluxRegistrationsConfig.class, WebFluxEnv.class })
public class WebFluxAutoConfig {
	
}
