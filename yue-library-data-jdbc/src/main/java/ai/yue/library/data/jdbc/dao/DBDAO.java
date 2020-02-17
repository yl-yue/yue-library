package ai.yue.library.data.jdbc.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.data.jdbc.client.DB;
import ai.yue.library.data.jdbc.constant.DBSortEnum;
import ai.yue.library.data.jdbc.ipo.PageIPO;
import ai.yue.library.data.jdbc.vo.PageVO;

/**
 * DBDAO 为 JSON 对象提供服务
 * 
 * @author	ylyue
 * @since	2019年4月30日
 */
public abstract class DBDAO {

	@Autowired
	protected DB db;
	protected abstract String tableName();
    
	/**
	 * 插入数据
	 * @param paramJson 参数
	 * @return 返回主键值
	 */
	public Long insert(JSONObject paramJson) {
		return db.insert(tableName(), paramJson);
	}
	
	/**
	 * 插入数据-批量
	 * @param paramJsons 参数
	 */
	public void insertBatch(JSONObject[] paramJsons) {
		db.insertBatch(tableName(), paramJsons);
	}
	
	/**
	 * 删除
	 * @param id 主键id
	 */
	public void delete(Long id) {
		db.delete(tableName(), id);
	}
	
	/**
	 * 删除-安全的
	 * <p>数据删除前会先进行条数确认
	 * 
	 * @param id 主键id
	 */
	public void deleteSafe(Long id) {
		db.deleteSafe(tableName(), id);
	}
	
	/**
	 * 更新-ById
	 * @param paramJson 更新所用到的参数（包含主键ID字段）
	 */
	public void updateById(JSONObject paramJson) {
		db.updateById(tableName(), paramJson);
	}
	
	/**
	 * 单个
	 * @param id 主键id
	 * @return JSON数据
	 */
	public JSONObject get(Long id) {
		return db.queryById(tableName(), id);
	}
	
	/**
	 * 列表-全部
	 * @return 列表数据
	 */
	public List<JSONObject> listAll() {
		return db.queryAll(tableName());
	}
	
	/**
	 * 分页
	 * @param pageIPO 分页查询参数 {@linkplain PageIPO}，所有的条件参数，都将以等于的形式进行SQL拼接
	 * @return count（总数），data（分页列表数据）
	 */
	public PageVO page(PageIPO pageIPO) {
		return db.page(tableName(), pageIPO);
	}
	
	/**
	 * 分页-降序
	 * @param pageIPO 分页查询参数 {@linkplain PageIPO}，所有的条件参数，都将以等于的形式进行SQL拼接
	 * @return count（总数），data（分页列表数据）
	 */
	public PageVO pageDESC(PageIPO pageIPO) {
		return db.page(tableName(), pageIPO, DBSortEnum.降序);
	}
	
}
