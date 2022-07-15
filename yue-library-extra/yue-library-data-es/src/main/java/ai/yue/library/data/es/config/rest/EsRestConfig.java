package ai.yue.library.data.es.config.rest;

import ai.yue.library.base.config.net.http.SkipHostnameVerifier;
import ai.yue.library.base.config.net.http.SkipSslVerificationHttpRequestFactory;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
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

/**
 * ES REST 配置
 * 
 * @author	ylyue
 * @since	2020年8月3日
 */
@Configuration
@EnableConfigurationProperties(EsRestProperties.class)
@ConditionalOnProperty(prefix = "yue.es.rest", name = "enabled", havingValue = "true")
public class EsRestConfig extends AbstractElasticsearchConfiguration {

	@Autowired
	private EsRestProperties esRestProperties;
	
	@Bean
	@Primary
	@Override
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

		clientConfigurationBuilder.withConnectTimeout(esRestProperties.getConnectionTimeout());
		clientConfigurationBuilder.withSocketTimeout(esRestProperties.getReadTimeout());
		return RestClients.create(clientConfigurationBuilder.build()).rest();                   
    }
	
}
