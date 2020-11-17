# AbstractDAO
## 简单使用
`data-jdbc`所有的CRUD方法都在`Db`类里面，所以使用时只需要直接注入即可，推荐采用继承`AbstractDAO 或 DBRepository`方式。<br>
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

## <font color=red>AbstractDAO类速览</font>
`AbstractDAO`为 JSON 对象提供服务

实际中可能会遇到类型转换问题，可使用 `Convert` 类进行转换，支持DO、Json、List等相互转换

```java
package ai.yue.library.data.jdbc.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.data.jdbc.client.Db;
import ai.yue.library.data.jdbc.constant.SortEnum;
import ai.yue.library.data.jdbc.ipo.PageIPO;
import ai.yue.library.data.jdbc.vo.PageVO;

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
		return db.getById(tableName(), id);
	}
	
	/**
	 * 列表-全部
	 * @return 列表数据
	 */
	public List<JSONObject> listAll() {
		return db.listAll(tableName());
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
		return db.page(tableName(), pageIPO, SortEnum.DESC);
	}
	
}
```

[👉点击前往源码仓库查看](https://gitee.com/yl-yue/yue-library/blob/master/yue-library-data-jdbc/src/main/java/ai/yue/library/data/jdbc/dao/AbstractDAO.java)