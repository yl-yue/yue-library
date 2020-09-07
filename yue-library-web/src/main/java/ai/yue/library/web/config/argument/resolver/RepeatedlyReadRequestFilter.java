package ai.yue.library.web.config.argument.resolver;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * ServletInputStream可反复读取的HttpServletRequestWrapper过滤器
 * <p>传递可反复读取的HttpServletRequest
 * <p>OncePerRequestFilter是在一次外部请求中只过滤一次。对于服务器内部之间的forward等请求，不会再次执行过滤方法。
 * 
 * @author ylyue
 * @since 2020年9月3日
 */
@Component
public class RepeatedlyReadRequestFilter extends OncePerRequestFilter {
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// 防止流读取一次后就没有了, 所以需要将流继续写出去
		ServletRequest requestWrapper = new RepeatedlyReadRequestWrapper(request);
		filterChain.doFilter(requestWrapper, response);
	}
	
}
