package ai.yue.library.web.config;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.BeanContext;
import com.alibaba.fastjson.serializer.ContextValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import ai.yue.library.base.constant.FieldNamingStrategyEnum;
import ai.yue.library.base.util.ClassUtils;
import ai.yue.library.base.util.ListUtils;
import ai.yue.library.web.config.argument.resolver.ArrayArgumentResolver;
import ai.yue.library.web.config.argument.resolver.CustomRequestParamMethodArgumentResolver;
import ai.yue.library.web.config.argument.resolver.JavaBeanArgumentResolver;
import ai.yue.library.web.config.properties.FastJsonHttpMessageConverterProperties;
import ai.yue.library.web.config.properties.JacksonHttpMessageConverterProperties;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author	ylyue
 * @since	2020年4月5日
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Autowired
	FastJsonHttpMessageConverterProperties fastJsonProperties;
	@Autowired
	JacksonHttpMessageConverterProperties jacksonProperties;
	
	/**
	 * 扩展HTTP消息转换器做Json解析处理
	 */
	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		if (fastJsonProperties.isEnabled()) {
			// 使用FastJson优先于默认的Jackson做json解析
			// https://github.com/alibaba/fastjson/wiki/%E5%9C%A8-Spring-%E4%B8%AD%E9%9B%86%E6%88%90-Fastjson
			fastJsonHttpMessageConverterConfig(converters);
		} else if (jacksonProperties.isEnabled()) {
			// 启用yue-library对Jackson进行增强配置
			MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = ListUtils.get(converters, MappingJackson2HttpMessageConverter.class);
			mappingJackson2HttpMessageConverterConfig(mappingJackson2HttpMessageConverter);
		}
	}
	
	private void fastJsonHttpMessageConverterConfig(List<HttpMessageConverter<?>> converters) {
		// 1. 创建FastJsonHttpMessageConverter
		FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
		FastJsonConfig config = new FastJsonConfig();
		config.setDateFormat(JSON.DEFFAULT_DATE_FORMAT);
		config.setSerializerFeatures(fastJsonProperties.getSerializerFeatures());
		
		// 2. 配置FastJsonHttpMessageConverter规则
		ContextValueFilter contextValueFilter = (BeanContext context, Object object, String name, Object value) -> {
			if (context == null) {
				if (fastJsonProperties.isWriteNullAsStringEmpty()) {
					return StrUtil.EMPTY;
				}
				
				return value;
			}
			
			Class<?> fieldClass = context.getFieldClass();
			if (value != null || ClassUtils.isBasicType(fieldClass) || Collection.class.isAssignableFrom(fieldClass)) {
				return value;
			}
			
			if (fastJsonProperties.isWriteNullMapAsEmpty() && Map.class.isAssignableFrom(fieldClass)) {
				return new JSONObject();
			} else if (fastJsonProperties.isWriteNullArrayAsEmpty() && fieldClass.isArray()) {
				return ArrayUtil.newArray(0);
			}
			
			if (fastJsonProperties.isWriteNullAsStringEmpty()) {
				return StrUtil.EMPTY;
			}
			
			return value;
		};
		
		// 3. 配置FastJsonHttpMessageConverter
		if (fastJsonProperties.isWriteNullAsStringEmpty() || fastJsonProperties.isWriteNullMapAsEmpty()) {
			config.setSerializeFilters(contextValueFilter);
		}
		FieldNamingStrategyEnum fieldNamingStrategy = fastJsonProperties.getFieldNamingStrategy();
		if (fieldNamingStrategy != null) {
			config.getSerializeConfig().setPropertyNamingStrategy(fieldNamingStrategy.getPropertyNamingStrategy());
		}
		converter.setFastJsonConfig(config);
		converters.add(0, converter);
		log.info("【初始化配置-FastJsonHttpMessageConverter】默认配置为false，当前环境为true：使用FastJson优先于默认的Jackson做json解析 ... 已初始化完毕。");
	}
	
	private void mappingJackson2HttpMessageConverterConfig(MappingJackson2HttpMessageConverter converter) {
		ObjectMapper objectMapper = converter.getObjectMapper();
		JsonSerializer<Object> jsonSerializer = new JsonSerializer<Object>() {
			@Override
			public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				// 1. 确认currentValue是否有值
				JsonStreamContext outputContext = gen.getOutputContext();
				Object currentValue = outputContext.getCurrentValue();
				if (currentValue == null) {
					writeNullOrEmpty(gen);
					return;
				}
				
				// 2. 确认当前字段
				String currentName = outputContext.getCurrentName();
				Field field = null;
				try {
					field = ReflectUtil.getField(currentValue.getClass(), currentName);
				} catch (Exception e) {
					// 不做处理
				}
				if (field == null) {
					writeNullOrEmpty(gen);
					return;
				}
				
				// 3. 处理初始化类型
				Class<?> fieldClass = field.getType();
				if (jacksonProperties.isWriteNullStringAsEmpty() && CharSequence.class.isAssignableFrom(fieldClass)) {
					gen.writeString(StrUtil.EMPTY);
					return;
				} else if (jacksonProperties.isWriteNullNumberAsZero() && Number.class.isAssignableFrom(fieldClass)) {
					gen.writeNumber(0);
					return;
				} else if (jacksonProperties.isWriteNullBooleanAsFalse() && Boolean.class.isAssignableFrom(fieldClass)) {
					gen.writeBoolean(false);
					return;
				} else if (jacksonProperties.isWriteNullMapAsEmpty() && Map.class.isAssignableFrom(fieldClass)) {
					gen.writeStartObject();
					gen.writeEndObject();
					return;
				} else if (jacksonProperties.isWriteNullArrayAsEmpty() && (fieldClass.isArray() || Collection.class.isAssignableFrom(fieldClass))) {
					gen.writeStartArray();
					gen.writeEndArray();
					return;
				} else if (ClassUtils.isBasicType(fieldClass)) {
					gen.writeNull();
					return;
				}
				
				// 4. 其它类型处理
				writeNullOrEmpty(gen);
			}
			
			private void writeNullOrEmpty(JsonGenerator gen) throws IOException {
				if (jacksonProperties.isWriteNullAsStringEmpty()) {
					gen.writeString(StrUtil.EMPTY);
					return;
				}
				
				gen.writeNull();
			}
		};
		
		objectMapper.getSerializerProvider().setNullValueSerializer(jsonSerializer);
	}
	
	/**
	 * 添加自定义方法参数解析器
	 */
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new JavaBeanArgumentResolver());
		resolvers.add(new ArrayArgumentResolver(true));
		resolvers.add(new CustomRequestParamMethodArgumentResolver(true));
	}
	
}
