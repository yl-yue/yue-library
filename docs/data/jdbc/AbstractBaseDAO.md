# AbstractBaseDAO
## 简单使用
`data-jdbc`所有的CRUD方法都在`Db`类里面，所以使用时只需要直接注入即可，推荐采用继承`AbstractDAO 或 AbstractRepository`方式。<br>
<font color=red>注意：sql数据表中主键的DDL最好同下面一样。</font>
```ddl
`id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '表自增ID'
```
主键ID：bigint类型、无符号、自动递增、不能为NULL
> 其实这样做也符合了《Java开发手册》MySQL数据库-建表规约第九条：<br>
> ![建表规约第九条](介绍_files/建表规约第九条.png)

**AbstractDAO：**
```java
@Repository
public class DataJdbcExampleDAO extends AbstractDAO {

	@Override
	protected String tableName() {
		return "tableName";
	}
	
}
```

**AbstractRepository：**
```java
@Repository
public class DataJdbcExampleTDAO extends AbstractRepository<UserDO> {

	@Override
	protected String tableName() {
		return "user";
	}
	
}
```

## <font color=red>AbstractBaseDAO类速览</font>
```java
package ai.yue.library.data.jdbc.dao;

import ai.yue.library.data.jdbc.client.Db;
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
	 * <p><code style="color:red">依赖于接口传入 {@value DbConstant#PRIMARY_KEY} 参数时慎用此方法</code>，避免有序主键被遍历风险，造成数据越权行为。推荐使用 {@link #deleteByBusinessUk(String)}</p>
	 *
	 * @param id 主键id
	 */
	public void delete(Long id) {
		db.delete(tableName(), id);
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
	 * @param paramJson 更新所用到的参数（包含业务键字段）
	 */
	public void updateByBusinessUk(JSONObject paramJson) {
		db.updateByBusinessUk(tableName(), paramJson);
	}

	/**
	 * 单个
	 *
	 * @param id 主键id
	 * @return 可以是一个正确的单行查询结果、或null、或查询结果是多条数据而引发的预期错误异常
	 */
	public abstract T get(Long id);

	/**
	 * 单个-By业务键
	 * <p>默认业务键为key
	 * <p>业务键值推荐使用UUID5
	 *
	 * @param businessUkValue 业务键的唯一值
	 * @return 可以是一个正确的单行查询结果、或null、或查询结果是多条数据而引发的预期错误异常
	 */
	public abstract T getByBusinessUk(String businessUkValue);

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
```

[👉点击前往源码仓库查看](https://gitee.com/yl-yue/yue-library/blob/master/yue-library-data-jdbc/src/main/java/ai/yue/library/data/jdbc/dao/AbstractBaseDAO.java)

### <font color=red>AbstractBaseDAO JSONObject 实现 AbstractDAO 类速览</font>
```java
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
	public JSONObject getByBusinessUk(String businessUkValue) {
		return db.getByBusinessUk(tableName(), businessUkValue);
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
```

[👉点击前往源码仓库查看](https://gitee.com/yl-yue/yue-library/blob/master/yue-library-data-jdbc/src/main/java/ai/yue/library/data/jdbc/dao/AbstractDAO.java)

### <font color=red>AbstractBaseDAO 泛型实现 AbstractRepository 类速览</font>
```java
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
	public T get(Long id) {
		return db.getById(tableName(), id, mappedClass);
	}

	@Override
	public T getByBusinessUk(String businessUkValue) {
		return db.getByBusinessUk(tableName(), businessUkValue, mappedClass);
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
```

[👉点击前往源码仓库查看](https://gitee.com/yl-yue/yue-library/blob/master/yue-library-data-jdbc/src/main/java/ai/yue/library/data/jdbc/dao/AbstractRepository.java)