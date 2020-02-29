package ai.yue.library.data.jdbc.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ai.yue.library.data.jdbc.client.Db;
import ai.yue.library.data.jdbc.config.properties.JdbcProperties;

/**
 * data-jdbc配置，提供自动配置项支持与增强
 * 
 * @author	ylyue
 * @since	2018年6月11日
 */
@Configuration
@AutoConfigureAfter(JdbcTemplateAutoConfiguration.class)
@EnableConfigurationProperties({ JdbcProperties.class })
public class JdbcAutoConfig {

	@Bean
	@Primary
	@ConditionalOnBean({JdbcTemplate.class, NamedParameterJdbcTemplate.class})
	public Db db(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		return new Db(jdbcTemplate, namedParameterJdbcTemplate);
	}
	
}
