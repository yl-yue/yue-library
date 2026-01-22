package ai.yue.library.base.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

/**
 * base可配置属性
 *
 * @author	ylyue
 * @since	2018年11月6日
 */
@Data
@ConfigurationProperties("yue.base")
public class BaseProperties {

	/**
	 * i18语言（详见官网文档中的Accept-Language参数对照表）
	 * <p>
	 * 默认：zh-CN
	 * </p>
	 */
	private String i18nLanguage = "zh-CN";

	/**
	 * 跨域
	 */
	private final CorsProperties cors = new CorsProperties();

	/**
	 * RESTful API接口版本定义
	 */
	private final ApiVersionProperties apiVersion = new ApiVersionProperties();

	/**
	 * 跨域
	 */
	@Data
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

	/**
	 * RESTful API接口版本定义自动配置属性
	 */
	@Data
	public class ApiVersionProperties {

		/**
		 * 是否启用 <code style="color:red">RESTful API接口版本控制</code>
		 * <p>
		 * 默认：true
		 */
		private boolean enabled = true;

		/**
		 * 最小版本号，小于该版本号返回版本过时。
		 */
		private double minimumVersion;

		/**
		 * {@link RequestMapping} 版本占位符，如下所示：
		 * <p>/{version}/user
		 * <p>/user/{version}
		 */
		private String versionPlaceholder = "{version}";

	}

}
