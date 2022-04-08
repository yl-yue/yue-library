package ai.yue.library.data.jdbc.config.properties;

import ai.yue.library.base.constant.FieldNamingStrategyEnum;
import ai.yue.library.base.convert.Convert;
import ai.yue.library.data.jdbc.constant.EncryptAlgorithmEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * jdbc可配置属性
 * 
 * @author	ylyue
 * @since	2018年11月6日
 */
@Data
@ConfigurationProperties("yue.jdbc")
public class JdbcProperties implements Serializable, Cloneable {

	private static final long serialVersionUID = -2792479012600072153L;

	// ====================== 关键字段定义 ======================

	/**
	 * 关键字段定义-无序主键名（表中字段值推荐使用UUID5无符号位）
	 *
	 * <p>默认：uuid</p>
	 */
	private String fieldDefinitionUuid = "uuid";

	/**
	 * 关键字段定义-数据删除标识
	 *
	 * <p>默认：delete_time</p>
	 */
	private String fieldDefinitionDeleteTime = "delete_time";

	/**
	 * 关键字段定义-排序
	 *
	 * <p>默认：sort_idx</p>
	 */
	private String fieldDefinitionSortIdx = "sort_idx";

	// ====================== JDBC属性 ======================

	/**
	 * 启用删除查询过滤（只对自动生成的查询sql生效）
	 * <p>默认：false</p>
	 */
	private boolean enableDeleteQueryFilter = false;

	/**
	 * 启用数据库字段命名策略识别
	 * <p>默认：true
	 */
	private boolean enableFieldNamingStrategyRecognition = true;

	/**
	 * 数据库字段命名策略
	 * <p>默认：SNAKE_CASE
	 */
	private FieldNamingStrategyEnum databaseFieldNamingStrategy = FieldNamingStrategyEnum.SNAKE_CASE;
	
	/**
	 * 启用布尔值映射识别
	 *
	 * <p>
	 * 　实体属性名: tempUser<br>
	 * 　数据库字段：is_temp_user
	 * </p>
	 *
	 * <p>默认：true
	 */
	private boolean enableBooleanMapRecognition = true;

	/**
	 * 缺省数据加密算法（仅当在表级未配置单独的加密算法时，以缺省值的方式生效）
	 */
	private EncryptAlgorithmEnum dataEncryptAlgorithm = EncryptAlgorithmEnum.AES;

	/**
	 * 缺省数据加密密钥（仅当在表级未配置单独的加密密钥时，以缺省值的方式生效）
	 */
	private String dataEncryptKey;

	/**
	 * 数据加密配置
	 * <p>
	 *     key：表名<br>
	 *     value：数据加密规则
	 * </p>
	 */
	private Map<String, DataEncryptProperties> dataEncryptConfigs;

	/**
	 * 数据审计属性
	 */
	private DataAuditProperties dataAuditProperties = new DataAuditProperties();

	/**
	 * 数据审计表名
	 */
	private List<String> dataAuditTableNames;

	@Override
	public JdbcProperties clone() {
		JdbcProperties jdbcProperties = null;
		try {
			jdbcProperties = (JdbcProperties) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		return Convert.toJavaBean(Convert.toJSONObject(jdbcProperties), getClass());
	}

}
