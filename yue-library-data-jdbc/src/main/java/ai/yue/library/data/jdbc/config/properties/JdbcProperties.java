package ai.yue.library.data.jdbc.config.properties;

import ai.yue.library.base.constant.FieldNamingStrategyEnum;
import ai.yue.library.base.convert.Convert;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

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
	 * <p>默认：key</p>
	 */
	private String businessUk = "key";

	/**
	 * 启用删除查询过滤
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
