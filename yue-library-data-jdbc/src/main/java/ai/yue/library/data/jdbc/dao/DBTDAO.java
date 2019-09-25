package ai.yue.library.data.jdbc.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.data.jdbc.client.DB;
import ai.yue.library.data.jdbc.constant.DBSortEnum;
import ai.yue.library.data.jdbc.ipo.PageIPO;
import ai.yue.library.data.jdbc.vo.PageTVO;
import cn.hutool.core.util.ClassUtil;

/**
 * @author	ylyue
 * @since	2019年4月30日
 * @param <T> 映射类
 */
public abstract class DBTDAO<T> {

	@Autowired
	protected DB db;
	@SuppressWarnings("unchecked")
	protected Class<T> mappedClass = (Class<T>) ClassUtil.getTypeArgument(getClass());
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
	 * 更新-ById
	 * @param paramJson 更新所用到的参数（包含主键ID字段）
	 */
	public void updateById(JSONObject paramJson) {
		db.updateById(tableName(), paramJson);
	}
	
	/**
	 * 单个
	 * @param id 主键ID
	 * @return POJO对象
	 */
	public T get(Long id) {
		return db.queryById(tableName(), id, mappedClass);
	}
	
	/**
	 * 列表-全部
	 * @return 列表数据
	 */
	public List<T> listAll() {
		return db.queryAll(tableName(), mappedClass);
	}
	
	/**
	 * 分页
	 * @param pageIPO 分页查询参数 {@linkplain PageIPO}，所有的条件参数，都将以等于的形式进行SQL拼接
	 * @return count（总数），data（分页列表数据）
	 */
	public PageTVO<T> page(PageIPO pageIPO) {
		return db.page(tableName(), pageIPO, mappedClass);
	}
	
	/**
	 * 分页-降序
	 * @param pageIPO 分页查询参数 {@linkplain PageIPO}，所有的条件参数，都将以等于的形式进行SQL拼接
	 * @return count（总数），data（分页列表数据）
	 */
	public PageTVO<T> pageDESC(PageIPO pageIPO) {
		return db.page(tableName(), pageIPO, mappedClass, DBSortEnum.降序);
	}
	
}
