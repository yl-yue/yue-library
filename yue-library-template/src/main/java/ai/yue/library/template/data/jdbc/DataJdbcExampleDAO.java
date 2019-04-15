package ai.yue.library.template.data.jdbc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.data.jdbc.client.DB;
import ai.yue.library.data.jdbc.ipo.PageIPO;
import ai.yue.library.data.jdbc.vo.PageVO;
import ai.yue.library.template.dataobject.UserDO;

/**
 * @author  孙金川
 * @version 创建时间：2019年3月12日
 */
@Repository
public class DataJdbcExampleDAO {

	@Autowired
	DB db;
	
	/**
	 * 插入数据
	 * @param paramJson
	 * @return
	 */
	public Long insert(JSONObject paramJson) {
		return db.insert("user_info", paramJson);
	}
	
	/**
	 * 插入数据-批量
	 * @param paramJsons
	 */
	public void insertBatch(JSONObject[] paramJsons) {
		db.insertBatch("user_info", paramJsons);
	}
	
	/**
	 * 删除
	 * @param id
	 */
	public void delete(Long id) {
		db.delete("user_info", id);
	}
	
	/**
	 * 删除
	 * @param name
	 */
	public void deleteByName(String name) {
		JSONObject paramJson = new JSONObject();
		paramJson.put("name", name);
		db.delete("user_info", paramJson);
	}
	
	/**
	 * 删除-批量
	 * @param paramJsons
	 */
	public void deleteBatch(JSONObject[] paramJsons) {
		db.deleteBatch("user_info", paramJsons);
	}
	
	/**
	 * 更新-ById
	 * @param paramJson
	 */
	public void updateById(JSONObject paramJson) {
		db.updateById("user_info", paramJson);
	}
	
	/**
	 * 更新-ByName
	 * @param paramJson
	 */
	public void updateByName(JSONObject paramJson) {
		String[] conditions = {"name"};
		long updateRowsNumber = db.update("tableName", paramJson, conditions);
		int expectedValue = 1;
		db.updateAndExpectedEqual(updateRowsNumber, expectedValue);
	}
	
	/**
	 * 更新-排序
	 * @param id
	 * @param move
	 */
	public void updateSort(Long id, Integer move) {
		String uniqueKeys = "name";
		db.updateSort("tableName", id, move, uniqueKeys);
	}
	
//	/**
//	 * 单个
//	 * @param id
//	 * @return
//	 */
//	public JSONObject get(Long id) {
//		return db.queryById("user_info", id);
//	}
	
	/**
	 * 单个
	 * @param id
	 * @return
	 */
	public UserDO get(Long id) {
		return db.queryById("user_info", id, UserDO.class);
	}
	
	/**
	 * 列表-全部
	 * @return
	 */
	public List<JSONObject> listAll() {
		return db.queryAll("tableName");
	}
	
	/**
	 * 列表
	 * @param id
	 * @return
	 */
	public List<JSONObject> list(Long id) {
		// 1. 处理参数
		JSONObject paramJson = new JSONObject();
		paramJson.put("id", id);

		// 2. 查询SQL
		String sql = "";

		// 3. 返回结果
		return db.queryForList(sql, paramJson);
	}
	
	/**
	 * 分页
	 * @param pageIPO
	 * @return
	 */
	public PageVO page(PageIPO pageIPO) {
		return db.page("user_info", pageIPO);
	}
	
	/**
	 * 分页
	 * @param pageIPO
	 * @return
	 */
	public PageVO pageSql(PageIPO pageIPO) {
		String querySql = "";
		return db.pageSql(querySql, pageIPO);
	}
	
}
