package ai.yue.library.test.remove.redis.controller.data.jdbc.query.map;

import org.springframework.stereotype.Repository;

import ai.yue.library.data.jdbc.dao.AbstractRepository;

/**
 * 人员测试
 * 
 * @author	ylyue
 * @since	2020年11月2日
 */
@Repository
public class PersonDAO extends AbstractRepository<PersonDO> {

	@Override
	protected String tableName() {
		return "base_person";
	}

	/**
	 * 单个
	 *
	 * @param id
	 * @return
	 */
	public PersonVO getPersonVO(Long id) {
		return db.getById(tableName(), id, PersonVO.class);
	}

}
