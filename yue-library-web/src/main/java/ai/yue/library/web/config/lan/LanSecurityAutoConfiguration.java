package ai.yue.library.web.config.lan;

import cn.hutool.v7.core.data.id.IdUtil;
import cn.hutool.v7.core.text.StrUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = LanSecurityProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(LanSecurityProperties.class)
public class LanSecurityAutoConfiguration {

	@PostConstruct
	public void logConfiguration() {
		log.info("【初始化配置-/lan/安全拦截】默认配置为true，当前环境为true：/lan/ 路径统一安全拦截 ... 已初始化完毕。");
	}

	@Bean
	public FilterRegistrationBean<LanSecurityFilter> lanSecurityFilterRegistration(LanSecurityProperties properties) {
		resolveBasicAuthPassword(properties);

		LanSecurityFilter filter = new LanSecurityFilter(properties);
		FilterRegistrationBean<LanSecurityFilter> registration = new FilterRegistrationBean<>(filter);
		registration.addUrlPatterns("/lan/*");
		registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
		return registration;
	}

	private void resolveBasicAuthPassword(LanSecurityProperties properties) {
		if (!properties.getBasicAuth().isEnabled()) {
			return;
		}

		String password = properties.getBasicAuth().getPassword();
		boolean randomPassword = StrUtil.isBlank(password);
		if (randomPassword) {
			password = IdUtil.fastSimpleUUID();
			properties.getBasicAuth().setPassword(password);
		}

		String username = properties.getBasicAuth().getUsername();
		if (randomPassword) {
			log.info("【初始化配置-/lan/安全拦截】Basic Auth（随机密码）用户名={}, 密码={}", username, password);
		} else {
			log.info("【初始化配置-/lan/安全拦截】Basic Auth 用户名={}, 密码={}", username, password);
		}
	}
}
