package ai.yue.library.base.crypto.annotation.key.exchange;

import ai.yue.library.base.crypto.constant.key.exchange.ExchangeKeyEnum;
import ai.yue.library.base.crypto.dao.key.exchange.KeyExchangeStorage;
import ai.yue.library.base.exception.ParamException;
import ai.yue.library.base.util.SpringUtils;
import ai.yue.library.web.util.ServletUtils;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * 请求解密处理器
 *
 * @author	ylyue
 * @since	2020年9月18日
 */
@Slf4j
@ControllerAdvice
@ConditionalOnClass(HttpServletRequest.class)
public class RequestDecryptHandler extends RequestBodyAdviceAdapter {

	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		return methodParameter.hasMethodAnnotation(RequestDecrypt.class);
	}

	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
		// 根据注解参数创建对应的加密算法实例
		RequestDecrypt methodAnnotation = parameter.getMethodAnnotation(RequestDecrypt.class);
		boolean enableExchangeKeyDecrypt = methodAnnotation.enableExchangeKeyDecrypt();
		SymmetricCrypto symmetricCrypto = null;
		if (enableExchangeKeyDecrypt == true) {
			// 使用交换密钥创建加密算法实例
			String keyExchangeStorageKey = null;
			if (methodAnnotation.useAuthTokenGetExchangeKey()) {
				keyExchangeStorageKey = ServletUtils.getAuthToken();
			} else {
				String headerNameGetExchangeKey = methodAnnotation.headerNameGetExchangeKey();
				String paramNameGetExchangeKey = methodAnnotation.paramNameGetExchangeKey();
				keyExchangeStorageKey = ServletUtils.getRequest().getHeader(headerNameGetExchangeKey);
				if (StrUtil.isBlank(keyExchangeStorageKey)) {
					keyExchangeStorageKey = ServletUtils.getRequest().getParameter(paramNameGetExchangeKey);
				}
			}

			if (StrUtil.isBlank(keyExchangeStorageKey)) {
				throw new ParamException("未能获取到交换密钥：1. 请确认是否已进行过密钥交换，2. 请确认是否传入keyExchangeStorageKey（如：token别名、UUID等）并指定了对应的Get方式。");
			}

			// 创建加密算法实例
			KeyExchangeStorage keyExchangeStorage = SpringUtils.getBean(KeyExchangeStorage.class);
			String exchangeKey = keyExchangeStorage.getExchangeKey(keyExchangeStorageKey);
			log.debug("【密钥交换-解密】keyExchangeStorageKey={}，exchangeKey={}", keyExchangeStorageKey, exchangeKey);
			ExchangeKeyEnum exchangeKeyType = methodAnnotation.exchangeKeyType();
			symmetricCrypto = exchangeKeyType.getSymmetricCrypto(exchangeKey.getBytes());
		} else {
			// 使用用户定义的密钥创建加密算法实例
			symmetricCrypto = methodAnnotation.exchangeKeyType().getSymmetricCrypto(methodAnnotation.key().getBytes());
		}

		// 解密Data
		SymmetricCrypto finalSymmetricCrypto = symmetricCrypto;
		return new HttpInputMessage() {
			@Override
			public InputStream getBody() throws IOException {
				String decryptStr = StreamUtils.copyToString(inputMessage.getBody(), Charset.defaultCharset());
				log.debug("【密钥交换-解密】decryptStr={}", decryptStr);
				return new ByteArrayInputStream(finalSymmetricCrypto.decrypt(decryptStr));
			}

			@Override
			public HttpHeaders getHeaders() {
				return inputMessage.getHeaders();
			}
		};
	}

}
