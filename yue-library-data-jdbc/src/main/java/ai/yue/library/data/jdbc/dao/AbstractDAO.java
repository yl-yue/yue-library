package ai.yue.library.data.jdbc.dao;

import ai.yue.library.base.constant.SortEnum;
import ai.yue.library.data.jdbc.ipo.PageIPO;
import ai.yue.library.data.jdbc.vo.PageVO;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * AbstractDAO 为 JSON 对象提供服务
 * 
 * @author	ylyue
 * @since	2019年4月30日
 */
public abstract class AbstractDAO extends AbstractBaseDAO<JSONObject> {

	@Override
	public JSONObject get(Long id) {
		return db.getById(tableName(), id);
	}

	@Override
	public JSONObject getByUuid(String uuidValue) {
		return db.getByUuid(tableName(), uuidValue);
	}

	@Override
	public List<JSONObject> listAll() {
		return db.listAll(tableName());
	}

	@Override
	public PageVO<JSONObject> page(PageIPO pageIPO) {
		return db.page(tableName(), pageIPO);
	}

	@Override
	public PageVO<JSONObject> pageDESC(PageIPO pageIPO) {
		return db.page(tableName(), pageIPO, SortEnum.DESC);
	}

}
