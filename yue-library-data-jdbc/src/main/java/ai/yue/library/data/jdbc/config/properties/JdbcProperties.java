package ai.yue.library.data.jdbc.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import ai.yue.library.data.jdbc.constant.DatabaseFieldNamingStrategyEnum;
import lombok.Data;

/**
 * jdbc可配置属性
 * 
 * @author	ylyue
 * @since	2018年11月6日
 */
@Data
@ConfigurationProperties("yue.jdbc")
public class JdbcProperties {
	
	/**
	 * 数据库字段命名策略
	 * <p>默认：SNAKE_CASE
	 */
	private DatabaseFieldNamingStrategyEnum databaseFieldNamingStrategy = DatabaseFieldNamingStrategyEnum.SNAKE_CASE;
	
	/**
	 * 数据库字段命名策略识别-是否开启
	 * <p>默认：true
	 */
	private boolean databaseFieldNamingStrategyRecognitionEnabled = true;
	
}
