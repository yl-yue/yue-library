package ai.yue.library.data.jdbc.client.dialect.impl;

import ai.yue.library.data.jdbc.client.dialect.AnsiSqlDialect;
import ai.yue.library.data.jdbc.client.dialect.DialectNameEnum;
import ai.yue.library.data.jdbc.client.dialect.Wrapper;
import ai.yue.library.data.jdbc.config.properties.JdbcProperties;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * Postgre方言
 * <p>TODO 暂未实现
 * 
 * @author	ylyue
 * @since	2020年6月13日
 */
public abstract class PostgresqlDialect extends AnsiSqlDialect {
	
	private static final long serialVersionUID = 3889210427543389642L;
	
	public PostgresqlDialect(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcProperties jdbcProperties) {
		super.wrapper = new Wrapper('"');
		super.dialect = this;
		super.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
		super.jdbcProperties = jdbcProperties;
	}

	@Override
	public DialectNameEnum dialectName() {
		return DialectNameEnum.POSTGRESQL;
	}
	
}
