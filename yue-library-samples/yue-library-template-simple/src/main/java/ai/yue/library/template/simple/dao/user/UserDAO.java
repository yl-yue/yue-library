package ai.yue.library.template.simple.dao.user;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.data.jdbc.dao.DBTDAO;
import ai.yue.library.template.simple.dataobject.UserDO;

/**
 * @author	ylyue
 * @since	2019年9月25日
 */
@Repository
public class UserDAO extends DBTDAO<UserDO> {

	@Override
	protected String tableName() {
		return "user_base_info";
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
		long dataSize = db.query(tableName(), paramJson, mappedClass).size();
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
		return db.resultToObject(db.query(tableName(), paramJson, mappedClass));
	}
	
}
