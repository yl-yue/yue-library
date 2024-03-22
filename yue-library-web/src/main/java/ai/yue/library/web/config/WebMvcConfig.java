package ai.yue.library.web.config;

import ai.yue.library.base.constant.FieldNamingStrategyEnum;
import ai.yue.library.base.util.ClassUtils;
import ai.yue.library.base.util.ListUtils;
import ai.yue.library.web.config.argument.resolver.ArrayArgumentResolver;
import ai.yue.library.web.config.argument.resolver.CustomRequestParamMethodArgumentResolver;
import ai.yue.library.web.config.argument.resolver.JavaBeanArgumentResolver;
import ai.yue.library.web.config.idempotent.IdempotentInterceptorRegistry;
import ai.yue.library.web.config.properties.FastJsonHttpMessageConverterProperties;
import ai.yue.library.web.config.properties.JacksonHttpMessageConverterProperties;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.filter.BeanContext;
import com.alibaba.fastjson2.filter.ContextNameFilter;
import com.alibaba.fastjson2.filter.ContextValueFilter;
import com.alibaba.fastjson2.filter.Filter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring.http.converter.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * WebMvc配置
 *
 * @author	ylyue
 * @since	2020年4月5日
 */
@Slf4j
@Configuration
@Import(IdempotentInterceptorRegistry.class)
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Autowired
	FastJsonHttpMessageConverterProperties fastJsonProperties;
	@Autowired
	JacksonHttpMessageConverterProperties jacksonProperties;
	@Autowired(required = false)
	IdempotentInterceptorRegistry idempotentInterceptorRegistry;

	/**
	 * 扩展HTTP消息转换器做Json解析处理
	 */
	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		if (fastJsonProperties.isEnabled()) {
			// 使用FastJson优先于默认的Jackson做json解析
			// https://github.com/alibaba/fastjson2/blob/main/docs/spring_support_cn.md
			fastJsonHttpMessageConverterConfig(converters);
		} else if (jacksonProperties.isEnabled()) {
			// 启用yue-library对Jackson进行增强配置
			MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = ListUtils.get(converters, MappingJackson2HttpMessageConverter.class);
			mappingJackson2HttpMessageConverterConfig(mappingJackson2HttpMessageConverter);
		}
	}
	
	private void fastJsonHttpMessageConverterConfig(List<HttpMessageConverter<?>> converters) {
		// 1. 创建消息转换器
		FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
		converter.setSupportedMediaTypes(CollUtil.newArrayList(MediaType.APPLICATION_JSON, new MediaType("application", "*+json")));
		FastJsonConfig config = new FastJsonConfig();
		config.setWriterFeatures(fastJsonProperties.getWriterFeatures());
		Filter[] writerFilters = config.getWriterFilters();
		List<Filter> writerFilterList = ListUtils.toList(writerFilters);

		// 2. 创建key拦截器
		FieldNamingStrategyEnum fieldNamingStrategy = fastJsonProperties.getFieldNamingStrategy();
		if (fieldNamingStrategy != null) {
			ContextNameFilter contextNameFilter = (context, object, name, value) -> fieldNamingStrategy.getPropertyNamingStrategy().fieldName(name);
			writerFilterList.add(contextNameFilter);
		}

		// 3. 创建value拦截器
		if (fastJsonProperties.isWriteNullAsStringEmpty() || fastJsonProperties.isWriteNullMapAsEmpty()) {
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

			writerFilterList.add(contextValueFilter);
		}

		// 4. 配置拦截器
		writerFilters = new Filter[writerFilterList.size()];
		writerFilters = writerFilterList.toArray(writerFilters);
		config.setWriterFilters(writerFilters);

		// 5. 配置消息转换器
		converter.setFastJsonConfig(config);
		converters.add(0, converter);
		log.info("【初始化配置-FastJsonHttpMessageConverter】默认配置为false，当前环境为true：使用FastJson优先于默认的Jackson做json解析 ... 已初始化完毕。");
	}
	
	private void mappingJackson2HttpMessageConverterConfig(MappingJackson2HttpMessageConverter converter) {
		ObjectMapper objectMapper = converter.getObjectMapper();
		JsonSerializer<Object> nullKeySerializer = new JsonSerializer<Object>() {
			@Override
			public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				nullSerializer(gen, StrUtil.NULL);
			}
		};
		JsonSerializer<Object> nullValueSerializer = new JsonSerializer<Object>() {
			@Override
			public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				nullSerializer(gen, StrUtil.EMPTY);
			}
		};
		SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
		serializerProvider.setNullKeySerializer(nullKeySerializer);
		serializerProvider.setNullValueSerializer(nullValueSerializer);
	}

	private void nullSerializer(JsonGenerator gen, String emptyValue) throws IOException {
		// 1. 确认currentValue是否有值
		JsonStreamContext outputContext = gen.getOutputContext();
		Object currentValue = outputContext.getCurrentValue();
		if (currentValue == null) {
			writeNullOrEmpty(gen, emptyValue);
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
			writeNullOrEmpty(gen, emptyValue);
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
		writeNullOrEmpty(gen, emptyValue);
	}

	private void writeNullOrEmpty(JsonGenerator gen, String emptyValue) throws IOException {
		if (jacksonProperties.isWriteNullAsStringEmpty()) {
			if (emptyValue.equals(StrUtil.NULL)) {
				gen.writeFieldName(emptyValue);
			} else {
				gen.writeString(emptyValue);
			}
			return;
		}

		gen.writeNull();
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

	/**
	 * 添加自定义拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 添加幂等性拦截器
		if (idempotentInterceptorRegistry != null) {
			idempotentInterceptorRegistry.registry(registry);
		}
	}

}
