package ai.yue.library.data.jdbc.client.dialect.impl;

import ai.yue.library.data.jdbc.client.dialect.AnsiSqlDialect;
import ai.yue.library.data.jdbc.client.dialect.DialectName;
import ai.yue.library.data.jdbc.client.dialect.Wrapper;

/**
 * Postgree方言
 * <p>TODO 暂未实现
 * 
 * @author	ylyue
 * @since	2020年6月13日
 */
public abstract class PostgresqlDialect extends AnsiSqlDialect {
	
	private static final long serialVersionUID = 3889210427543389642L;

	public PostgresqlDialect() {
		wrapper = new Wrapper('"');
	}

	@Override
	public DialectName dialectName() {
		return DialectName.POSTGREESQL;
	}
	
}
