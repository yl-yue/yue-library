package ai.yue.library.web.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 全局错误拦截自动配置
 * <p>实现对Filter异常进行统一处理
 * 
 * @author	ylyue
 * @since	2020年9月16日
 */
@Slf4j
@Configuration
@Import({ ResultExceptionHandler.class, ResultResponseBodyHandler.class })
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
@EnableConfigurationProperties({ ServerProperties.class })
@ConditionalOnProperty(prefix = "yue.exception-handler", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ResultErrorAutoConfig {

	@Autowired
	ServerProperties serverProperties;
	
	/**
	 * 优先级高于 {@linkplain ErrorMvcAutoConfiguration#basicErrorController(ErrorAttributes, org.springframework.beans.factory.ObjectProvider)}
	 */
	@Bean
	public BasicErrorController basicErrorController(ErrorAttributes errorAttributes) {
		log.info("【初始化配置-全局统一异常处理】拦截所有Controller层异常，返回HTTP请求最外层对象 ... 已初始化完毕。");
		return new ResultErrorController(errorAttributes, this.serverProperties.getError());
	}
	
}
