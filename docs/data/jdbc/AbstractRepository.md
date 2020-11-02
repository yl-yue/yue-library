## AbstractRepository
### 简单使用
`data-jdbc`所有的CRUD方法都在`Db`类里面，所以使用时只需要直接注入即可，推荐采用继承`AbstractDAO 或 AbstractRepository`方式。<br>
<font color=red>注意：sql数据表中主键的DDL最好同下面一样。</font>
```ddl
`id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '表自增ID'
```
主键ID：bigint类型、无符号、自动递增、不能为NULL
> 其实这样做也符合了《Java开发手册》MySQL数据库-建表规约第九条：<br>
> ![建表规约第九条](介绍_files/建表规约第九条.png)

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

### <font color=red>AbstractRepository类速览</font>
`AbstractRepository`为 DO 对象提供服务，字段映射支持下划线与驼峰自动识别转换

实际中可能会遇到类型转换问题，可使用 `Convert` 类进行转换，支持DO、Json、List等相互转换

```java
package ai.yue.library.data.jdbc.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;

import ai.yue.library.base.constant.FieldNamingStrategyEnum;
import ai.yue.library.base.convert.Convert;
import ai.yue.library.data.jdbc.client.Db;
import ai.yue.library.data.jdbc.config.properties.JdbcProperties;
import ai.yue.library.data.jdbc.constant.DbSortEnum;
import ai.yue.library.data.jdbc.ipo.PageIPO;
import ai.yue.library.data.jdbc.vo.PageTVO;
import cn.hutool.core.util.ClassUtil;

/**
 * AbstractRepository 为 DO 对象提供服务，字段映射支持下划线与驼峰自动识别转换
 * 
 * @author	ylyue
 * @since	2019年4月30日
 * @param <T> 映射类
 */
public abstract class AbstractRepository<T> {

	@Autowired
	protected Db db;
	@Autowired
	private JdbcProperties jdbcProperties;
	@SuppressWarnings("unchecked")
	protected Class<T> mappedClass = (Class<T>) ClassUtil.getTypeArgument(getClass());
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
	 * 插入数据-实体
	 * <p>默认进行 {@link FieldNamingStrategyEnum#SNAKE_CASE} 数据库字段命名策略转换
	 * 
	 * @param paramIPO 参数IPO（POJO-IPO对象）
	 * @return 返回主键值
	 */
	public Long insert(Object paramIPO) {
		if (jdbcProperties.isEnableFieldNamingStrategyRecognition()) {
			return insert(paramIPO, jdbcProperties.getDatabaseFieldNamingStrategy());
		}
		
		return insert(Convert.toJSONObject(paramIPO));
	}
    
	/**
	 * 插入数据-实体
	 * 
	 * @param paramIPO 参数IPO（POJO-IPO对象）
	 * @param databaseFieldNamingStrategyEnum 数据库字段命名策略
	 * @return 返回主键值
	 */
	public Long insert(Object paramIPO, FieldNamingStrategyEnum databaseFieldNamingStrategyEnum) {
		PropertyNamingStrategy propertyNamingStrategy = databaseFieldNamingStrategyEnum.getPropertyNamingStrategy();
		SerializeConfig serializeConfig = new SerializeConfig();
		serializeConfig.setPropertyNamingStrategy(propertyNamingStrategy);
		JSONObject paramJson = (JSONObject) JSONObject.toJSON(paramIPO, serializeConfig);
		return insert(paramJson);
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
	 * @param id 主键ID
	 * @return POJO对象
	 */
	public T get(Long id) {
		return db.getById(tableName(), id, mappedClass);
	}
	
	/**
	 * 列表-全部
	 * @return 列表数据
	 */
	public List<T> listAll() {
		return db.listAll(tableName(), mappedClass);
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
		return db.page(tableName(), pageIPO, mappedClass, DbSortEnum.DESC);
	}
	
}
```

[👉点击前往源码仓库查看](https://gitee.com/yl-yue/yue-library/blob/master/yue-library-data-jdbc/src/main/java/ai/yue/library/data/jdbc/dao/AbstractRepository.java)