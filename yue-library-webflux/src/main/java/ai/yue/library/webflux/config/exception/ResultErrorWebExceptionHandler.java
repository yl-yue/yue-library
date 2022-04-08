package ai.yue.library.webflux.config.exception;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import cn.hutool.core.lang.Console;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.all;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Web异常处理器
 * <p>可对WebFilter异常进行统一处理
 * 
 * @author	ylyue
 * @since	2020年9月16日
 */
public class ResultErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {

	public ResultErrorWebExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources,
			ErrorProperties errorProperties, ApplicationContext applicationContext) {
		super(errorAttributes, resources, errorProperties, applicationContext);
	}
	
	@Override
	protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
		return route(acceptsTextHtml(), this::renderErrorView).andRoute(all(), this::renderErrorResponse);
	}
	
	@Override
	protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
		Console.error("path={}", request.path());
		Throwable e = getError(request);
		Result<?> result = R.getResult(e);
		return ServerResponse.status(result.getCode()).contentType(MediaType.APPLICATION_JSON).bodyValue(result);
	}
    
}
