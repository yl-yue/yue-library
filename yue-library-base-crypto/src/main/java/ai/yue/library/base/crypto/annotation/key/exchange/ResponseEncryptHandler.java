package ai.yue.library.base.crypto.annotation.key.exchange;

import ai.yue.library.base.crypto.constant.key.exchange.ExchangeKeyEnum;
import ai.yue.library.base.crypto.dao.key.exchange.KeyExchangeStorage;
import ai.yue.library.base.crypto.util.EncryptParamUtils;
import ai.yue.library.base.exception.ParamException;
import ai.yue.library.base.util.SpringUtils;
import ai.yue.library.base.view.Result;
import ai.yue.library.web.util.ServletUtils;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 响应加密处理器
 *
 * @author	ylyue
 * @since	2020年9月18日
 */
@ControllerAdvice
@ConditionalOnClass(HttpServletRequest.class)
public class ResponseEncryptHandler<T> implements ResponseBodyAdvice<T> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return returnType.hasMethodAnnotation(ResponseEncrypt.class) && returnType.getMethod().getReturnType() == Result.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T beforeBodyWrite(T body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		// NULL值与错误Result不做加密处理
		if (body == null) {
			return body;
		}
		Result<Object> result = (Result<Object>) body;
		if (result.isFlag() == false) {
			return body;
		}

		// 根据注解参数创建对应的加密算法实例
		ResponseEncrypt methodAnnotation = returnType.getMethodAnnotation(ResponseEncrypt.class);
		boolean enableExchangeKeyEncrypt = methodAnnotation.enableExchangeKeyEncrypt();
		SymmetricCrypto symmetricCrypto = null;
		if (enableExchangeKeyEncrypt == true) {
			// 使用交换密钥创建加密算法实例
			String keyExchangeStorageKey = null;
			HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
			if (methodAnnotation.useAuthTokenGetExchangeKey()) {
				keyExchangeStorageKey = ServletUtils.getAuthToken(servletRequest);
			} else {
				String headerNameGetExchangeKey = methodAnnotation.headerNameGetExchangeKey();
				String paramNameGetExchangeKey = methodAnnotation.paramNameGetExchangeKey();
				keyExchangeStorageKey = servletRequest.getHeader(headerNameGetExchangeKey);
				if (StrUtil.isBlank(keyExchangeStorageKey)) {
					keyExchangeStorageKey = servletRequest.getParameter(paramNameGetExchangeKey);
				}
			}

			if (StrUtil.isBlank(keyExchangeStorageKey)) {
				throw new ParamException("未能获取到交换密钥：1. 请确认是否已进行过密钥交换，2. 请确认是否传入keyExchangeStorageKey（如：token别名、UUID等）并指定了对应的Get方式。");
			}

			// 创建加密算法实例
			KeyExchangeStorage keyExchangeStorage = SpringUtils.getBean(KeyExchangeStorage.class);
			String exchangeKey = keyExchangeStorage.getExchangeKey(keyExchangeStorageKey);
			ExchangeKeyEnum exchangeKeyType = methodAnnotation.exchangeKeyType();
			symmetricCrypto = exchangeKeyType.getSymmetricCrypto(exchangeKey.getBytes());
		} else {
			// 使用用户定义的密钥创建加密算法实例
			symmetricCrypto = methodAnnotation.exchangeKeyType().getSymmetricCrypto(methodAnnotation.key().getBytes());
		}

		// 加密Data
		String encryptedBase64 = symmetricCrypto.encryptBase64(EncryptParamUtils.toEncryptByte(result.getData()));
		result.setData(encryptedBase64);
		return body;
	}

}
