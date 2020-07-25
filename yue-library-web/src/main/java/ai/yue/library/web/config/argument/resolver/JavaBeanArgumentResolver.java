package ai.yue.library.web.config.argument.resolver;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import ai.yue.library.base.util.ParamUtils;
import ai.yue.library.base.util.SpringUtils;
import ai.yue.library.base.validation.Validator;
import cn.hutool.core.bean.BeanUtil;

/**
 * POJO、IPO、JavaBean对象方法参数解析器
 * 
 * @author	ylyue
 * @since	2020年6月9日
 */
public class JavaBeanArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> parameterType = parameter.getParameterType();
		return !BeanUtils.isSimpleProperty(parameterType) && BeanUtil.isBean(parameterType);
	}
	
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		Object param = ParamUtils.getParam(parameter.getParameterType());
		if (parameter.hasParameterAnnotation(Valid.class) || parameter.hasParameterAnnotation(Validated.class)) {
			SpringUtils.getBean(Validator.class).valid(param);
		}
		
		return param;
	}

}
