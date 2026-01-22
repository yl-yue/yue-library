package ai.yue.library.base.crypto.annotation;

import ai.yue.library.base.crypto.config.properties.CryptoProperties;
import ai.yue.library.base.crypto.util.EncryptParamUtils;
import ai.yue.library.base.util.SpringUtils;
import ai.yue.library.base.view.Result;
import cn.hutool.v7.crypto.symmetric.SymmetricCrypto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 响应加密处理器
 *
 * @author	ylyue
 * @since	2020年9月18日
 */
@ControllerAdvice
@ConditionalOnClass(HttpServletRequest.class)
@ConditionalOnProperty(prefix = CryptoProperties.PREFIX, name = "enable-response-encrypt-annotation", havingValue = "true", matchIfMissing = true)
public class ResponseEncryptAnnotationAdvice<T> implements ResponseBodyAdvice<T> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return returnType.hasMethodAnnotation(ResponseEncrypt.class) && returnType.getMethod().getReturnType() == Result.class;
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
		if (result.isFlag() == false) {
			return body;
		}

		// 获取SymmetricCrypto
		ResponseEncrypt responseEncrypt = returnType.getMethodAnnotation(ResponseEncrypt.class);
		String factoryBeanName = responseEncrypt.symmetricCryptoFactoryBeanName();
		RequestDecryptEncryptFactory decryptEncryptFactory = SpringUtils.getBean(factoryBeanName, RequestDecryptEncryptFactory.class);
		HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
		SymmetricCrypto symmetricCrypto = decryptEncryptFactory.getSymmetricCrypto(servletRequest);

		// 加密Data
		String encryptedBase64 = symmetricCrypto.encryptBase64(EncryptParamUtils.toEncryptByte(result.getData()));
		result.setData(encryptedBase64);
		return body;
	}

}
