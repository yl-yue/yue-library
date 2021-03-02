package ai.yue.library.data.jdbc.config;

import ai.yue.library.data.jdbc.client.Db;
import ai.yue.library.data.jdbc.client.dialect.Dialect;
import ai.yue.library.data.jdbc.client.dialect.impl.AnsiDialect;
import ai.yue.library.data.jdbc.client.dialect.impl.DmDialect;
import ai.yue.library.data.jdbc.client.dialect.impl.MysqlDialect;
import ai.yue.library.data.jdbc.client.dialect.impl.PostgresqlDialect;
import ai.yue.library.data.jdbc.config.properties.JdbcProperties;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.JdbcConstants;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

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
	@ConditionalOnBean({NamedParameterJdbcTemplate.class})
	public Db db(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcProperties jdbcProperties) {
		DataSource dataSource = namedParameterJdbcTemplate.getJdbcTemplate().getDataSource();
		Dialect dialect;
		if (dataSource instanceof DruidDataSource) {
			String dbType = ((DruidDataSource) dataSource).getDbType();
			if (JdbcConstants.MYSQL.equalsIgnoreCase(dbType)) {
				dialect = new MysqlDialect(namedParameterJdbcTemplate, jdbcProperties);
			} else if (JdbcConstants.POSTGRESQL.equalsIgnoreCase(dbType)) {
				dialect = new PostgresqlDialect(namedParameterJdbcTemplate, jdbcProperties);
			} else if (JdbcConstants.DM.equalsIgnoreCase(dbType)) {
				dialect = new DmDialect(namedParameterJdbcTemplate, jdbcProperties);
			} else {
				dialect = new AnsiDialect(namedParameterJdbcTemplate, jdbcProperties);
			}
		} else {
			dialect = new AnsiDialect(namedParameterJdbcTemplate, jdbcProperties);
		}

		return new Db(dialect);
	}
	
}
