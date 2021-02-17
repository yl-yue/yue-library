package ai.yue.library.data.jdbc.client.dialect;

import ai.yue.library.base.constant.SortEnum;
import ai.yue.library.data.jdbc.config.properties.JdbcProperties;
import ai.yue.library.data.jdbc.constant.DbUpdateEnum;
import ai.yue.library.data.jdbc.dto.PageDTO;
import ai.yue.library.data.jdbc.ipo.Page;
import ai.yue.library.data.jdbc.ipo.PageIPO;
import ai.yue.library.data.jdbc.vo.PageBeforeAndAfterVO;
import com.alibaba.fastjson.JSONObject;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

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
     * <h2>插入或更新</h2>
     * <i>表中必须存在数据唯一性约束</i>
     * <p>更新触发条件：此数据若存在唯一性约束则更新，否则便执行插入数据
     * <p><b>MySQL执行示例：</b><br>
     * <code>INSERT INTO table (param1, param2, ...)</code><br>
     * <code>VALUES</code><br>
     * <code>(:param1, :param2, ...)</code><br>
     * <code>ON DUPLICATE KEY UPDATE</code><br>
     * <code>condition = condition + :condition, ...</code>
     * @param tableName		表名
     * @param paramJson		插入或更新所用到的参数
     * @param conditions	更新条件（对应paramJson内的key值）
     * @param dBUpdateEnum	更新类型 {@linkplain DbUpdateEnum}
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
	 * 转换为分页查询对象
	 * 
	 * @param pageIPO
	 * @return 分页查询对象
	 */
	Page toPage(PageIPO pageIPO);
	
	/**
	 * 转换为Db参数Json
	 * 
	 * @param pageIPO 分页请求对象
	 * @return paramJson
	 */
	JSONObject toParamJson(PageIPO pageIPO);
	
    PageDTO pageDTOBuild(String tableName, PageIPO pageIPO, SortEnum sortEnum);
    
    PageDTO pageDTOBuild(String tableName, String whereSql, PageIPO pageIPO);
    
    PageDTO pageDTOBuild(String querySql, PageIPO pageIPO);
    
    /**
     * <b>根据相同的列表条件，获得上一条与下一条数据</b>
     * 
     * @param querySql 			用于查询数据的sql语句
     * @param pageIPO 			分页查询参数 {@linkplain PageIPO}
     * @param equalsId			做比较的条件ID（将与查询结果的主键ID做比较）
     * @return {@linkplain PageBeforeAndAfterVO}
     */
	PageBeforeAndAfterVO pageBeforeAndAfter(String querySql, PageIPO pageIPO, Long equalsId);
	
	// Update

    String updateSqlBuild(String tableName, JSONObject paramJson, String[] conditions, DbUpdateEnum dBUpdateEnum);
    
}
