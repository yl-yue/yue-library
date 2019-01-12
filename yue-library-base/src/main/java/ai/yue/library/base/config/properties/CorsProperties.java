package ai.yue.library.base.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author 	孙金川
 * @version 创建时间：2018年11月6日
 */
@Data
@ConfigurationProperties("yue.cors")
public class CorsProperties {
	
	/**
	 * 是否允许跨域
	 * <p>
	 * 默认：true
	 */
	private boolean allow = true;
	
}
