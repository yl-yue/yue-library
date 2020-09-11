package ai.yue.library.web.config.argument.resolver;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

/**
 * RepeatedlyReadServletRequestWrapper过滤器
 * <p>传递输入流可反复读取的HttpServletRequest
 * <p>OncePerRequestFilter是在一次外部请求中只过滤一次。对于服务器内部之间的forward等请求，不会再次执行过滤方法。
 * 
 * @author ylyue
 * @since 2020年9月3日
 */
@Slf4j
public class RepeatedlyReadServletRequestFilter extends OncePerRequestFilter {
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.debug("传递输入流可反复读取的HttpServletRequest ...");
		ServletRequest requestWrapper = new RepeatedlyReadServletRequestWrapper(request);
		filterChain.doFilter(requestWrapper, response);
	}
	
}
