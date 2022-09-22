package ai.yue.library.test.doc.example.data.es;

import ai.yue.library.base.constant.SortEnum;
import ai.yue.library.data.jdbc.client.Db;
import ai.yue.library.data.jdbc.dao.AbstractDAO;
import ai.yue.library.data.jdbc.ipo.PageIPO;
import ai.yue.library.data.jdbc.vo.PageBeforeAndAfterVO;
import ai.yue.library.data.jdbc.vo.PageVO;
import ai.yue.library.test.dataobject.jdbc.UserDO;
import cn.hutool.core.lang.Console;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * ES SQL使用示例
 *
 * @author  ylyue
 * @version 创建时间：2019年3月12日
 */
//@Repository
public class DataEsExampleDAO extends AbstractDAO {

	@Autowired
	@Qualifier("esDb")
	private Db esDb;
	@Autowired
	private RestHighLevelClient elasticsearchClient;
	private ElasticsearchRestTemplate elasticsearchRestTemplate;

	@PostConstruct
	private void init() {
		db = esDb;
		elasticsearchRestTemplate = new ElasticsearchRestTemplate(elasticsearchClient);
	}

	@Override
	protected String tableName() {
		return "table_example";
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
		int[] updateRowsNumbers = db.deleteBatchSql(sql, paramJsons);
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
		return db.pageSqlBeforeAndAfter(querySql, pageIPO, equalsId);
	}
	
}
