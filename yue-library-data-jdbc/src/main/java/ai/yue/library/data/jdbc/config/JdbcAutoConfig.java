package ai.yue.library.data.jdbc.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ai.yue.library.data.jdbc.client.DB;

/**
 * data-jdbc配置，提供自动配置项支持与增强
 * 
 * @author	ylyue
 * @since	2018年6月11日
 */
@Configuration
@AutoConfigureAfter(JdbcTemplateAutoConfiguration.class)
public class JdbcAutoConfig {

	@Bean
	@Primary
	@ConditionalOnBean({JdbcTemplate.class, NamedParameterJdbcTemplate.class})
	public DB db(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		return new DB(jdbcTemplate, namedParameterJdbcTemplate);
	}
	
}
