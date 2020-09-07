package ai.yue.library.data.es.config;

import java.sql.SQLException;
import java.util.Properties;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.ClientConfiguration.MaybeSecureClientConfigurationBuilder;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

import com.amazon.opendistroforelasticsearch.jdbc.ElasticsearchDataSource;
import com.amazon.opendistroforelasticsearch.jdbc.config.HostnameVerificationConnectionProperty;
import com.amazon.opendistroforelasticsearch.jdbc.config.PasswordConnectionProperty;
import com.amazon.opendistroforelasticsearch.jdbc.config.TrustSelfSignedConnectionProperty;
import com.amazon.opendistroforelasticsearch.jdbc.config.UserConnectionProperty;

import ai.yue.library.base.config.net.http.SkipHostnameVerifier;
import ai.yue.library.base.config.net.http.SkipSslVerificationHttpRequestFactory;
import ai.yue.library.data.es.config.properties.EsRestProperties;
import ai.yue.library.data.es.config.properties.EsSqlProperties;
import ai.yue.library.data.jdbc.client.Db;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

/**
 * ES配置
 * 
 * @author	ylyue
 * @since	2020年8月3日
 */
@Configuration
@EnableConfigurationProperties({ EsRestProperties.class, EsSqlProperties.class })
public class ElasticsearchAutoConfig extends AbstractElasticsearchConfiguration {

	@Autowired
	EsRestProperties esRestProperties;
	@Autowired
	EsSqlProperties esSqlProperties;
	
	@Bean
	@ConditionalOnProperty(prefix = "yue.es.sql", name = "enabled", havingValue = "true")
	public Db esDb() throws SQLException {
		String url = "jdbc:elasticsearch://" + esSqlProperties.getUrl();

		ElasticsearchDataSource ds = new ElasticsearchDataSource();
		ds.setUrl(url);

		Properties properties = new Properties();
        properties.setProperty(TrustSelfSignedConnectionProperty.KEY, String.valueOf(esSqlProperties.isTrustSelfSigned()));
        properties.setProperty(HostnameVerificationConnectionProperty.KEY, String.valueOf(esSqlProperties.isHostnameVerification()));
        String username = esSqlProperties.getUsername();
        String password = esSqlProperties.getPassword();
        if (StrUtil.isAllNotEmpty(username, password)) {
        	properties.setProperty(UserConnectionProperty.KEY, username);
        	properties.setProperty(PasswordConnectionProperty.KEY, password);
        }
        ds.setProperties(properties);
		
        return new Db(ds);
	}
	
	@Bean
	@Primary
	@Override
	@ConditionalOnProperty(prefix = "yue.es.rest", name = "enabled", havingValue = "true")
    public RestHighLevelClient elasticsearchClient() {
		MaybeSecureClientConfigurationBuilder clientConfigurationBuilder = ClientConfiguration.builder()
				.connectedTo(ArrayUtil.toArray(esRestProperties.getHostAndPort(), String.class));
		if (esRestProperties.isUseSsl()) {
			boolean trustSelfSigned = esRestProperties.isTrustSelfSigned();
			boolean hostnameVerification = esRestProperties.isHostnameVerification();
			if (trustSelfSigned && !hostnameVerification) {
				clientConfigurationBuilder.usingSsl(SkipSslVerificationHttpRequestFactory.getSSLContext(), new SkipHostnameVerifier());
			} else if (trustSelfSigned) {
				clientConfigurationBuilder.usingSsl(SkipSslVerificationHttpRequestFactory.getSSLContext());
			} else {
				clientConfigurationBuilder.usingSsl();
			}
		}
		
		String username = esRestProperties.getUsername();
		String password = esRestProperties.getPassword();
		if (StrUtil.isNotEmpty(username) && StrUtil.isNotEmpty(password)) {
			clientConfigurationBuilder.withBasicAuth(username, password);
		}
		
		return RestClients.create(clientConfigurationBuilder.build()).rest();                   
    }
	
}
