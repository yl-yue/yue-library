package ai.yue.library.webflux.config;

import ai.yue.library.webflux.config.thread.pool.ContextDecorator;
import ai.yue.library.webflux.env.WebFluxEnv;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.TaskDecorator;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * webflux bean 自动配置
 * 
 * @author	ylyue
 * @since	2018年11月26日
 */
@Configuration
@EnableWebFluxSecurity
@Import({ WebFluxRegistrationsConfig.class, WebFluxEnv.class })
public class WebFluxAutoConfig {

    /**
     * 子线程上下文装饰器
     */
    @Bean
    @ConditionalOnMissingBean({TaskDecorator.class, ContextDecorator.class})
    public TaskDecorator taskDecorator() {
        return new ContextDecorator();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/actuator-security/health").permitAll()
                        .pathMatchers("/actuator-security/health/**").permitAll()
                        .pathMatchers("/actuator-security/**").authenticated()
                        .anyExchange().permitAll()
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .build();
    }

}
