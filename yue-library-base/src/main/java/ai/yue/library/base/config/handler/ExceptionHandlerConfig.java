package ai.yue.library.base.config.handler;

import javax.annotation.PostConstruct;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.ControllerAdvice;

import ai.yue.library.base.handler.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author	孙金川
 * @since	2018年6月12日
 */
@Slf4j
@ControllerAdvice
@EnableConfigurationProperties(ExceptionHandlerProperties.class)
@ConditionalOnProperty(prefix = "yue.exception-handler", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ExceptionHandlerConfig extends GlobalExceptionHandler {
	
    @PostConstruct
    private void init() {
    	log.info("【初始化配置-全局统一异常处理】拦截所有Controller层异常，返回HTTP请求最外层对象 ... 已初始化完毕。");
    }
    
}
