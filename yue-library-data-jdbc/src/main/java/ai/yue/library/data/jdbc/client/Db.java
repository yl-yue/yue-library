package ai.yue.library.data.jdbc.client;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ai.yue.library.data.jdbc.client.dialect.Dialect;
import ai.yue.library.data.jdbc.client.dialect.impl.MysqlDialect;

/**
 * <h2>SQL优化型数据库操作</h2>
 * Created by sunJinChuan on 2016/6/6
 * @since 0.0.1
 */
public class Db extends DbInsert implements Cloneable {

	public Db(DataSource dataSource) {
		this(new NamedParameterJdbcTemplate(dataSource));
	}
	
	public Db(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		super.jdbcTemplate = namedParameterJdbcTemplate.getJdbcTemplate();
		super.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
		super.dialect = new MysqlDialect(namedParameterJdbcTemplate);
	}
	
	public Db(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, Dialect dialect) {
		super.jdbcTemplate = jdbcTemplate;
		super.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
		super.dialect = dialect;
	}
	
	/**
	 * 克隆Db
	 * <p>适用于需要复制原有Db配置，并创建新的Db对象以进行修改配置等场景。
	 * <p>如：需要对操作的某个DAO单独配置 {@link #setEnableDeleteQueryFilter(boolean)} 属性
	 */
	@Override
	public Db clone() {
		Db db = new Db(jdbcTemplate, namedParameterJdbcTemplate, dialect);
		db.setBusinessUk(businessUk);
		db.setDatabaseFieldNamingStrategy(databaseFieldNamingStrategy);
		db.setDatabaseFieldNamingStrategyRecognitionEnabled(databaseFieldNamingStrategyRecognitionEnabled);
		db.setEnableDeleteQueryFilter(enableDeleteQueryFilter);
		return db;
	}
	
}
