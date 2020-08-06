package ai.yue.library.web.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author	ylyue
 * @since	2020年4月6日
 */
@Data
@ConfigurationProperties("yue.web")
public class WebProperties {
	
	/**
	 * 启用FastJson优先于默认的Jackson做json解析
	 * <p>HTTP序列化时对null值进行初始化处理：<br>
	 * <code>
	 * 	SerializerFeature.WriteMapNullValue, 
	 * 	SerializerFeature.WriteNullBooleanAsFalse,
	 * 	SerializerFeature.WriteNullListAsEmpty, 
	 * 	SerializerFeature.WriteNullNumberAsZero,
	 * 	SerializerFeature.WriteNullStringAsEmpty
	 * </code>
	 * <p>默认：false
	 * @deprecated {@linkplain FastJsonHttpMessageConverterProperties#isEnabled()}
	 */
	@Deprecated
	private boolean enabledFastJsonHttpMessageConverter = false;
	
}
