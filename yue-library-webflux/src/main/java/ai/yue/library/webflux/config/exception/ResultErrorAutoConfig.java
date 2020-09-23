package ai.yue.library.webflux.config.exception;

import java.util.stream.Collectors;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.result.view.ViewResolver;

/**
 * 全局错误拦截自动配置
 * <p>实现对Filter异常进行统一处理
 * 
 * @author	ylyue
 * @since	2020年9月16日
 */
@Configuration
//@AutoConfigureBefore(ErrorWebFluxAutoConfiguration.class)
@Import({ ResultExceptionHandler.class })
@EnableConfigurationProperties({ ServerProperties.class })
public class ResultErrorAutoConfig {

	@Autowired
	ServerProperties serverProperties;
	
	/**
	 * 优先级高于 {@linkplain ErrorWebFluxAutoConfiguration#errorWebExceptionHandler(ErrorAttributes, ResourceProperties, ObjectProvider, ServerCodecConfigurer, ApplicationContext)}
	 */
	@Bean
	@Order(-2)
	public ErrorWebExceptionHandler errorWebExceptionHandler(ErrorAttributes errorAttributes,
			ResourceProperties resourceProperties, ObjectProvider<ViewResolver> viewResolvers,
			ServerCodecConfigurer serverCodecConfigurer, ApplicationContext applicationContext) {
		ResultErrorWebExceptionHandler exceptionHandler = new ResultErrorWebExceptionHandler(errorAttributes,
				resourceProperties, this.serverProperties.getError(), applicationContext);
		exceptionHandler.setViewResolvers(viewResolvers.orderedStream().collect(Collectors.toList()));
		exceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
		exceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
		return exceptionHandler;
	}
	
	/**
	 * 默认优先级高于 ResponseBodyResultHandler
	 */
	@Bean
	public ResultResponseBodyHandler resultResponseBodyHandler(ServerCodecConfigurer serverCodecConfigurer,
			@Qualifier("webFluxContentTypeResolver") RequestedContentTypeResolver contentTypeResolver,
			@Qualifier("webFluxAdapterRegistry") ReactiveAdapterRegistry reactiveAdapterRegistry) {
		return new ResultResponseBodyHandler(serverCodecConfigurer.getWriters(), contentTypeResolver,
				reactiveAdapterRegistry);
	}
	
}
