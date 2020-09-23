package ai.yue.library.test.webflux.dao.data.jdbc;

import org.springframework.stereotype.Repository;

import ai.yue.library.data.jdbc.dao.AbstractRepository;
import ai.yue.library.test.webflux.dataobject.UserDO;

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
