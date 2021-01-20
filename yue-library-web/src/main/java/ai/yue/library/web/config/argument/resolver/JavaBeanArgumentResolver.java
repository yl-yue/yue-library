package ai.yue.library.web.config.argument.resolver;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.util.ParamUtils;
import ai.yue.library.base.util.SpringUtils;
import ai.yue.library.base.validation.Validator;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.validation.Valid;
import java.util.Iterator;

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
		// 1. 确认Content-Type
		RequestMapping requestMapping = parameter.getMethodAnnotation(RequestMapping.class);
		String[] produces = requestMapping.produces();
		boolean containsXmlType = false;
		for (String produce : produces) {
			if (StrUtil.containsIgnoreCase(produce, "xml")) {
				containsXmlType = true;
			}
		}

		// 2. 获得参数
		Object param = null;
		Class<?> parameterType = parameter.getParameterType();
		if (containsXmlType) {
			JSONObject json = new JSONObject();
			Iterator<String> parameterNames = webRequest.getParameterNames();
			while (parameterNames.hasNext()) {
				String paramName = parameterNames.next();
				json.put(paramName, webRequest.getParameter(paramName));
			}

			param = Convert.toJavaBean(json, parameterType);
		} else {
			param = ParamUtils.getParam(parameterType);
		}

		// 3. 确认校验
		boolean verify = false;
		if (parameter.hasParameterAnnotation(Valid.class) || parameter.hasParameterAnnotation(Validated.class)) {
			verify = true;
		}
		if (verify == false && param != null) {
			Class<?> paramClass = param.getClass();
			if (paramClass.isAnnotationPresent(Valid.class) || paramClass.isAnnotationPresent(Validated.class)) {
				verify = true;
			}
		}

		// 4. 执行校验
		if (verify) {
			SpringUtils.getBean(Validator.class).valid(param);
		}

		// 5. 返回结果
		return param;
	}

}
