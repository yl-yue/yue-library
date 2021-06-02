package ai.yue.library.template.boot.dao.user;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.data.jdbc.dao.AbstractRepository;
import ai.yue.library.template.boot.dataobject.user.UserDO;

/**
 * UserDAO基于DO操作示例
 * 
 * @author	ylyue
 * @since	2019年9月25日
 */
@Repository
public class UserDAO extends AbstractRepository<UserDO> {

	@Override
	protected String tableName() {
		return "user";
	}
	
	/**
	 * 确认-用户
	 * 
	 * @param cellphone
	 * @return
	 */
	public boolean isUser(String cellphone) {
		JSONObject paramJson = new JSONObject();
		paramJson.put("cellphone", cellphone);
		long dataSize = db.queryForList(tableName(), paramJson, mappedClass).size();
		return db.isDataSize(dataSize);
	}
	
	/**
	 * 单个
	 * 
	 * @param cellphone
	 * @param password
	 * @return
	 */
	public UserDO get(String cellphone, String password) {
		JSONObject paramJson = new JSONObject();
		paramJson.put("cellphone", cellphone);
		paramJson.put("password", password);
		return db.resultToObject(db.queryForList(tableName(), paramJson, mappedClass));
	}
	
}
