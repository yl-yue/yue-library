package ai.yue.library.base.config.net.proxy;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 网络代理自动配置属性
 * <p>Java文档：https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/net/doc-files/net-properties.html
 * 
 * @author	ylyue
 * @since	2020年8月26日
 */
@Data
@ConfigurationProperties("yue.net.proxy")
public class NetProxyProperties {

	/**
	 * 是否启用 <code style="color:red">网络代理</code> 自动配置
	 * <p>默认：false
	 */
	private boolean enabled = false;
	
	// HTTP代理
	
	/**
	 * 启用http代理服务器进行http请求代理访问
	 * <p>默认：false
	 */
	private boolean httpServerEnabled = false;
	
	/**
	 * HTTP代理服务器Host
	 * <p>示例：localhost、127.0.0.1
	 */
	private String httpServerHost = "localhost";
	
	/**
	 * HTTP代理服务器Port
	 * <p>默认：80
	 */
	private int httpServerPort = 80;
	
	// HTTPS代理
	
	/**
	 * 启用https代理服务器进行https请求代理访问
	 * <p>默认：false
	 */
	private boolean httpsServerEnabled = false;
	
	/**
	 * HTTPS代理服务器Host
	 * <p>示例：localhost、127.0.0.1
	 */
	private String httpsServerHost = "localhost";
	
	/**
	 * HTTPS代理服务器Port
	 * <p>默认：443
	 */
	private int httpsServerPort = 443;
	
	// FTP代理
	
	/**
	 * 启用ftp代理服务器进行ftp请求代理访问
	 * <p>默认：false
	 */
	private boolean ftpServerEnabled = false;
	
	/**
	 * FTP代理服务器Host
	 * <p>示例：localhost、127.0.0.1
	 */
	private String ftpServerHost = "localhost";
	
	/**
	 * FTP代理服务器Port
	 * <p>默认：80
	 */
	private int ftpServerPort = 80;
	
	// SOCKS代理
	
	/**
	 * 启用socks代理服务器进行http、https、ftp、socket请求代理访问
	 * <p>默认：false
	 */
	private boolean socksServerEnabled = false;
	
	/**
	 * SOCKS代理服务器Host
	 * <p>示例：localhost、127.0.0.1
	 */
	private String socksServerHost = "localhost";
	
	/**
	 * SOCKS代理服务器Port
	 * <p>默认：1080
	 */
	private int socksServerPort = 1080;
	
	/**
	 * SOCKS代理协议版本
	 * <p>默认：5
	 */
	private int socksProxyVersion = 5;
	
	// 忽略代理
	
	/**
	 * 忽略代理地址，适用于发起HTTP、HTTPS、FTP、SOCKET请求时不进行代理访问（优先级高于其它配置）
	 * <p>使用 {@code |} 对多个地址进行分割，支持通配符配置，如：{@code 192.168.0.*|*.ylyue.cn}
	 * <p>默认：{@value NetProxy#DEF_NON_PROXY_VAL}
	 */
	private String nonProxyHosts = NetProxy.DEF_NON_PROXY_VAL;
	
	/**
	 * 额外忽略地址（适用于不想覆盖默认值进行配置）
	 * <p>使用 {@code |} 对多个地址进行分割，支持通配符配置，如：{@code 192.168.0.*|*.ylyue.cn}
	 */
	private String nonProxyHostsAdditional;
	
	// 其它属性
	
	/**
	 * 使用Basic认证方式，连接代理服务器的username
	 */
	private String basicAuthenticatorUsername;
	
	/**
	 * 使用Basic认证方式，连接代理服务器的password
	 */
	private String basicAuthenticatorPassword;
	
}
