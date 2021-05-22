package ai.yue.library.base.config.net.proxy;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * 网络代理
 *
 * @author ylyue
 * @since 2020年8月26日
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(NetProxyProperties.class)
@ConditionalOnProperty(prefix = "yue.net.proxy", name = "enabled", havingValue = "true")
public class NetProxy {

    // HTTP代理
    public static final String HTTP_PROXY_HOST = "http.proxyHost";
    public static final String HTTP_PROXY_PORT = "http.proxyPort";

    // HTTPS代理
    public static final String HTTPS_PROXY_HOST = "https.proxyHost";
    public static final String HTTPS_PROXY_PORT = "https.proxyPort";

    // FTP代理
    public static final String FTP_PROXY_HOST = "ftp.proxyHost";
    public static final String FTP_PROXY_PORT = "ftp.proxyPort";

    // SOCKS5代理
    public static final String SOCKS_PROXY_HOST = "socksProxyHost";
    public static final String SOCKS_PROXY_PORT = "socksProxyPort";
    public static final String SOCKS_PROXY_VERSION = "socksProxyVersion";

    // 忽略代理
    public static final String FTP_NON_PROXY_HOSTS = "ftp.nonProxyHosts";
    public static final String HTTP_NON_PROXY_HOSTS = "http.nonProxyHosts";
    public static final String SOCKET_NON_PROXY_HOSTS = "socksNonProxyHosts";

    // 系统属性
    public static final String USE_SYSTEM_PROXIES = "java.net.useSystemProxies";
    public static final String DEF_NON_PROXY_VAL = "localhost|127.*|[::1]|0.0.0.0|[::0]";
    public static final String ADD_NON_PROXY_VAL = "10.*|172.16.*|172.17.*|172.18.*|172.19.*|172.20.*|172.21.*|172.22.*|172.23.*|172.24.*|172.25.*|172.26.*|172.27.*|172.28.*|172.29.*|172.30.*|172.31.*|192.168.*";

    @Autowired
    NetProxyProperties netProxyProperties;

    /**
     * 使用系统代理
     * <p>在Windows系统、macOS系统和Gnome系统上，可以告诉java.net堆栈，将该属性设置为true，以使用系统代理设置(这两个系统都允许通过它们的用户界面全局设置代理)。注意，此属性只在启动时检查一次。
     */
    public static void useSystemProxies() {
        System.setProperty(USE_SYSTEM_PROXIES, "true");
    }

    @PostConstruct
    private void init() {
        String basicAuthenticatorUsername = netProxyProperties.getBasicAuthenticatorUsername();
        String basicAuthenticatorPassword = netProxyProperties.getBasicAuthenticatorPassword();
        if (StrUtil.isNotEmpty(basicAuthenticatorUsername) && StrUtil.isNotEmpty(basicAuthenticatorPassword)) {
            // 设置登陆到代理服务器的用户名和密码
            Authenticator.setDefault(new BasicAuthenticator(basicAuthenticatorUsername, basicAuthenticatorPassword));
        }

        if (netProxyProperties.isHttpServerEnabled()) {
            httpServerEnable();
        }

        if (netProxyProperties.isHttpsServerEnabled()) {
            httpsServerEnable();
        }

        if (netProxyProperties.isFtpServerEnabled()) {
            ftpServerEnable();
        }

        if (netProxyProperties.isSocksServerEnabled()) {
            socksServerEnable();
        }

        String nonProxyHosts = netProxyProperties.getNonProxyHosts();
        String nonProxyHostsAdditional = netProxyProperties.getNonProxyHostsAdditional();
        // 适配`,`分割
        if (StrUtil.isNotEmpty(nonProxyHosts) && nonProxyHosts.contains(",")) {
            nonProxyHosts = nonProxyHosts.replace(",", "|");
        }
        if (StrUtil.isNotEmpty(nonProxyHostsAdditional)) {
            if (nonProxyHostsAdditional.contains(",")) {
                nonProxyHostsAdditional = nonProxyHostsAdditional.replace(",", "|");
            }
            nonProxyHosts = String.join("|", nonProxyHosts, nonProxyHostsAdditional);
        }
        System.setProperty(HTTP_NON_PROXY_HOSTS, nonProxyHosts);
        System.setProperty(FTP_NON_PROXY_HOSTS, nonProxyHosts);
        System.setProperty(SOCKET_NON_PROXY_HOSTS, nonProxyHosts);

        log.info("【初始化配置-全局网络代理】Java全局网络代理已开启，已知不会采用Java全局网络代理进行通信的三方库有：aliyun-java-sdk-core ...");
    }

    public void httpServerEnable() {
        System.setProperty(HTTP_PROXY_HOST, netProxyProperties.getHttpServerHost());
        System.setProperty(HTTP_PROXY_PORT, String.valueOf(netProxyProperties.getHttpServerPort()));
        log.info("【全局网络代理】已启用http代理服务器进行http请求代理访问 ...");
    }

    public void httpServerClose() {
        System.clearProperty(HTTP_PROXY_HOST);
        log.info("【全局网络代理】已断开http代理服务器，http请求将不再进行代理访问 ...");
    }

    public void httpsServerEnable() {
        System.setProperty(HTTPS_PROXY_HOST, netProxyProperties.getHttpsServerHost());
        System.setProperty(HTTPS_PROXY_PORT, String.valueOf(netProxyProperties.getHttpsServerPort()));
        log.info("【全局网络代理】已启用https代理服务器进行https请求代理访问 ...");
    }

    public void httpsServerClose() {
        System.clearProperty(HTTPS_PROXY_HOST);
        log.info("【全局网络代理】已断开https代理服务器，https请求将不再进行代理访问 ...");
    }

    public void ftpServerEnable() {
        System.setProperty(FTP_PROXY_HOST, netProxyProperties.getFtpServerHost());
        System.setProperty(FTP_PROXY_PORT, String.valueOf(netProxyProperties.getFtpServerPort()));
        log.info("【全局网络代理】已启用ftp代理服务器进行ftp请求代理访问 ...");
    }

    public void ftpServerClose() {
        System.clearProperty(FTP_PROXY_HOST);
        log.info("【全局网络代理】已断开ftp代理服务器，ftp请求将不再进行代理访问 ...");
    }

    public void socksServerEnable() {
        System.setProperty(SOCKS_PROXY_HOST, netProxyProperties.getSocksServerHost());
        System.setProperty(SOCKS_PROXY_PORT, String.valueOf(netProxyProperties.getSocksServerPort()));
        System.setProperty(SOCKS_PROXY_VERSION, String.valueOf(netProxyProperties.getSocksProxyVersion()));
        log.info("【全局网络代理】已启用socks代理服务器进行http、https、ftp、socket请求代理访问 ...");
    }

    public void socksServerClose() {
        System.clearProperty(SOCKS_PROXY_HOST);
        log.info("【全局网络代理】已断开socks代理服务器，http、https、ftp、socket请求将不再进行代理访问 ...");
    }

    static class BasicAuthenticator extends Authenticator {
        private String user = "";
        private String password = "";

        public BasicAuthenticator(String user, String password) {
            this.user = user;
            this.password = password;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(user, password.toCharArray());
        }
    }

}
