package ai.yue.library.auth.client.config;

import ai.yue.library.auth.client.client.User;
import ai.yue.library.auth.client.config.properties.AuthProperties;
import ai.yue.library.data.redis.client.Redis;
import ai.yue.library.data.redis.config.RedisAutoConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * AuthClient自动配置
 * 
 * @author	ylyue
 * @since	2018年6月11日
 */
@Slf4j
@Configuration
@AutoConfigureAfter(RedisAutoConfig.class)
@EnableConfigurationProperties({ AuthProperties.class })
public class AuthClientAutoConfig {
	
	@Bean
	@ConditionalOnBean(Redis.class)
	public User user() {
		log.info("【初始化配置-AuthClient-User客户端】配置项：" + AuthProperties.PREFIX + "。Bean：User ... 已初始化完毕。");
		return new User();
	}

	@Bean
	public SecurityFilterChain endpointRequestSecurityFilterChain(HttpSecurity http) throws Exception {
		http.requestMatcher(EndpointRequest.toAnyEndpoint()).authorizeRequests((requests) ->
				requests.anyRequest().hasRole("ENDPOINT_ADMIN"));
		http.httpBasic();
		return http.build();
	}

}
