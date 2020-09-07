package ai.yue.library.data.es.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * ES SQL 可配置属性定义
 * 
 * @author	ylyue
 * @since	2020年8月28日
 */
@Data
@ConfigurationProperties("yue.es.sql")
public class EsSqlProperties {

	/**
	 * 是否启用 <code style="color:red">Elasticsearch Db</code> 自动配置
	 * <p>提供Bean esDb
	 * <p>默认：false
	 */
	private boolean enabled = false;
	/**
	 * https://localhost:80
	 */
	private String url;
	/**
	 * 信任自签证书
	 */
	private boolean trustSelfSigned = true;
	/**
	 * hostname验证
	 */
	private boolean hostnameVerification = false;
	private String username;
	private String password;
    
}
