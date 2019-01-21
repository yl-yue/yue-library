package ai.yue.library.base.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author 	孙金川
 * @version 创建时间：2018年11月6日
 */
@Data
@ConfigurationProperties("yue.rest")
public class RestProperties {
	
	/**
	 * 链接超时时间（单位：毫秒）
	 * <p>0 = 不超时
	 * <p>默认：系统“默认”的超时设置
	 */
	private Integer connectTimeout;
	
	/**
	 * 读取超时时间（单位：毫秒）
	 * <p>0 = 不超时
	 * <p>默认：系统“默认”的超时设置
	 */
	private Integer readTimeout;
	
}
