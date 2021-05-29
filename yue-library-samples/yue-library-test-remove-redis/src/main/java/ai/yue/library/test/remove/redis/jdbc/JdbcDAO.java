package ai.yue.library.test.remove.redis.jdbc;

import ai.yue.library.test.remove.redis.dataobject.jdbc.UserDO;
import org.springframework.stereotype.Repository;

import ai.yue.library.data.jdbc.dao.AbstractRepository;

/**
 * @author	ylyue
 * @since	2020年2月21日
 */
@Repository
public class JdbcDAO extends AbstractRepository<UserDO> {

	@Override
	protected String tableName() {
		return "user";
	}
	
}
