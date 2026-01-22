package ai.yue.library.base.crypto.annotation;

import ai.yue.library.base.crypto.config.properties.CryptoProperties;
import ai.yue.library.base.util.ListUtils;
import ai.yue.library.base.util.ParamUtils;
import ai.yue.library.base.util.SpringUtils;
import ai.yue.library.web.util.ServletUtils;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.v7.core.array.ArrayUtil;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.crypto.symmetric.SymmetricCrypto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

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
@ConditionalOnClass(HttpServletRequest.class)
@ConditionalOnProperty(prefix = CryptoProperties.PREFIX, name = "enable-request-decrypt-annotation", havingValue = "true", matchIfMissing = true)
public class RequestDecryptAnnotationAdvice extends RequestBodyAdviceAdapter {

	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		return methodParameter.hasMethodAnnotation(RequestDecrypt.class);
	}

	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
		// 获取SymmetricCrypto
		RequestDecrypt requestDecrypt = parameter.getMethodAnnotation(RequestDecrypt.class);
		String factoryBeanName = requestDecrypt.symmetricCryptoFactoryBeanName();
		RequestDecryptEncryptFactory decryptEncryptFactory = SpringUtils.getBean(factoryBeanName, RequestDecryptEncryptFactory.class);
		SymmetricCrypto symmetricCrypto = decryptEncryptFactory.getSymmetricCrypto(ServletUtils.getRequest());

		// 按需解密
		return new HttpInputMessage() {
			@Override
			public InputStream getBody() throws IOException {
				String[] decryptParamNames = requestDecrypt.decryptParamNames();
				if (ArrayUtil.isNotEmpty(decryptParamNames)) {
					try {
						JSONObject param = ParamUtils.getParam();
						log.debug("【RequestDecrypt】paramJson={}", param);
						for (String decryptParamName : decryptParamNames) {
							String decryptParamValue = param.getString(decryptParamName);
							if (StrUtil.isNotBlank(decryptParamValue)) {
								String replaceParamValue = symmetricCrypto.decryptStr(decryptParamValue);
								param.replace(decryptParamName, replaceParamValue);
							}
						}

						return new ByteArrayInputStream(param.toJSONString().getBytes());
					} catch (Exception e) {
						log.error("decryptParamNames与整个body解密是互斥的，获取请求参数失败", e);
						throw e;
					}
				} else {
					String decryptStr = StreamUtils.copyToString(inputMessage.getBody(), Charset.defaultCharset());
					log.debug("【RequestDecrypt】decryptStr={}", decryptStr);
					return new ByteArrayInputStream(symmetricCrypto.decrypt(decryptStr));
				}
			}

			@Override
			public HttpHeaders getHeaders() {
				HttpHeaders headers = inputMessage.getHeaders();
				String[] decryptHeaderNames = requestDecrypt.decryptHeaderNames();
				if (ArrayUtil.isNotEmpty(decryptHeaderNames)) {
					for (String headerName : decryptHeaderNames) {
						List<String> headerValue = headers.get(headerName);
						if (ListUtils.isNotEmpty(headerValue)) {
							String decryptHeaderValue = symmetricCrypto.decryptStr(headerValue.get(0));
							List<String> replaceHeader = new ArrayList<>(1);
							replaceHeader.add(decryptHeaderValue);
							headers.replace(headerName, replaceHeader);
						}
					}
				}

				return headers;
			}
		};
	}

}
