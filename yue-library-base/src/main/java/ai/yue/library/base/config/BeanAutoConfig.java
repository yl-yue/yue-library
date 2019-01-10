package ai.yue.library.base.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import ai.yue.library.base.config.properties.ConstantProperties;
import ai.yue.library.base.config.properties.RestProperties;

/**
 * @author	孙金川
 * @version 创建时间：2018年11月26日
 */
@Configuration
@EnableConfigurationProperties({ConstantProperties.class, RestProperties.class})
public class BeanAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public ClientHttpRequestFactory httpsRequestFactory(RestProperties restProperties){
    	HttpsRequestFactory factory = new HttpsRequestFactory();
        factory.setReadTimeout(restProperties.getReadTimeout());
        factory.setConnectTimeout(restProperties.getConnectTimeout());
        return factory;
    }
    
	@Bean
	@ConditionalOnMissingBean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory){
        return new RestTemplate(factory);
    }
	
}
