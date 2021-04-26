package ai.yue.library.web.config.argument.resolver;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.util.ListUtils;
import ai.yue.library.base.util.SpringUtils;
import ai.yue.library.base.validation.Validator;
import ai.yue.library.web.util.ServletUtils;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMapMethodArgumentResolver;
import org.springframework.web.method.support.UriComponentsContributor;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.multipart.support.MultipartResolutionDelegate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.validation.Valid;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Array、List对象方法参数解析器
 * 
 * @author	ylyue
 * @since	2020年6月9日
 */
public class ArrayArgumentResolver extends AbstractNamedValueMethodArgumentResolver
		implements UriComponentsContributor {

	private static final TypeDescriptor STRING_TYPE_DESCRIPTOR = TypeDescriptor.valueOf(String.class);

	private final boolean useDefaultResolution;


	/**
	 * Create a new {@link CustomRequestParamMethodArgumentResolver} instance.
	 * @param useDefaultResolution in default resolution mode a method argument
	 * that is a simple type, as defined in {@link BeanUtils#isSimpleProperty},
	 * is treated as a request parameter even if it isn't annotated, the
	 * request parameter name is derived from the method parameter name.
	 */
	public ArrayArgumentResolver(boolean useDefaultResolution) {
		this.useDefaultResolution = useDefaultResolution;
	}

	/**
	 * Create a new {@link CustomRequestParamMethodArgumentResolver} instance.
	 * @param beanFactory a bean factory used for resolving  ${...} placeholder
	 * and #{...} SpEL expressions in default values, or {@code null} if default
	 * values are not expected to contain expressions
	 * @param useDefaultResolution in default resolution mode a method argument
	 * that is a simple type, as defined in {@link BeanUtils#isSimpleProperty},
	 * is treated as a request parameter even if it isn't annotated, the
	 * request parameter name is derived from the method parameter name.
	 */
	public ArrayArgumentResolver(@Nullable ConfigurableBeanFactory beanFactory,
			boolean useDefaultResolution) {

		super(beanFactory);
		this.useDefaultResolution = useDefaultResolution;
	}

	/**
	 * Supports the following:
	 * <ul>
	 * <li>@RequestParam-annotated method arguments.
	 * This excludes {@link Map} params where the annotation does not specify a name.
	 * See {@link RequestParamMapMethodArgumentResolver} instead for such params.
	 * <li>Arguments of type {@link MultipartFile} unless annotated with @{@link RequestPart}.
	 * <li>Arguments of type {@code Part} unless annotated with @{@link RequestPart}.
	 * <li>In default resolution mode, simple type arguments even if not with @{@link RequestParam}.
	 * </ul>
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		if (parameter.hasParameterAnnotation(RequestParam.class)) {
			if (Map.class.isAssignableFrom(parameter.nestedIfOptional().getNestedParameterType())) {
				RequestParam requestParam = parameter.getParameterAnnotation(RequestParam.class);
				return (requestParam != null && StringUtils.hasText(requestParam.name()));
			} else {
				return true;
			}
		} else {
			if (parameter.hasParameterAnnotation(RequestPart.class)) {
				return false;
			}
			parameter = parameter.nestedIfOptional();
			if (MultipartResolutionDelegate.isMultipartArgument(parameter)) {
				return true;
			} else if (this.useDefaultResolution) {
				Class<?> parameterType = parameter.getParameterType();
				return (parameterType.isArray() || List.class.isAssignableFrom(parameterType));
			} else {
				return false;
			}
		}
	}

	@Override
	protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
		RequestParam ann = parameter.getParameterAnnotation(RequestParam.class);
		return (ann != null ? new RequestParamNamedValueInfo(ann) : new RequestParamNamedValueInfo());
	}

	@Override
	@Nullable
	protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
		HttpServletRequest servletRequest = request.getNativeRequest(HttpServletRequest.class);

		if (servletRequest != null) {
			Object mpArg = MultipartResolutionDelegate.resolveMultipartArgument(name, parameter, servletRequest);
			if (mpArg != MultipartResolutionDelegate.UNRESOLVABLE) {
				return mpArg;
			}
		}

		Object arg = null;
		MultipartRequest multipartRequest = request.getNativeRequest(MultipartRequest.class);
		if (multipartRequest != null) {
			List<MultipartFile> files = multipartRequest.getFiles(name);
			if (!files.isEmpty()) {
				arg = (files.size() == 1 ? files.get(0) : files);
			}
		}
		if (arg == null) {
			String[] paramValues = request.getParameterValues(name);
			if (paramValues != null) {
				arg = (paramValues.length == 1 ? paramValues[0] : paramValues);
			}
		}
		
		// yue-library
		if (arg == null) {
			JSONObject param = null;
			try {
				param = ServletUtils.getParamToJson(servletRequest);
			} catch (Exception e) {
				// 忽略
			}
			if (param != null) {
				arg = param.get(name);
			} else {
				// 确认数组类型
				String body = ServletUtils.getBody(servletRequest);
				Class<?> parameterType = parameter.getParameterType();
				Class<?> actualTypeArgument = null;
				if (List.class.isAssignableFrom(parameterType)) {
					ParameterizedType genericParameterType = (ParameterizedType) parameter.getGenericParameterType();
					actualTypeArgument = (Class<?>) genericParameterType.getActualTypeArguments()[0];
				} else if (parameterType.isArray()) {
					actualTypeArgument = parameterType.getComponentType();
				}
				
				// 执行转换
				if (parameterType == JSONArray.class || actualTypeArgument == JSONArray.class) {
					arg = Convert.toJSONArray(body);
				} else if (actualTypeArgument == JSONObject.class) {
					arg = ListUtils.toJsonList(Convert.toJSONArray(body));
				} else if (!BeanUtils.isSimpleProperty(actualTypeArgument) && BeanUtil.isBean(actualTypeArgument)) {
					List<?> value = Convert.toJSONArray(body).toJavaList(actualTypeArgument);
					arg = value;

					// 确认校验
					boolean verify = false;
					Class<?>[] groups = {};
					if (parameter.hasParameterAnnotation(Valid.class) || parameter.hasParameterAnnotation(Validated.class)) {
						verify = true;
						Validated validated = parameter.getParameterAnnotation(Validated.class);
						groups = validated != null ? validated.value() : groups;
					}
					if (verify == false && value != null && value.size() > 0) {
						Object verifyObject = value.get(0);
						Class<?> paramClass = verifyObject.getClass();
						if (paramClass.isAnnotationPresent(Valid.class) || paramClass.isAnnotationPresent(Validated.class)) {
							verify = true;
						}
					}

					// 执行校验
					if (verify) {
						Validator validator = SpringUtils.getBean(Validator.class);
						for (Object javaBean : value) {
							validator.valid(javaBean, groups);
						}
					}
				} else {
					arg = Convert.toList(actualTypeArgument, body);
				}
			}
		}
		
		return arg;
	}

	@Override
	protected void handleMissingValue(String name, MethodParameter parameter, NativeWebRequest request)
			throws Exception {

		HttpServletRequest servletRequest = request.getNativeRequest(HttpServletRequest.class);
		if (MultipartResolutionDelegate.isMultipartArgument(parameter)) {
			if (servletRequest == null || !MultipartResolutionDelegate.isMultipartRequest(servletRequest)) {
				throw new MultipartException("Current request is not a multipart request");
			}
			else {
				throw new MissingServletRequestPartException(name);
			}
		}
		else {
			throw new MissingServletRequestParameterException(name,
					parameter.getNestedParameterType().getSimpleName());
		}
	}

	@Override
	public void contributeMethodArgument(MethodParameter parameter, @Nullable Object value,
			UriComponentsBuilder builder, Map<String, Object> uriVariables, ConversionService conversionService) {

		Class<?> paramType = parameter.getNestedParameterType();
		if (Map.class.isAssignableFrom(paramType) || MultipartFile.class == paramType || Part.class == paramType) {
			return;
		}

		RequestParam requestParam = parameter.getParameterAnnotation(RequestParam.class);
		String name = (requestParam != null && StringUtils.hasLength(requestParam.name()) ?
				requestParam.name() : parameter.getParameterName());
		Assert.state(name != null, "Unresolvable parameter name");

		if (value == null) {
			if (requestParam != null &&
					(!requestParam.required() || !requestParam.defaultValue().equals(ValueConstants.DEFAULT_NONE))) {
				return;
			}
			builder.queryParam(name);
		}
		else if (value instanceof Collection) {
			for (Object element : (Collection<?>) value) {
				element = formatUriValue(conversionService, TypeDescriptor.nested(parameter, 1), element);
				builder.queryParam(name, element);
			}
		}
		else {
			builder.queryParam(name, formatUriValue(conversionService, new TypeDescriptor(parameter), value));
		}
	}

	@Nullable
	protected String formatUriValue(
			@Nullable ConversionService cs, @Nullable TypeDescriptor sourceType, @Nullable Object value) {

		if (value == null) {
			return null;
		}
		else if (value instanceof String) {
			return (String) value;
		}
		else if (cs != null) {
			return (String) cs.convert(value, sourceType, STRING_TYPE_DESCRIPTOR);
		}
		else {
			return value.toString();
		}
	}


	private static class RequestParamNamedValueInfo extends NamedValueInfo {

		public RequestParamNamedValueInfo() {
			super("", false, ValueConstants.DEFAULT_NONE);
		}

		public RequestParamNamedValueInfo(RequestParam annotation) {
			super(annotation.name(), annotation.required(), annotation.defaultValue());
		}
	}

}
