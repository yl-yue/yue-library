package ai.yue.library.data.jdbc.dao;

import ai.yue.library.base.constant.FieldNamingStrategyEnum;
import ai.yue.library.base.constant.SortEnum;
import ai.yue.library.base.convert.Convert;
import ai.yue.library.data.jdbc.ipo.PageIPO;
import ai.yue.library.data.jdbc.vo.PageVO;
import cn.hutool.core.util.ClassUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;

import java.util.List;

/**
 * AbstractRepository 为 DO 对象提供服务，字段映射支持下划线与驼峰自动识别转换
 *
 * @param <T> 映射类
 * @author	ylyue
 * @since	2019年4月30日
 */
public abstract class AbstractRepository<T> extends AbstractBaseDAO {

	@SuppressWarnings("unchecked")
	protected Class<T> mappedClass = (Class<T>) ClassUtil.getTypeArgument(getClass());

	/**
	 * 插入数据-实体
	 * <p>默认进行 {@link FieldNamingStrategyEnum#SNAKE_CASE} 数据库字段命名策略转换
	 *
	 * @param paramIPO 参数IPO（POJO-IPO对象）
	 * @return 返回主键值
	 */
	public Long insert(Object paramIPO) {
		if (db.getJdbcProperties().isEnableFieldNamingStrategyRecognition()) {
			return insert(paramIPO, db.getJdbcProperties().getDatabaseFieldNamingStrategy());
		}

		return insert(Convert.toJSONObject(paramIPO));
	}

	/**
	 * 插入数据-实体
	 *
	 * @param paramIPO 参数IPO（POJO-IPO对象）
	 * @param fieldNamingStrategyEnum 数据库字段命名策略
	 * @return 返回主键值
	 */
	public Long insert(Object paramIPO, FieldNamingStrategyEnum fieldNamingStrategyEnum) {
		PropertyNamingStrategy propertyNamingStrategy = fieldNamingStrategyEnum.getPropertyNamingStrategy();
		SerializeConfig serializeConfig = new SerializeConfig();
		serializeConfig.setPropertyNamingStrategy(propertyNamingStrategy);
		JSONObject paramJson = (JSONObject) JSONObject.toJSON(paramIPO, serializeConfig);
		return insert(paramJson);
	}

	@Override
	public T getById(Long id) {
		return db.getById(tableName(), id, mappedClass);
	}

	@Override
	public T getByUuid(String uuidValue) {
		return db.getByUuid(tableName(), uuidValue, mappedClass);
	}

	@Override
	public List<T> listAll() {
		return db.listAll(tableName(), mappedClass);
	}

	@Override
	public PageVO<T> page(PageIPO pageIPO) {
		return db.page(tableName(), pageIPO, mappedClass);
	}

	@Override
	public PageVO<T> pageDESC(PageIPO pageIPO) {
		return db.page(tableName(), pageIPO, SortEnum.DESC, mappedClass);
	}

}
