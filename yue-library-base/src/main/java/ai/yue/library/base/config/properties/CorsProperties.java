package ai.yue.library.base.config.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

import lombok.Data;

/**
 * @author 	孙金川
 * @version 创建时间：2018年11月6日
 */
@Data
@ConfigurationProperties("yue.cors")
public class CorsProperties {
	
	/**
	 * 是否允许 <code style="color:red">跨域</code>
	 * <p>
	 * 默认：true
	 */
	private boolean allow = true;
	
	/**
	 * response允许暴露的Headers
	 * <p>
	 * 默认：{@linkplain CorsConfiguration#setExposedHeaders(List)}
	 * ({@code Cache-Control}, {@code Content-Language}, {@code Content-Type},
	 * {@code Expires}, {@code Last-Modified}, or {@code Pragma})
	 * 此外在此基础之上还额外的增加了一个名为 <b>token</b> 的Headers
	 */
	private List<String> exposedHeaders;
	
}
