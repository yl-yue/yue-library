package ai.yue.library.test.remove.redis.jdbc;

import ai.yue.library.data.jdbc.dao.AbstractRepository;
import ai.yue.library.test.remove.redis.dataobject.jdbc.UserDO;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

/**
 * @author	ylyue
 * @since	2020年2月21日
 */
@Repository
public class CloneOneDAO extends AbstractRepository<UserDO> {

	@PostConstruct
	private void init() {
		db = db.clone();
		db.getJdbcProperties().setEnableLogicDeleteFilter(true);
	}

	@Override
	protected String tableName() {
		return "user";
	}
	
}
