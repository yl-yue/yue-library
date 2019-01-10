package ai.yue.library.base.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author 	孙金川
 * @version 创建时间：2018年11月6日
 */
@Data
@ConfigurationProperties("rest")
public class RestProperties {
	
	/**
	 * 读取超时时间（单位：毫秒）
	 * <p>
	 * 默认：5000
	 */
	private Integer readTimeout = 5000;
	
	/**
	 * 链接超时时间（单位：毫秒）
	 * <p>
	 * 默认：15000
	 */
	private Integer connectTimeout = 15000;
	
}
