package ai.yue.library.web.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import ai.yue.library.base.constant.FieldNamingStrategyEnum;
import lombok.Data;

/**
 * Jackson HTTP消息转换器配置
 * 
 * @author	ylyue
 * @since	2020年4月6日
 */
@Data
@ConfigurationProperties("yue.web.http-message-converter.jackson")
public class JacksonHttpMessageConverterProperties {
	
	/**
	 * 启用yue-library对Jackson进行增强配置
	 * <p>Jackson是SpringBoot默认的Json解析器
	 * <p>默认：false
	 */
	private boolean enabled = false;
	
	/**
	 * 字段命名策略
	 */
	private FieldNamingStrategyEnum fieldNamingStrategy;
	
	/**
	 * 输出Null值为空字符串
	 * <p>默认：true
	 */
	private boolean writeNullAsStringEmpty = true;
	
	/**
	 * Null String 输出为空字符串
	 * <p>默认：true
	 */
	private boolean WriteNullStringAsEmpty = true;
	
	/**
	 * 输出 Null Map 为 {}
	 * <p>默认：true
	 */
	private boolean writeNullMapAsEmpty = true;
	
	/**
	 * Null List 输出为 []
	 * <p>默认：true
	 */
	private boolean WriteNullListAsEmpty = true;
	
	/**
	 * 输出 Null Array 为 []
	 * <p>默认：true
	 */
	private boolean writeNullArrayAsEmpty = true;
	
	/**
	 * Null Boolean 输出为 false
	 * <p>默认：false
	 */
	private boolean WriteNullBooleanAsFalse = false;
	
	/**
	 * Null Number 输出为 0
	 * <p>默认：false
	 */
	private boolean WriteNullNumberAsZero = false;
	
}
