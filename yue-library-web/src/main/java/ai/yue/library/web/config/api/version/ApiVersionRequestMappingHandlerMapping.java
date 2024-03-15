package ai.yue.library.web.config.api.version;

import ai.yue.library.base.annotation.api.version.ApiVersion;
import ai.yue.library.base.annotation.api.version.ApiVersionProperties;
import ai.yue.library.base.util.StrUtils;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * RESTful API接口版本控制
 * 
 * @author	ylyue
 * @since	2020年2月27日
 */
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
    
	private RequestCondition<ApiVersionRequestCondition> createRequestCondition(ApiVersion apiVersion, Class<?> handlerType) {
		// 1. 确认是否进行版本控制-ApiVersion注解不为空
		if (Objects.isNull(apiVersion)) {
			return null;
		}
		
		// 2. 确认是否进行版本控制-RequestMapping注解包含版本占位符
		RequestMapping requestMapping = AnnotationUtils.findAnnotation(handlerType, RequestMapping.class);
		if (requestMapping == null) {
			return null;
		}
		
		String[] requestMappingValues = requestMapping.value();
		if (StrUtil.isAllEmpty(requestMappingValues) || !requestMappingValues[0].contains(apiVersionProperties.getVersionPlaceholder())) {
			return null;
		}
		
		// 3. 解析版本占位符索引位置
		String[] versionPlaceholderValues = StrUtils.splitToArray(requestMappingValues[0], "/");
		Integer index = null;
		for (int i = 0; i < versionPlaceholderValues.length; i++) {
			if (StrUtils.equals(versionPlaceholderValues[i], apiVersionProperties.getVersionPlaceholder())) {
				index = i;
				break;
			}
		}
		
		// 4. 确认是否进行版本控制-占位符索引确认
		if (index == null) {
			return null;
		}
		
		// 5. 确认是否满足最低版本（v1）要求
		double value = apiVersion.value();
		Assert.isTrue(value >= 1, "Api Version Must be greater than or equal to 1");
		
		// 6. 创建 RequestCondition
		return new ApiVersionRequestCondition(apiVersion, apiVersionProperties, index);
	}
    
}
