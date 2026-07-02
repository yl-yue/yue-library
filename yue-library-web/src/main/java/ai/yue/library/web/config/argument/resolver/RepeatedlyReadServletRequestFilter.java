package ai.yue.library.web.config.argument.resolver;

import ai.yue.library.web.util.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

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

	/**
	 * 是否对 multipart 请求包装为可重复读
	 */
	private final boolean repeatedlyReadMultipart;

	/**
	 * 默认构造，multipart 请求跳过包装（保全文件上传）
	 */
	public RepeatedlyReadServletRequestFilter() {
		this(false);
	}

	/**
	 * @param repeatedlyReadMultipart 是否对 multipart 请求包装为可重复读
	 */
	public RepeatedlyReadServletRequestFilter(boolean repeatedlyReadMultipart) {
		this.repeatedlyReadMultipart = repeatedlyReadMultipart;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		/*
		 * multipart 请求的 body 字节缓存与 getParts() 参数解析互斥（Servlet 规范限制：二者独占 InputStream 唯一一次可读性）。
		 * 未开启 repeatedlyReadMultipart 时跳过包装，让容器正常解析 multipart 文件上传，避免包装后 getParts() 因流已耗尽而解析失败。
		 */
		if (ServletUtils.isMultipart(request) && !repeatedlyReadMultipart) {
			filterChain.doFilter(request, response);
			return;
		}

		log.debug("传递输入流可反复读取的HttpServletRequest ...");
		ServletRequest requestWrapper = new RepeatedlyReadServletRequestWrapper(request);
		filterChain.doFilter(requestWrapper, response);
	}

}
