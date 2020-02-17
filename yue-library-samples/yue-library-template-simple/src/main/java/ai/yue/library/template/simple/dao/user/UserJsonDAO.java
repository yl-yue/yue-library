package ai.yue.library.template.simple.dao.user;

import org.springframework.stereotype.Repository;

import ai.yue.library.data.jdbc.dao.DBDAO;

/**
 * UserDAO基于Json操作示例
 * 
 * @author	ylyue
 * @since	2020年2月13日
 */
@Repository
public class UserJsonDAO extends DBDAO {

	@Override
	protected String tableName() {
		return "user";
	}
	
}
