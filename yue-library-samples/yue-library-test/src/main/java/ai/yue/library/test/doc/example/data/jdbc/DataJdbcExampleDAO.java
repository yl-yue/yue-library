package ai.yue.library.test.doc.example.data.jdbc;

import ai.yue.library.base.constant.SortEnum;
import ai.yue.library.data.jdbc.constant.DbUpdateEnum;
import ai.yue.library.data.jdbc.dao.AbstractDAO;
import ai.yue.library.data.jdbc.ipo.PageIPO;
import ai.yue.library.data.jdbc.vo.PageBeforeAndAfterVO;
import ai.yue.library.data.jdbc.vo.PageVO;
import ai.yue.library.test.dataobject.jdbc.UserDO;
import cn.hutool.core.lang.Console;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

/**
 * @author  ylyue
 * @version 创建时间：2019年3月12日
 */
@Repository
public class DataJdbcExampleDAO extends AbstractDAO {

	@Override
	protected String tableName() {
		return "table_example";
	}

	public void example(JSONObject paramJson) {
		db.insert(tableName(), paramJson);
		db.updateById(tableName(), paramJson);
		db.deleteByUuid(tableName(), "uuid");
		db.getByUuid(tableName(), "uuid");
		db.queryForList("SELECT * FROM table_example", null);
	}

	/**
	 * 插入数据-自动递增 sort_idx
	 * @param paramJson
	 * @return
	 */
	public Long insertWithSortIdxAutoIncrement(JSONObject paramJson) {
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
		return db.insertOrUpdate(tableName(), paramJson, conditions, DbUpdateEnum.NORMAL);
	}
	
	/**
	 * 删除
	 * @param name
	 */
	public void deleteByName(String name) {
		JSONObject paramJson = new JSONObject();
		paramJson.put("name", name);
		db.delete(tableName(), paramJson);
	}
	
	/**
	 * 删除-批量
	 * @param paramJsons
	 */
	public void deleteBatch(JSONObject[] paramJsons) {
		db.deleteBatch(tableName(), paramJsons);
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
		db.updateById(tableName(), paramJson, DbUpdateEnum.DECR_UNSIGNED);
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
		return db.list(tableName(), paramJson, SortEnum.DESC);
	}
	
	public void query(JSONObject paramJson) {
		// 1. 查询SQL
		String sqlGet = "SELECT * FROM table_example WHERE id = 1";
		String sqlList = "SELECT * FROM table_example";

		// 2. 查询
		JSONObject jsonObject = db.queryForJson(sqlGet, paramJson);
		UserDO userDO = db.queryForObject(sqlGet, paramJson, UserDO.class);
		List<JSONObject> jsonObjectList = db.queryForList(sqlList, paramJson);
		List<UserDO> userDOList = db.queryForList(sqlList, paramJson, UserDO.class);
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
	 *
	 * @param pageIPO
	 * @return
	 */
	public PageVO pageSql(PageIPO pageIPO) {
		// 示例sql为sql优化型分页sql，解决mysql limit分页缺陷
		String querySql =
				"SELECT\n" +
				"	a.* \n" +
				"FROM\n" +
				"	table_example a,\n" +
				"	( SELECT id FROM table_example WHERE 1 > 5 LIMIT 0, 10 ) b \n" +
				"WHERE\n" +
				"	a.id = b.id";
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
