package ai.yue.library.webflux.config.exception;

import java.util.List;

import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;
import org.springframework.web.server.ServerWebExchange;

import ai.yue.library.base.view.Result;
import reactor.core.publisher.Mono;

/**
 * 响应结果处理器
 * <p>标准HTTP状态码
 * 
 * @author	ylyue
 * @since	2020年9月18日
 */
public class ResultResponseBodyHandler extends ResponseBodyResultHandler {

	/**
	 * 默认优先级高于 ResponseBodyResultHandler
	 */
	public ResultResponseBodyHandler(List<HttpMessageWriter<?>> writers, RequestedContentTypeResolver resolver,
			ReactiveAdapterRegistry registry) {
		super(writers, resolver, registry);
		setOrder(99);
	}
	
	@Override
	public boolean supports(HandlerResult result) {
		if (!super.supports(result)) {
			return false;
		}
		
		if (result.getReturnType().getRawClass() == Result.class) {
			return true;
		}
		
		/*
		 * Mono 与 Flux 返回类型为非阻塞的反应式编程，暂不考虑以阻塞的方式同步 Result Code 为 HTTP 状态码。
		 * 推荐 return Mono<ResponseEntity<Result<?>>> 类型直接响应 HTTP 状态码，不需要做拦截处理。
		 * ai.yue.library.base.view.Result.castToResponseEntity().
		 */
//		ResolvableType generic = result.getReturnType().getGeneric(0);
//		if (generic.getRawClass() == Result.class) {
//			return true;
//		}
		
		return false;
	}
	
	@Override
	public Mono<Void> handleResult(ServerWebExchange exchange, HandlerResult result) {
		// 获得状态码，但暂不处理 Mono 与 Flux 返回类型的 Result
		Object body = result.getReturnValue();
		Integer code = null;
		if (body instanceof Result) {
			code = ((Result<?>) body).getCode();
		}
		
//		else if (body instanceof Mono) {
//			@SuppressWarnings("unchecked")
//			Result<?> block = ((Mono<Result<?>>) body).block();
//			code = block.getCode();
//		}
		
		// 设置HTTP状态码
		ServerHttpResponse response = exchange.getResponse();
		int status = response.getRawStatusCode();
		if (code != null && code != status) {
			response.setRawStatusCode(code);
		}
		
		// 调用父类结果处理
		return super.handleResult(exchange, result);
	}
	
}
