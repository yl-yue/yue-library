package ai.yue.library.web.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

import ai.yue.library.web.config.argument.resolver.JavaBeanArgumentResolver;
import ai.yue.library.web.config.properties.WebProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * @author	ylyue
 * @since	2020年4月5日
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Autowired
	WebProperties webProperties;
	
	/**
	 * <p>使用FastJson优先于默认的Jackson做json解析
	 * <p>https://github.com/alibaba/fastjson/wiki/%E5%9C%A8-Spring-%E4%B8%AD%E9%9B%86%E6%88%90-Fastjson
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		// 1. 确认是否启用FastJsonHttpMessageConverter
		if (!webProperties.isEnabledFastJsonHttpMessageConverter()) {
			return;
		}
		
		// 2. 配置FastJsonHttpMessageConverter
		FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
		FastJsonConfig config = new FastJsonConfig();
		config.setDateFormat(JSON.DEFFAULT_DATE_FORMAT);
		config.setSerializerFeatures(
				SerializerFeature.PrettyFormat, 
				SerializerFeature.BrowserCompatible, 
				SerializerFeature.WriteMapNullValue, 
				SerializerFeature.WriteNullBooleanAsFalse,
				SerializerFeature.WriteNullListAsEmpty, 
				SerializerFeature.WriteNullNumberAsZero,
				SerializerFeature.WriteNullStringAsEmpty
		);
		converter.setFastJsonConfig(config);
		converters.add(0, converter);
		
		log.info("【初始化配置-FastJsonHttpMessageConverter】默认配置为false，当前环境为true：使用FastJson优先于默认的Jackson做json解析 ... 已初始化完毕。");
	}
    
	/**
	 * 添加自定义方法参数解析器
	 */
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new JavaBeanArgumentResolver());
	}
	
}
