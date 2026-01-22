package ai.yue.library.base.crypto.aspect.key.exchange;

import ai.yue.library.base.crypto.annotation.RequestDecrypt;
import ai.yue.library.base.crypto.config.properties.KeyExchangeProperties;
import ai.yue.library.base.crypto.constant.key.exchange.ExchangeKeyEnum;
import ai.yue.library.base.crypto.dao.key.exchange.KeyExchangeStorage;
import ai.yue.library.base.crypto.dto.KeyExchangeStorageDTO;
import ai.yue.library.base.exception.ParamException;
import ai.yue.library.web.util.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.crypto.symmetric.SymmetricCrypto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.PathContainer;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import org.springframework.web.util.pattern.PathPatternParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 请求解密处理器
 *
 * @author	ylyue
 * @since	2020年9月18日
 */
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
@ConditionalOnClass(HttpServletRequest.class)
@ConditionalOnProperty(prefix = KeyExchangeProperties.PREFIX, name = "enabled", havingValue = "true")
public class RequestBodyDecryptAdvice extends RequestBodyAdviceAdapter {

	final KeyExchangeProperties keyExchangeProperties;
	final KeyExchangeStorage keyExchangeStorage;

	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		// RequestDecrypt过滤
		if (methodParameter.hasMethodAnnotation(RequestDecrypt.class)) {
			return false;
		}

		// 请求白名单过滤
		List<String> whiteList = new ArrayList<>();
		whiteList.addAll(keyExchangeProperties.getWhiteList());
		for (String defaultWhite : KeyExchangeProperties.DEFAULT_WHITE_LIST) {
			whiteList.add(defaultWhite);
		}
		String path = ServletUtils.getRequest().getRequestURI();
		for (String pathPattern : whiteList) {
			if (new PathPatternParser().parse(pathPattern).matches(PathContainer.parsePath(path))) {
				return false;
			}
		}

		return true;
	}

	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
		// 1. 获取sessionKey
		String sessionKey = null;
		if (keyExchangeProperties.isUseAuthTokenGetExchangeKey()) {
			sessionKey = ServletUtils.getAuthToken();
		}
		if (StrUtil.isBlank(sessionKey)) {
			String headerNameGetExchangeKey = keyExchangeProperties.getHeaderNameGetExchangeKey();
			sessionKey = ServletUtils.getRequest().getHeader(headerNameGetExchangeKey);
		}
		if (StrUtil.isBlank(sessionKey)) {
			throw new ParamException("未能获取到交换密钥：1. 请确认是否已进行过密钥交换，2. 请确认是否通过OAuth2或Header传入获取密钥时使用的sessionKey。");
		}

		// 2. 创建加密算法实例
		KeyExchangeStorageDTO keyExchangeStorageDTO = keyExchangeStorage.getKeyExchangeStorageDTO(sessionKey);
		String symmetricKey = keyExchangeStorageDTO.getSymmetricKey();
		ExchangeKeyEnum exchangeKeyType = keyExchangeStorageDTO.getExchangeKeyType();
		SymmetricCrypto symmetricCrypto = exchangeKeyType.getSymmetricCrypto(symmetricKey);

		// 3. Body解密
		String finalSessionKey = sessionKey;
		return new HttpInputMessage() {
			@Override
			public InputStream getBody() throws IOException {
				String decryptStr = StreamUtils.copyToString(inputMessage.getBody(), Charset.defaultCharset());
				log.debug("【密钥交换-解密】sessionKey={}，symmetricKey={}，decryptStr={}", finalSessionKey, symmetricKey, decryptStr);
				return new ByteArrayInputStream(symmetricCrypto.decrypt(decryptStr));
			}

			@Override
			public HttpHeaders getHeaders() {
				return inputMessage.getHeaders();
			}
		};
	}

}
