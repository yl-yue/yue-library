package ai.yue.library.data.jdbc.client;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ai.yue.library.data.jdbc.client.dialect.Dialect;

/**
 * <h2>SQL优化型数据库操作</h2>
 * Created by sunJinChuan on 2016/6/6
 * @since 0.0.1
 */
public class Db extends DbInsert {

	public Db(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, Dialect dialect) {
		super.jdbcTemplate = jdbcTemplate;
		super.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
		super.dialect = dialect;
	}
	
}
