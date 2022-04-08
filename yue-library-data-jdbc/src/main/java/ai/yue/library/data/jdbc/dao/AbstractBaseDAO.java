package ai.yue.library.data.jdbc.dao;

import ai.yue.library.data.jdbc.client.Db;
import ai.yue.library.data.jdbc.config.properties.JdbcProperties;
import ai.yue.library.data.jdbc.constant.DbConstant;
import ai.yue.library.data.jdbc.ipo.PageIPO;
import ai.yue.library.data.jdbc.vo.PageVO;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 抽象的基础DAO
 *
 * @param <T> 映射类
 * @author	ylyue
 * @since	2019年4月30日
 */
abstract class AbstractBaseDAO<T> {

	@Autowired
	protected Db db;
	protected String tableName = tableName();

	/**
	 * 设置表名
	 *
	 * @return 表名
	 */
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
	 * <p><code style="color:red">依赖于接口传入 {@value DbConstant#FIELD_DEFINITION_PRIMARY_KEY} 参数时慎用此方法</code>，避免有序主键被遍历风险，造成数据越权行为。推荐使用 {@link #deleteByUuid(String)}</p>
	 *
	 * @param id 主键id
	 */
	public void delete(Long id) {
		db.delete(tableName(), id);
	}

	/**
	 * 删除-逻辑的
	 * <p>数据非真实删除，而是更改 {@link JdbcProperties().getFieldDefinitionDeleteTime()} 字段值为时间戳，代表数据已删除
	 * <p><code style="color:red">依赖于接口传入 {@value DbConstant#FIELD_DEFINITION_PRIMARY_KEY} 参数时慎用此方法</code>，避免有序主键被遍历风险，造成数据越权行为。推荐使用 {@link #deleteByUuid(String)}</p>
	 *
	 * @param id 主键id
	 */
	public void deleteLogic(Long id) {
		db.deleteLogic(tableName(), id);
	}

	/**
	 * 删除-通过表无序主键
	 * <p>数据删除前会先进行条数确认
	 * <p>无序主键名默认为 {@link JdbcProperties#getFieldDefinitionUuid()}
	 * <p>无序主键值请使用UUID5无符号位
	 *
	 * @param uuidValue 无序主键的唯一值
	 */
	public void deleteByUuid(String uuidValue) {
		db.deleteByUuid(tableName, uuidValue);
	}

	/**
	 * 删除-逻辑的
	 * <p>数据非真实删除，而是更改 {@link JdbcProperties().getFieldDefinitionDeleteTime()} 字段值为时间戳，代表数据已删除
	 *
	 * @param uuidValue 无序主键的唯一值
	 */
	public void deleteLogicByUuid(String uuidValue) {
		db.deleteLogicByUuid(tableName, uuidValue);
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
	 * 更新-By无序主键
	 * <p>根据表中无序主键进行更新
	 * <p>无序主键名默认为 {@link JdbcProperties#getFieldDefinitionUuid()}
	 * <p>无序主键值请使用UUID5无符号位
	 *
	 * @param paramJson 更新所用到的参数（包含业务键字段）
	 */
	public void updateByUuid(JSONObject paramJson) {
		db.updateByUuid(tableName(), paramJson);
	}

	/**
	 * 单个
	 *
	 * @param id 主键id
	 * @return 可以是一个正确的单行查询结果、或null、或查询结果是多条数据而引发的预期错误异常
	 */
	public abstract T get(Long id);

	/**
	 * 单个-By无序主键
	 * <p>无序主键名默认为 {@link JdbcProperties#getFieldDefinitionUuid()}
	 * <p>无序主键值请使用UUID5无符号位
	 *
	 * @param uuidValue   无序主键值
	 * @return 可以是一个正确的单行查询结果、或null、或查询结果是多条数据而引发的预期错误异常
	 */
	public abstract T getByUuid(String uuidValue);

	/**
	 * 列表-全部
	 *
	 * @return 列表数据
	 */
	public abstract List<T> listAll();

	/**
	 * 分页
	 *
	 * @param pageIPO 分页查询参数 {@linkplain PageIPO}，所有的条件参数，都将以等于的形式进行SQL拼接
	 * @return count（总数），data（分页列表数据）
	 */
	public abstract PageVO<T> page(PageIPO pageIPO);

	/**
	 * 分页-降序
	 *
	 * @param pageIPO 分页查询参数 {@linkplain PageIPO}，所有的条件参数，都将以等于的形式进行SQL拼接
	 * @return count（总数），data（分页列表数据）
	 */
	public abstract PageVO<T> pageDESC(PageIPO pageIPO);

}
