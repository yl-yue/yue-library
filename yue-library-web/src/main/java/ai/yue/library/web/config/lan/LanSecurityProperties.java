package ai.yue.library.web.config.lan;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * /lan/ 路径安全拦截配置
 * <p>
 * 默认仅允许内网IP访问，外网IP可通过 Basic Auth 或 IP 白名单扩展放行。
 * 配置前缀：yue.web.lan-security
 */
@ConfigurationProperties(prefix = LanSecurityProperties.PREFIX)
@Data
public class LanSecurityProperties {

	public static final String PREFIX = "yue.web.lan-security";

	/** 是否启用 /lan/ 路径安全拦截，默认启用 */
	private boolean enabled = true;

	/** 额外的IP白名单（CIDR格式，如 "10.0.0.0/8"），默认为空 */
	private List<String> ipWhitelistExtra = new ArrayList<>();

	/** Basic Auth 认证配置，外网IP访问 /lan/ 时生效 */
	private BasicAuth basicAuth = new BasicAuth();

	/**
	 * Basic Auth 认证配置
	 * <p>
	 * 启用后，外网IP需提供正确的用户名密码才能访问 /lan/ 路径。
	 * 未设置密码时，框架会自动生成随机密码并在启动日志中输出。
	 */
	@Data
	public static class BasicAuth {
		/** 是否启用 Basic Auth，默认关闭 */
		private boolean enabled = false;
		/** 认证用户名，默认 "admin" */
		private String username = "admin";
		/** 认证密码，为空时启动自动生成随机密码 */
		private String password;
	}
}
