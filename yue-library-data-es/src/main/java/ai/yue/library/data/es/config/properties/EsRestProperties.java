package ai.yue.library.data.es.config.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * ES REST 可配置属性定义
 * 
 * @author	ylyue
 * @since	2020年8月28日
 */
@Data
@ConfigurationProperties("yue.es.rest")
public class EsRestProperties {

	/**
	 * 是否启用 <code style="color:red">Elasticsearch RestHighLevelClient</code> 自动配置
	 * <p>默认：false
	 */
	private boolean enabled = false;
	/**
	 * localhost:80
	 */
	private List<String> hostAndPort;
	/**
	 * 开启HTTPS协议进行通信
	 * <p>默认：false
	 */
	private boolean useSsl = false;
	/**
	 * 信任自签证书
	 * <p>默认：true
	 */
	private boolean trustSelfSigned = true;
	/**
	 * hostname验证
	 * <p>默认：false
	 */
	private boolean hostnameVerification = false;
	private String username;
	private String password;
    
}
