package ai.yue.library.data.jdbc.client;

import ai.yue.library.data.jdbc.client.dialect.Dialect;
import ai.yue.library.data.jdbc.client.dialect.impl.MysqlDialect;
import ai.yue.library.data.jdbc.config.properties.JdbcProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

/**
 * <h2>SQL优化型数据库操作</h2>
 * Created by sunJinChuan on 2016/6/6
 * @since 0.0.1
 */
public class Db extends DbInsert implements Cloneable {

	public Db(DataSource dataSource) {
		this(new NamedParameterJdbcTemplate(dataSource));
	}

	public Db(DataSource dataSource, Dialect dialect, JdbcProperties jdbcProperties) {
		this(new JdbcTemplate(dataSource), new NamedParameterJdbcTemplate(dataSource), dialect, jdbcProperties);
	}

	public Db(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this(namedParameterJdbcTemplate.getJdbcTemplate(), namedParameterJdbcTemplate, new MysqlDialect(namedParameterJdbcTemplate), new JdbcProperties());
	}
	
	public Db(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, Dialect dialect) {
		this(jdbcTemplate, namedParameterJdbcTemplate, dialect, new JdbcProperties());
	}

	public Db(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, Dialect dialect, JdbcProperties jdbcProperties) {
		super.jdbcTemplate = jdbcTemplate;
		super.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
		super.dialect = dialect;
		super.jdbcProperties = jdbcProperties;
	}

	/**
	 * 克隆Db
	 * <p>适用于需要复制原有Db配置，并创建新的Db对象以进行修改配置等场景。
	 * <p>如：需要对操作的某个DAO单独配置 {@link JdbcProperties#setEnableDeleteQueryFilter(boolean)} 属性
	 */
	@Override
	public Db clone() {
		return new Db(jdbcTemplate, namedParameterJdbcTemplate, dialect, jdbcProperties);
	}
	
}
