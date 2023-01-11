package ai.yue.library.web.config.argument.resolver;

import ai.yue.library.web.util.ServletUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;

import javax.servlet.http.HttpServletRequest;

/**
 * {@linkplain RequestParamMethodArgumentResolver} 增强
 * <p>添加对body参数的解析
 * 
 * @author	ylyue
 * @since	2020年7月25日
 */
public class CustomRequestParamMethodArgumentResolver extends RequestParamMethodArgumentResolver {

	public CustomRequestParamMethodArgumentResolver(boolean useDefaultResolution) {
		super(useDefaultResolution);
	}

	public CustomRequestParamMethodArgumentResolver(@Nullable ConfigurableBeanFactory beanFactory,
													boolean useDefaultResolution) {
		super(beanFactory, useDefaultResolution);
	}

	@Override
	@Nullable
	protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
		HttpServletRequest servletRequest = request.getNativeRequest(HttpServletRequest.class);
		// 保留原始逻辑
		Object arg = super.resolveName(name, parameter, request);

		// yue-library
		if (arg == null) {
			arg = ServletUtils.getParamToJson(servletRequest).get(name);
		}
		// 被多重“”包装的String转Long
//		else if (arg instanceof String && parameter.getParameterType() == Long.class && StrUtil.isWrap((String) arg, "\"")) {
//			arg = StrUtil.strip((CharSequence) arg, "\"");
//		}

		return arg;
	}

}
