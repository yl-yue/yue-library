package ai.yue.library.base.crypto.aspect.key.exchange;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.crypto.config.properties.KeyExchangeProperties;
import ai.yue.library.base.crypto.constant.key.exchange.ExchangeKeyEnum;
import ai.yue.library.base.crypto.dao.key.exchange.KeyExchangeStorage;
import ai.yue.library.base.crypto.dto.KeyExchangeStorageDTO;
import ai.yue.library.base.exception.ParamException;
import ai.yue.library.base.exception.ResultException;
import ai.yue.library.base.util.SpringUtils;
import ai.yue.library.base.validation.Validator;
import ai.yue.library.base.view.R;
import ai.yue.library.web.util.ServletUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.v7.core.array.ArrayUtil;
import cn.hutool.v7.core.bean.BeanUtil;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.core.util.CharsetUtil;
import cn.hutool.v7.crypto.symmetric.SymmetricCrypto;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.server.PathContainer;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 请求解密处理器
 *
 * @author	ylyue
 * @since	2020年9月18日
 */
@Slf4j
public class RequestDecryptHandler implements HandlerMethodArgumentResolver {

	@Resource
	KeyExchangeProperties keyExchangeProperties;
	@Resource
	KeyExchangeStorage keyExchangeStorage;

	public RequestDecryptHandler() {
		this.keyExchangeProperties = SpringUtils.getBean(KeyExchangeProperties.class);
		this.keyExchangeStorage = SpringUtils.getBean(KeyExchangeStorage.class);
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> parameterType = parameter.getParameterType();

		// 简单属性过滤
		if (BeanUtils.isSimpleProperty(parameterType)) {
			return false;
		}

		// 非JavaBean过滤
		if (!BeanUtil.isWritableBean(parameterType)) {
			return false;
		}

		// 未开启密钥交换加解密过滤
		if (!keyExchangeProperties.isEnabled()) {
			return false;
		}

		// GET请求过滤
		HttpServletRequest request = ServletUtils.getRequest();
		if (ServletUtils.isGetMethod(request)) {
			return false;
		}

		// 请求白名单过滤
		List<String> whiteList = new ArrayList<>();
		whiteList.addAll(keyExchangeProperties.getWhiteList());
		for (String defaultWhite : KeyExchangeProperties.DEFAULT_WHITE_LIST) {
			whiteList.add(defaultWhite);
		}
		String path = request.getRequestURI();
		for (String pathPattern : whiteList) {
			if (new PathPatternParser().parse(pathPattern).matches(PathContainer.parsePath(path))) {
				return false;
			}
		}

		// 空body过滤
		byte[] bodyBytes = ServletUtils.getBodyBytes(request);
		if (ArrayUtil.isEmpty(bodyBytes)) {
			return false;
		}

		return true;
	}
	
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		// 1. 获取sessionKey
		String sessionKey = null;
		HttpServletRequest request = ServletUtils.getRequest();
		if (keyExchangeProperties.isUseAuthTokenGetExchangeKey()) {
			sessionKey = ServletUtils.getAuthToken(request);
		}
		if (StrUtil.isBlank(sessionKey)) {
			String headerNameGetExchangeKey = keyExchangeProperties.getHeaderNameGetExchangeKey();
			sessionKey = request.getHeader(headerNameGetExchangeKey);
		}
		if (StrUtil.isBlank(sessionKey)) {
			throw new ParamException("未能获取到交换密钥：1. 请确认是否已进行过密钥交换，2. 请确认是否通过OAuth2或Header传入获取密钥时使用的sessionKey。");
		}

		// 2. 创建加密算法实例
		KeyExchangeStorageDTO keyExchangeStorageDTO = keyExchangeStorage.getKeyExchangeStorageDTO(sessionKey);
		String symmetricKey = keyExchangeStorageDTO.getSymmetricKey();
		ExchangeKeyEnum exchangeKeyType = keyExchangeStorageDTO.getExchangeKeyType();
		byte[] bodyBytes = ServletUtils.getBodyBytes(request);
		String decryptStr = new String(bodyBytes, CharsetUtil.UTF_8);
		log.debug("【密钥交换-解密】sessionKey={}，symmetricKey={}，decryptStr={}", sessionKey, symmetricKey, decryptStr);
		SymmetricCrypto symmetricCrypto = exchangeKeyType.getSymmetricCrypto(symmetricKey);

		// 3. 解密body
		String decryptBytes;
		try {
			decryptBytes = symmetricCrypto.decryptStr(decryptStr);
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			throw new ResultException(R.paramDecryptError());
		}
		Object param = Convert.toJavaBean(decryptBytes, parameter.getParameterType());

		// 4. 确认校验
		boolean verify = false;
		Class<?>[] groups = {};
		if (parameter.hasParameterAnnotation(Valid.class) || parameter.hasParameterAnnotation(Validated.class)) {
			verify = true;
			Validated validated = parameter.getParameterAnnotation(Validated.class);
			groups = validated != null ? validated.value() : groups;
		}
		if (verify == false && param != null) {
			Class<?> paramClass = param.getClass();
			if (paramClass.isAnnotationPresent(Valid.class) || paramClass.isAnnotationPresent(Validated.class)) {
				verify = true;
			}
		}

		// 5. 执行校验
		if (verify) {
			SpringUtils.getBean(Validator.class).valid(param, groups);
		}

		// 6. 返回结果
		return param;
	}

}
