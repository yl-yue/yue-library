package ai.yue.library.base.config.api.version;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.Data;

/**
 * @author	ylyue
 * @since	2018年11月6日
 */
@Data
@ConfigurationProperties("yue.api-version")
public class ApiVersionProperties {
	
	/**
	 * 是否启用 <code style="color:red">Restful API接口版本控制</code>
	 * <p>
	 * 默认：true
	 */
	private boolean enabled = true;
	
	/**
	 * 最小版本号，小于该版本号返回版本过时。
	 */
	private double minimumVersion;
    
	/**
	 * {@link RequestMapping} 版本占位符，采用强标准模式，占位符只允许出现在最前列，如下所示：
	 * <p>/{version}/user
	 * <p>/{version}/role
	 */
	private String versionPlaceholder = "{version}";
	
}
