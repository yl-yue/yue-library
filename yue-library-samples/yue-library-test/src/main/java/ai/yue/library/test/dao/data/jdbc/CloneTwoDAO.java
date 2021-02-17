package ai.yue.library.test.dao.data.jdbc;

import ai.yue.library.data.jdbc.dao.AbstractRepository;
import ai.yue.library.test.dataobject.jdbc.UserDO;
import org.springframework.stereotype.Repository;

/**
 * @author	ylyue
 * @since	2020年2月21日
 */
@Repository
public class CloneTwoDAO extends AbstractRepository<UserDO> {

	@Override
	protected String tableName() {
		return "user";
	}
	
}
