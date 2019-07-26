package ai.yue.library.template.data.jdbc;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.data.jdbc.constant.DBSortEnum;
import ai.yue.library.data.jdbc.constant.DBUpdateEnum;
import ai.yue.library.data.jdbc.dao.DBDAO;
import ai.yue.library.data.jdbc.ipo.PageIPO;
import ai.yue.library.data.jdbc.vo.PageBeforeAndAfterVO;
import ai.yue.library.data.jdbc.vo.PageVO;
import ai.yue.library.template.dataobject.UserDO;
import cn.hutool.core.lang.Console;

/**
 * @author  孙金川
 * @version 创建时间：2019年3月12日
 */
@Repository
public class DataJdbcExampleDAO extends DBDAO {

	@Override
	protected String tableName() {
		return tableName();
	}
	
	/**
	 * 插入数据-自动递增 sort_idx
	 * @param paramJson
	 * @return
	 */
	public Long insert(JSONObject paramJson) {
		String uniqueKeys = "name";// 可选参数
		return db.insertWithSortIdxAutoIncrement(tableName(), paramJson, uniqueKeys);
	}
	
	/**
	 * 插入或更新
	 * @param paramJson
	 * @return
	 */
	public Long insertOrUpdate(JSONObject paramJson) {
		String[] conditions = {"id"};
		return db.insertOrUpdate(tableName(), paramJson, conditions, DBUpdateEnum.正常);
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
	 * 删除-批量
	 * @param paramJsons
	 */
	public void deleteBatch2(JSONObject[] paramJsons) {
		String sql =
				"DELETE \n" +
				"FROM\n" +
				"	table_example \n" +
				"WHERE\n" +
				"	1 = 1 \n" +
				"	AND user_id > :user_id";
		int[] updateRowsNumbers = db.deleteBatch2(sql, paramJsons);
		Console.log(Arrays.toString(updateRowsNumbers));
	}
	
	/**
	 * 更新-ById
	 * @param paramJson
	 */
	public void updateById2(JSONObject paramJson) {
		db.updateById(tableName(), paramJson, DBUpdateEnum.递减_无符号);
	}
	
	/**
	 * 更新-ByName
	 * @param paramJson
	 */
	public void updateByName(JSONObject paramJson) {
		String[] conditions = {"name"};
		long updateRowsNumber = db.update(tableName(), paramJson, conditions);
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
		db.updateSort(tableName(), id, move, uniqueKeys);
	}
	
	/**
	 * 更新-批量
	 * @param paramJsons
	 */
	public void updateBatch(JSONObject[] paramJsons) {
		String sql =
				"UPDATE table_example \n" +
				"SET nickname = :nickname \n" +
				"WHERE\n" +
				"	1 = 1 \n" +
				"	AND user_id > :user_id";
		int[] updateRowsNumbers = db.updateBatch(sql, paramJsons);
		Console.log(Arrays.toString(updateRowsNumbers));
	}
	
	/**
	 * 列表
	 * 
	 * @param paramJson 查询参数
	 * @return 列表数据
	 */
	public List<JSONObject> list(JSONObject paramJson) {
		return db.query(tableName(), paramJson, DBSortEnum.降序);
	}
	
	public void query(JSONObject paramJson) {
		// 1. 查询SQL
		String sql = "";
		
		// 2. 查询
		db.queryForJson(sql, paramJson);
		db.queryForObject(sql, paramJson, UserDO.class);
		db.queryForList(sql, paramJson);
		db.queryForList(sql, paramJson, UserDO.class);
	}
	
	/**
	 * 分页
	 * @param pageIPO
	 * @return
	 */
	public PageVO pageWhere(PageIPO pageIPO) {
		String whereSql = "WHERE 1 = 1 AND user_id >= :user_id";
		return db.pageWhere(tableName(), whereSql, pageIPO);
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
	
	/**
	 * 分页-上一条与下一条数据
	 * 
	 * @param pageIPO
	 * @return
	 */
	public PageBeforeAndAfterVO pageBeforeAndAfter(PageIPO pageIPO) {
		String querySql = "";
		Long equalsId = 30L;
		return db.pageBeforeAndAfter(querySql, pageIPO, equalsId);
	}
	
}
