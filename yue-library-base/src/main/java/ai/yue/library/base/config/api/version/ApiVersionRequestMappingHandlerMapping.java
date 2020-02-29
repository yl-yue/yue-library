package ai.yue.library.base.config.api.version;

import java.lang.reflect.Method;
import java.util.Objects;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author	ylyue
 * @since	2020年2月27日
 */
@Slf4j
@AllArgsConstructor
public class ApiVersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

	private ApiVersionProperties apiVersionProperties;
	
    @Override
	protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
    	// 扫描类或接口上的 {@link ApiVersion}
		ApiVersion apiVersion = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
		return createRequestCondition(apiVersion, handlerType);
	}
    
    @Override
	protected RequestCondition<?> getCustomMethodCondition(Method method) {
    	// 扫描方法上的 {@link ApiVersion}
		ApiVersion apiVersion = AnnotationUtils.findAnnotation(method, ApiVersion.class);
		return createRequestCondition(apiVersion, method.getDeclaringClass());
	}
	
	/**
	 * 确认版本占位符是否符合强制标准
	 * 
	 * @param handlerType Controller 类
	 * @return 是否符合强制标准
	 */
	private boolean isVersionPlaceholder(Class<?> handlerType) {
		RequestMapping requestMapping = AnnotationUtils.findAnnotation(handlerType, RequestMapping.class);
		if (requestMapping == null) {
			return false;
		}
		
		String[] value = requestMapping.value();
		if (StrUtil.isAllEmpty(value) || !value[0].contains(apiVersionProperties.getVersionPlaceholder())) {
			return false;
		}
		
		return true;
	}
    
	private RequestCondition<ApiVersionRequestCondition> createRequestCondition(ApiVersion apiVersion, Class<?> handlerType) {
		// 1. 确认是否进行版本控制
		if (Objects.isNull(apiVersion)) {
			return null;
		}
		
		// 2. 检测版本占位符是否符合强制标准
		if (!isVersionPlaceholder(handlerType)) {
			log.error("版本占位符，采用强标准模式，占位符只允许出现在最前列，{} 不满足规则。", handlerType);
			log.error("版本占位符，采用强标准模式，占位符只允许出现在最前列，详细规则说明请查看：{}", ApiVersionProperties.class);
			return null;
		}
        
		// 3. 确认是否满足最低版本（v1）要求
		double value = apiVersion.value();
		Assert.isTrue(value >= 1, "Api Version Must be greater than or equal to 1");
		
		// 4. 创建 RequestCondition
		return new ApiVersionRequestCondition(apiVersion, apiVersionProperties);
	}
    
}
