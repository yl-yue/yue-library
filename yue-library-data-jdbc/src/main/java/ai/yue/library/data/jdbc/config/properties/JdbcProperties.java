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

	/**
	 * 业务唯一键
	 * <p>默认：bkey</p>
	 */
	private String businessUk = "bkey";

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
	 * 数据加密算法
	 */
	private EncryptAlgorithmEnum dataEncryptAlgorithm = EncryptAlgorithmEnum.AES;

	/**
	 * 数据加密密钥
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
	 * JDBC审计表名
	 */
	private List<String> auditTableNames;

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
