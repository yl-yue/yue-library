package ai.yue.library.webflux.config.exception;

import ai.yue.library.base.constant.Constant;
import ai.yue.library.base.util.I18nUtils;
import ai.yue.library.base.view.Result;
import org.slf4j.MDC;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

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



		// 1. 处理参数
//		if (body == null) {
//			return null;
//		}



		// 4. 响应结果
//		return body;




		// 获得状态码，但暂不处理 Mono 与 Flux 返回类型的 Result
		Object body = result.getReturnValue();
//		Integer code = null;
		if (body instanceof Result) {
//			code = ((Result<?>) body).getCode();
			/*
			 * 2. 设置i18n msg
			 *   - code >= 600 需调用 R.errorPromptI18n() 方法
			 *   - 此处统一翻译 code < 600 的 msg
			 */
//		Integer code = body.getCode();
//			if (code != null && code >= 200 && code < 600) {
//				((Result<?>) body).setMsg(I18nUtils.getYue(((Result<?>) body).getMsg()));
//			}

			// 3. 设置链路ID
			((Result<?>) body).setTraceId(MDC.get(Constant.TRACE_ID));
		}



//		else if (body instanceof Mono) {
//			@SuppressWarnings("unchecked")
//			Result<?> block = ((Mono<Result<?>>) body).block();
//			code = block.getCode();
//		}
		
		// 设置HTTP状态码
//		ServerHttpResponse response = exchange.getResponse();
//		int status = response.getRawStatusCode();
//		if (code != null && code != status) {
////			response.setRawStatusCode(code);
//			response.setRawStatusCode(HttpStatus.OK.value());
//		}
//		response.setRawStatusCode(HttpStatus.OK.value());
		// 调用父类结果处理
		return super.handleResult(exchange, result);
	}
	
}
