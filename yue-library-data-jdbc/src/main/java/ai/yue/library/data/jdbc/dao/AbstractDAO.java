package ai.yue.library.data.jdbc.dao;

import ai.yue.library.base.constant.SortEnum;
import ai.yue.library.data.jdbc.client.Db;
import ai.yue.library.data.jdbc.constant.DbConstant;
import ai.yue.library.data.jdbc.ipo.PageIPO;
import ai.yue.library.data.jdbc.vo.PageVO;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * AbstractDAO 为 JSON 对象提供服务
 * 
 * @author	ylyue
 * @since	2019年4月30日
 */
public abstract class AbstractDAO {

	@Autowired
	protected Db db;
	protected String tableName = tableName();
	protected abstract String tableName();

	/**
	 * 插入数据
	 *
	 * @param paramJson 参数
	 * @return 返回主键值
	 */
	public Long insert(JSONObject paramJson) {
		return db.insert(tableName(), paramJson);
	}

	/**
	 * 插入数据-批量
	 *
	 * @param paramJsons 参数
	 */
	public void insertBatch(JSONObject[] paramJsons) {
		db.insertBatch(tableName(), paramJsons);
	}

	/**
	 * 删除
	 * <p>数据删除前会先进行条数确认
	 * <p><code style="color:red">依赖于接口传入 {@value DbConstant#PRIMARY_KEY} 参数时慎用此方法</code>，避免有序主键被遍历风险，造成数据越权行为。推荐使用 {@link #deleteByBusinessUk(String)}</p>
	 *
	 * @param id 主键id
	 */
	public void delete(Long id) {
		db.delete(tableName(), id);
	}

	/**
	 * 删除-安全的
	 * <p>数据删除前会先进行条数确认
	 *
	 * @deprecated 请使用：{@link #delete(Long)}
	 * @param id 主键id
	 */
	@Deprecated
	public void deleteSafe(Long id) {
		db.deleteSafe(tableName(), id);
	}

	/**
	 * 删除-通过表业务键
	 * <p>数据删除前会先进行条数确认
	 * <p>默认业务键为key
	 * <p>业务键值推荐使用UUID5
	 *
	 * @param businessUkValue 业务键的唯一值
	 */
	public void deleteByBusinessUk(String businessUkValue) {
		db.deleteByBusinessUk(tableName, businessUkValue);
	}

	/**
	 * 删除-逻辑的
	 * <p>数据非真实删除，而是更改 {@value DbConstant#FIELD_DEFINITION_DELETE_TIME} 字段值为时间戳，代表数据已删除
	 *
	 * @param businessUkValue 业务键的唯一值
	 */
	public void deleteLogicByBusinessUk(String businessUkValue) {
		db.deleteLogicByBusinessUk(tableName, businessUkValue);
	}

	/**
	 * 更新-ById
	 *
	 * @param paramJson 更新所用到的参数（包含主键ID字段）
	 */
	public void updateById(JSONObject paramJson) {
		db.updateById(tableName(), paramJson);
	}

	/**
	 * 更新-By业务键
	 * <p>根据表中业务键进行更新
	 * <p>默认业务键为key
	 * <p>业务键值推荐使用UUID5
	 *
	 * @param paramJson		更新所用到的参数（包含业务键字段）
	 */
	public void updateByBusinessUk(JSONObject paramJson) {
		db.updateByBusinessUk(tableName(), paramJson);
	}

	/**
	 * 单个
	 *
	 * @param id 主键id
	 * @return JSON数据（结果映射应该是一个单行查询否则结果为null）
	 */
	public JSONObject get(Long id) {
		return db.getById(tableName(), id);
	}

	/**
	 * 单个-By业务键
	 * <p>默认业务键为key
	 * <p>业务键值推荐使用UUID5
	 *
	 * @param businessUkValue 业务键的唯一值
	 * @return POJO对象（结果映射应该是一个单行查询否则结果为null）
	 */
	public JSONObject getByBusinessUk(String businessUkValue) {
		return db.getByBusinessUk(tableName(), businessUkValue);
	}

	/**
	 * 列表-全部
	 *
	 * @return 列表数据
	 */
	public List<JSONObject> listAll() {
		return db.listAll(tableName());
	}

	/**
	 * 分页
	 *
	 * @param pageIPO 分页查询参数 {@linkplain PageIPO}，所有的条件参数，都将以等于的形式进行SQL拼接
	 * @return count（总数），data（分页列表数据）
	 */
	public PageVO page(PageIPO pageIPO) {
		return db.page(tableName(), pageIPO);
	}

	/**
	 * 分页-降序
	 *
	 * @param pageIPO 分页查询参数 {@linkplain PageIPO}，所有的条件参数，都将以等于的形式进行SQL拼接
	 * @return count（总数），data（分页列表数据）
	 */
	public PageVO pageDESC(PageIPO pageIPO) {
		return db.page(tableName(), pageIPO, SortEnum.DESC);
	}
	
}
