package ai.yue.library.test.doc.example.base;

import ai.yue.library.base.config.net.proxy.NetProxy;
import ai.yue.library.base.config.net.proxy.NetProxyProperties;
import ai.yue.library.test.TestApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;

/**
 * 网络代理文档
 *
 * @author ylyue
 * @since 2020年10月29日
 */
public class NetProxyDocs {

	// 使用系统代理

	public static void main(String[] args) throws Exception {
		NetProxy.useSystemProxies();// 使用系统代理需要在项目启动之前进行设置
		SpringApplication.run(TestApplication.class, args);
	}

	// 动态开启与关闭代理

	@Autowired
	NetProxy netProxy;

	public void use() {
		netProxy.httpServerEnable();
		netProxy.httpServerClose();
		netProxy.socksServerEnable();
		netProxy.socksServerClose();
		// https、ftp ...
	}

	// 获取代理配置

	@Autowired
	NetProxyProperties netProxyProperties;

	public void getProxyConfig() {
		netProxyProperties.getSocksServerHost();
		netProxyProperties.getSocksServerPort();
		netProxyProperties.getSocksProxyVersion();
		netProxyProperties.getNonProxyHosts();
		netProxyProperties.getNonProxyHostsAdditional();
		netProxyProperties.getBasicAuthenticatorUsername();
		netProxyProperties.getBasicAuthenticatorPassword();
		// http、https、ftp ...
	}

}
