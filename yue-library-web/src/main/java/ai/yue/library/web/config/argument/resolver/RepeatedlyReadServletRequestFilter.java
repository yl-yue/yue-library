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
import java.util.Locale;

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
	 * 默认请求体缓存上限：2MB
	 */
	public static final long DEFAULT_MAX_BODY_SIZE = 2L * 1024 * 1024;

	/**
	 * 是否对 multipart 请求包装为可重复读
	 */
	private final boolean repeatedlyReadMultipart;

	/**
	 * 请求体缓存上限（字节），<= 0 表示不限制
	 */
	private final long maxBodySize;

	/**
	 * 默认构造，multipart 请求跳过包装（保全文件上传）
	 */
	public RepeatedlyReadServletRequestFilter() {
		this(false, DEFAULT_MAX_BODY_SIZE);
	}

	/**
	 * @param repeatedlyReadMultipart 是否对 multipart 请求包装为可重复读
	 */
	public RepeatedlyReadServletRequestFilter(boolean repeatedlyReadMultipart) {
		this(repeatedlyReadMultipart, DEFAULT_MAX_BODY_SIZE);
	}

	/**
	 * @param repeatedlyReadMultipart 是否对 multipart 请求包装为可重复读
	 * @param maxBodySize             请求体缓存上限（字节），<= 0 表示不限制
	 */
	public RepeatedlyReadServletRequestFilter(boolean repeatedlyReadMultipart, long maxBodySize) {
		this.repeatedlyReadMultipart = repeatedlyReadMultipart;
		this.maxBodySize = maxBodySize;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		/*
		 * multipart 请求的 body 字节缓存与 getParts() 参数解析互斥（Servlet 规范限制：二者独占 InputStream 唯一一次可读性）。
		 * 未开启 repeatedlyReadMultipart 时跳过包装，让容器正常解析 multipart 文件上传，避免包装后 getParts() 因流已耗尽而解析失败。
		 */
		boolean multipart = ServletUtils.isMultipart(request);
		if (multipart && !repeatedlyReadMultipart) {
			filterChain.doFilter(request, response);
			return;
		}

		// 二进制/流式请求体不参与重复读缓存：这类请求体可能极大，缓存到堆内存会压垮应用
		if (!multipart && !isCacheableContentType(request.getContentType())) {
			log.debug("跳过请求体重复读包装（content-type 无需重复读）：{} {}", request.getMethod(), request.getContentType());
			filterChain.doFilter(request, response);
			return;
		}

		// 已声明长度且超过上限：不包装直接透传，堆占用与请求体大小脱钩
		long contentLength = request.getContentLengthLong();
		if (maxBodySize > 0 && contentLength > maxBodySize) {
			log.debug("跳过请求体重复读包装（content-length {} 超过上限 {}）：{} {}",
					contentLength, maxBodySize, request.getMethod(), request.getRequestURI());
			filterChain.doFilter(request, response);
			return;
		}

		log.debug("传递输入流可反复读取的HttpServletRequest ...");
		ServletRequest requestWrapper = new RepeatedlyReadServletRequestWrapper(request, maxBodySize);
		filterChain.doFilter(requestWrapper, response);
	}

	/**
	 * 判定 content-type 是否需要请求体重复读
	 * <p>仅文本类请求体参与（JSON/XML/纯文本/表单）；二进制类型（octet-stream、图片、音视频等）直接透传
	 *
	 * @param contentType 请求 content-type（可含 charset 等参数）
	 * @return 需要缓存并支持重复读时为 true
	 */
	private static boolean isCacheableContentType(String contentType) {
		if (contentType == null || contentType.isBlank()) {
			// 未声明类型按普通请求处理，仍受缓存上限约束
			return true;
		}

		String mediaType = contentType.split(";")[0].trim().toLowerCase(Locale.ROOT);
		return mediaType.startsWith("text/")
				|| mediaType.equals("application/json")
				|| mediaType.endsWith("+json")
				|| mediaType.equals("application/xml")
				|| mediaType.endsWith("+xml")
				|| mediaType.equals("application/x-www-form-urlencoded");
	}

}
