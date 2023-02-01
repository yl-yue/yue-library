package ai.yue.library.base.config;

import ai.yue.library.base.annotation.api.version.ApiVersionProperties;
import ai.yue.library.base.config.datetime.DateTimeFormatConfig;
import ai.yue.library.base.config.exception.ExceptionHandlerProperties;
import ai.yue.library.base.config.net.proxy.NetProxy;
import ai.yue.library.base.config.properties.CorsProperties;
import ai.yue.library.base.config.thread.pool.AsyncConfig;
import ai.yue.library.base.util.I18nUtils;
import ai.yue.library.base.util.SpringUtils;
import ai.yue.library.base.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * base bean 自动配置
 * 
 * @author	ylyue
 * @since	2018年11月26日
 */
@Slf4j
@Configuration
@Import({SpringUtils.class, I18nUtils.class, NetProxy.class, AsyncConfig.class, DateTimeFormatConfig.class})
@EnableConfigurationProperties({ApiVersionProperties.class, ExceptionHandlerProperties.class, CorsProperties.class,})
public class BaseAutoConfig {
	
	// Validator-校验器
	
	@Bean
	@ConditionalOnMissingBean
    public Validator validator(){
		log.info("【初始化配置-校验器】Bean：Validator ... 已初始化完毕。");
        return new Validator();
    }
	
}
