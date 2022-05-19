package ai.yue.library.data.jdbc.client;

import ai.yue.library.data.jdbc.client.dialect.Dialect;
import ai.yue.library.data.jdbc.client.dialect.impl.MysqlDialect;
import ai.yue.library.data.jdbc.config.properties.JdbcProperties;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

/**
 * <h2>SQL优化型数据库操作</h2>
 * Created by sunJinChuan on 2016/6/6
 * @since 0.0.1
 */
public class Db extends DbInsert implements Cloneable {

	public Db(DataSource dataSource) {
		this(new MysqlDialect(new NamedParameterJdbcTemplate(dataSource), new JdbcProperties()));
	}

	public Db(Dialect dialect) {
		super.dialect = dialect;
	}

	/**
	 * 克隆Db
	 *
	 * <ul>
	 *     <li>适用于需要复制原有Db配置，并创建新的Db对象以进行修改配置等场景</li>
	 *     <li>如：需要对操作的某个DAO单独配置 {@link #getJdbcProperties()} {@link JdbcProperties#setEnableLogicDeleteFilter(boolean)} 属性</li>
	 * </ul>
	 */
	@Override
	public Db clone() {
		return new Db(dialect.cloneDialect());
	}
	
}
