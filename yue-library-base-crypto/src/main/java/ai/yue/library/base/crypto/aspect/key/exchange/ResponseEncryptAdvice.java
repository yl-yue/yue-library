package ai.yue.library.base.crypto.aspect.key.exchange;

import ai.yue.library.base.crypto.annotation.RequestDecrypt;
import ai.yue.library.base.crypto.annotation.ResponseEncrypt;
import ai.yue.library.base.crypto.config.properties.KeyExchangeProperties;
import ai.yue.library.base.crypto.constant.key.exchange.ExchangeKeyEnum;
import ai.yue.library.base.crypto.dao.key.exchange.KeyExchangeStorage;
import ai.yue.library.base.crypto.dto.KeyExchangeStorageDTO;
import ai.yue.library.base.crypto.util.EncryptParamUtils;
import ai.yue.library.base.exception.ParamException;
import ai.yue.library.base.view.Result;
import ai.yue.library.web.util.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import cn.hutool.v7.core.array.ArrayUtil;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.crypto.symmetric.SymmetricCrypto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 响应加密处理器
 *
 * @author	ylyue
 * @since	2020年9月18日
 */
@ControllerAdvice
@RequiredArgsConstructor
@ConditionalOnClass(HttpServletRequest.class)
@ConditionalOnProperty(prefix = KeyExchangeProperties.PREFIX, name = "enabled", havingValue = "true")
public class ResponseEncryptAdvice<T> implements ResponseBodyAdvice<T> {

	final KeyExchangeProperties keyExchangeProperties;
	final KeyExchangeStorage keyExchangeStorage;

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		// 密钥交换是否开启过滤
		if (keyExchangeProperties.isEnabled() == false) {
			return false;
		}

		// Result返回类型过滤
		if (returnType.getMethod().getReturnType() != Result.class) {
			return false;
		}

		// RequestDecrypt过滤
		if (returnType.hasMethodAnnotation(RequestDecrypt.class)) {
			return false;
		}

		// ResponseEncrypt过滤
		if (returnType.hasMethodAnnotation(ResponseEncrypt.class)) {
			return false;
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T beforeBodyWrite(T body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType
			, ServerHttpRequest request, ServerHttpResponse response) {
		// NULL值与错误Result不做加密处理
		if (body == null) {
			return body;
		}
		Result<Object> result = (Result<Object>) body;
		if (result.isFlag() == false || result.getData() == null) {
			return body;
		}

		// 白名单过滤
		List<String> whiteList = new ArrayList<>();
		whiteList.addAll(keyExchangeProperties.getWhiteList());
		for (String defaultWhite : KeyExchangeProperties.DEFAULT_WHITE_LIST) {
			whiteList.add(defaultWhite);
		}
		String path = request.getURI().getPath();
		for (String pathPattern : whiteList) {
			if (new PathPatternParser().parse(pathPattern).matches(PathContainer.parsePath(path))) {
				return body;
			}
		}

		// 获取sessionKey
		String sessionKey = null;
		HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
		if (keyExchangeProperties.isUseAuthTokenGetExchangeKey()) {
			sessionKey = ServletUtils.getAuthToken(servletRequest);
		}
		if (StrUtil.isBlank(sessionKey)) {
			String headerNameGetExchangeKey = keyExchangeProperties.getHeaderNameGetExchangeKey();
			sessionKey = servletRequest.getHeader(headerNameGetExchangeKey);
		}
		if (StrUtil.isBlank(sessionKey)) {
			// 空body过滤
			byte[] bodyBytes = ServletUtils.getBodyBytes(servletRequest);
			if (ArrayUtil.isEmpty(bodyBytes)) {
				return body;
			}

			throw new ParamException("未能获取到交换密钥：1. 请确认是否已进行过密钥交换，2. 请确认是否通过OAuth2或Header传入获取密钥时使用的sessionKey。");
		}

		// 创建加密算法实例
		KeyExchangeStorageDTO keyExchangeStorageDTO = keyExchangeStorage.getKeyExchangeStorageDTO(sessionKey);
		String symmetricKey = keyExchangeStorageDTO.getSymmetricKey();
		ExchangeKeyEnum exchangeKeyType = keyExchangeStorageDTO.getExchangeKeyType();
		SymmetricCrypto symmetricCrypto = exchangeKeyType.getSymmetricCrypto(symmetricKey);

		// 加密Data
		String encryptedBase64 = symmetricCrypto.encryptBase64(EncryptParamUtils.toEncryptByte(result.getData()));
		result.setData(encryptedBase64);
		return body;
	}

}
