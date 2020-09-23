package ai.yue.library.test.webflux.controller.webflux.exception;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

/**
 * @author	ylyue
 * @since	2020年9月16日
 */
@Component
public class FilterExceptionFilter implements WebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//		throw new ResultException("Filter抛出异常拦截测试");
		return chain.filter(exchange);
	}

}
