package ai.yue.library.test.dao.data.jdbc;

import ai.yue.library.data.jdbc.client.dialect.impl.MysqlDialect;
import ai.yue.library.data.jdbc.client.dialect.impl.PostgresqlDialect;
import ai.yue.library.data.jdbc.dao.AbstractRepository;
import ai.yue.library.test.dataobject.jdbc.TableExampleTestDO;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

/**
 * PostgreSQL示例
 *
 * @author ylyue
 * @since 2022/6/28
 */
@DS("postgresql")
@Repository
public class PostgreSQLTestDAO extends AbstractRepository<TableExampleTestDO> {

    @Override
    protected String tableName() {
        return "table_example_test";
    }

    @PostConstruct
    private void initDb() {
        db = db.clone();
        db.setDialect(new PostgresqlDialect(db.getNamedParameterJdbcTemplate(), db.getJdbcProperties()));
//        db.setDialect(new MysqlDialect(db.getNamedParameterJdbcTemplate(), db.getJdbcProperties()));
    }

}
