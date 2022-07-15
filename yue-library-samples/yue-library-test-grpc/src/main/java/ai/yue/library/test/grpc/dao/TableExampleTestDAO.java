package ai.yue.library.test.grpc.dao;

import ai.yue.library.data.jdbc.dao.AbstractRepository;
import ai.yue.library.test.grpc.dataobject.TableExampleTestDO;
import org.springframework.stereotype.Repository;

/**
 * @author	ylyue
 * @since	2020年2月21日
 */
@Repository
public class TableExampleTestDAO extends AbstractRepository<TableExampleTestDO> {

	@Override
	protected String tableName() {
		return "table_example_test";
	}
	
}
