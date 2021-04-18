package ai.yue.library.data.jdbc.client.dialect;

import ai.yue.library.base.constant.SortEnum;
import ai.yue.library.data.jdbc.config.properties.JdbcProperties;
import ai.yue.library.data.jdbc.constant.DbUpdateEnum;
import ai.yue.library.data.jdbc.ipo.Page;
import ai.yue.library.data.jdbc.ipo.PageIPO;
import ai.yue.library.data.jdbc.vo.PageVO;
import com.alibaba.fastjson.JSONObject;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.Nullable;

import java.io.Serializable;

/**
 * SQL方言
 * <p>由于不同数据库间SQL语句的差异，导致无法统一拼接SQL，Dialect接口旨在根据不同的数据库，使用不同的方言实现类，来拼接对应的SQL。
 * <p>设计模式借鉴于hutool-db
 * 
 * @author	ylyue
 * @since	2020年6月13日
 */
public interface Dialect extends Serializable, Cloneable {

	// Dialect

	/**
	 * 方言名
	 *
	 * @return 方言名
	 */
	DialectNameEnum dialectName();

	/**
	 * 克隆方言
	 *
	 * @return 克隆方言
	 */
	Dialect cloneDialect();

	/**
	 * 包装器
	 *
	 * @return 包装器
	 */
	Wrapper getWrapper();

	/**
	 * 设置包装器
	 * 
	 * @param wrapper 包装器
	 */
	void setWrapper(Wrapper wrapper);

	/**
	 * 命名参数JdbcTemplate
	 *
	 * @return 命名参数JdbcTemplate
	 */
	NamedParameterJdbcTemplate getNamedParameterJdbcTemplate();

	/**
	 * 设置命名参数JdbcTemplate
	 *
	 * @param namedParameterJdbcTemplate 命名参数JdbcTemplate
	 */
	void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate);

	/**
	 * Db可配置属性
	 *
	 * @return Db可配置属性
	 */
	JdbcProperties getJdbcProperties();

	/**
	 * 设置Db可配置属性
	 *
	 * @param jdbcProperties Db可配置属性
	 */
	void setJdbcProperties(JdbcProperties jdbcProperties);

	// insert

	/**
	 * <b>插入或更新</b>
	 * <i>表中必须存在数据唯一性约束</i>
	 * <p>更新触发条件：此数据若存在唯一性约束则更新，否则便执行插入数据
	 * <p><b>MySQL执行示例：</b><br>
	 * <code>INSERT INTO table (param1, param2, ...)</code><br>
	 * <code>VALUES</code><br>
	 * <code>(:param1, :param2, ...)</code><br>
	 * <code>ON DUPLICATE KEY UPDATE</code><br>
	 * <code>condition = condition + :condition, ...</code>
	 *
	 * @param tableName    表名
	 * @param paramJson    插入或更新所用到的参数
	 * @param conditions   更新条件（对应paramJson内的key值）
	 * @param dBUpdateEnum 更新类型 {@linkplain DbUpdateEnum}
	 * @return 受影响的行数
	 */
	Long insertOrUpdate(String tableName, JSONObject paramJson, String[] conditions, DbUpdateEnum dBUpdateEnum);
	
	// Page
	
	/**
	 * 获得用于SQL字符串拼接的SQL分页字符串
	 * 
	 * @return 用于SQL字符串拼接的SQL分页字符串（带具名参数的SQL字符串，非SQL拼接）
	 */
	String getPageJoinSql();
	
	/**
	 * 转换为经过方言处理的分页查询参数，用于SQL分页查询
	 *
	 * @param pageIPO 分页查询参数
	 * @return 经过方言处理的分页查询参数
	 */
	Page toPage(PageIPO pageIPO);

	/**
	 * <b>单表分页查询</b><br><br>
	 * <p>阿里最优SQL示例：</p>
	 * <code>SELECT a.* FROM 表1 a, (select id from 表1 where 条件 ORDER BY id LIMIT 100000,20 ) b where a.id=b.id</code><br><br>
	 *
	 * @param <T>         泛型
	 * @param tableName   表名
	 * @param pageIPO     分页查询参数 {@linkplain PageIPO}，所有的条件参数，都将以等于的形式进行SQL拼接
	 * @param sortEnum    排序方式 {@linkplain SortEnum}
	 * @param mappedClass 映射类
	 * @return count（总数），data（分页列表数据）
	 */
    <T> PageVO<T> page(String tableName, PageIPO pageIPO, SortEnum sortEnum, Class<T> mappedClass);

	/**
	 * <b>单表分页查询</b><br><br>
	 * <p>阿里最优SQL示例：</p>
	 * <code>SELECT a.* FROM 表1 a, (select id from 表1 where 条件 LIMIT 100000,20 ) b where a.id=b.id</code><br><br>
	 *
	 * @param <T>         泛型
	 * @param tableName   表名
	 * @param whereSql    自定义WHERE语句，若此参数为空，那么所有的条件参数，都将以等于的形式进行SQL拼接。<br><i>SQL示例：</i>
	 *                    <code> WHERE 条件</code>
	 * @param pageIPO     分页查询参数 {@linkplain PageIPO}
	 * @param mappedClass 映射类
	 * @return count（总数），data（分页列表数据）
	 */
    <T> PageVO<T> pageWhere(String tableName, String whereSql, PageIPO pageIPO, Class<T> mappedClass);

	/**
	 * <b>复杂SQL分页查询</b><br><br>
	 * <p>阿里最优查询SQL示例：</p>
	 * <code>SELECT a.* FROM 表1 a, (select id from 表1 where 条件 LIMIT :page, :limit) b where a.id=b.id</code><br><br>
	 *
	 * @param <T>         泛型
	 * @param querySql    用于查询数据的sql语句
	 * @param pageIPO     分页查询参数 {@linkplain PageIPO}
	 * @param mappedClass 映射类
	 * @return count（总数），data（分页列表数据）
	 */
    <T> PageVO<T> pageSql(String querySql, PageIPO pageIPO, Class<T> mappedClass);

	/**
	 * <b>复杂SQL分页查询</b><br><br>
	 * <p>统计SQL示例：</p>
	 * <code>SELECT count(*) count FROM 表1 a, (select id from 表1 where 条件) b where a.id=b.id</code><br>
	 * <p>阿里最优查询SQL示例：</p>
	 * <code>SELECT a.* FROM 表1 a, (select id from 表1 where 条件 LIMIT :page, :limit) b where a.id=b.id</code><br><br>
	 *
	 * @param countSql    用于统计总数的sql语句 <i>（注意：count(*)必须拥有count别名）</i> 同时countSql可以为null表示不统计 <b>可选参数</b>
	 * @param querySql    用于查询数据的sql语句
	 * @param pageIPO     分页查询参数 {@linkplain PageIPO}
	 * @param mappedClass 映射类
	 * @return count（总数），data（分页列表数据）
	 */
	<T> PageVO<T> pageSql(@Nullable String countSql, String querySql, PageIPO pageIPO, Class<T> mappedClass);

}
