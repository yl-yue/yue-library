package ai.yue.library.web.config.argument.resolver;

import ai.yue.library.web.util.ServletUtils;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 包装HttpServletRequest实现输入流可重复读取
 * 
 * @author	ylyue
 * @since	2020年9月3日
 */
@Slf4j
public class RepeatedlyReadServletRequestWrapper extends HttpServletRequestWrapper {
	
	/**
	 * 不限制缓存上限的语义值
	 */
	public static final long UNLIMITED = -1L;

	/**
	 * 流中的数据（正常路径：完整缓存）
	 */
	private final byte[] body;

	/**
	 * 超过缓存上限时已读取的前缀字节（与 {@link #overflowSource} 拼接即为完整请求体）
	 */
	private final byte[] overflowPrefix;

	/**
	 * 超过缓存上限时原始流的剩余部分
	 */
	private final ServletInputStream overflowSource;

	/**
	 * 缓存上限（字节），<= 0 表示不限制
	 */
	private final long maxBodySize;

	/**
	 * 超限路径是否已发生过一次读取
	 */
	private boolean overflowConsumed;

	public RepeatedlyReadServletRequestWrapper(HttpServletRequest request) {
		this(request, UNLIMITED);
	}

	/**
	 * @param request     请求对象
	 * @param maxBodySize 请求体缓存上限（字节），<= 0 表示不限制
	 */
	public RepeatedlyReadServletRequestWrapper(HttpServletRequest request, long maxBodySize) {
		super(request);
		this.maxBodySize = maxBodySize;

		byte[] cachedBody = null;
		byte[] prefix = null;
		ServletInputStream source = null;
		try {
			if (maxBodySize <= 0) {
				// 获取流中的数据放到字节数组中
				cachedBody = ServletUtils.getBodyBytes(request);
			} else {
				ServletInputStream in = openInputStream(request);
				ByteArrayOutputStream buffer = readCapped(in, maxBodySize);

				if (buffer.size() > maxBodySize) {
					// 超限：保留已读前缀，与原始流剩余部分拼接后仍可被完整读取一次
					prefix = buffer.toByteArray();
					source = in;
					log.warn("请求体超过重复读缓存上限（{} 字节），本次请求降级为不可重复读：{} {}",
							maxBodySize, request.getMethod(), request.getRequestURI());
				} else {
					cachedBody = buffer.toByteArray();
				}
			}
		} catch (Exception e) {
			String requestUri = request.getMethod() + " " + request.getRequestURI();
			log.error("requestUri: {}", requestUri);
			throw e;
		}

		this.body = cachedBody;
		this.overflowPrefix = prefix;
		this.overflowSource = source;
	}

	private static ServletInputStream openInputStream(HttpServletRequest request) {
		try {
			return request.getInputStream();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	/**
	 * 读取请求体，最多读取 maxBodySize + 1 字节（多读 1 字节即可判定是否超限，堆占用因此有确定上界）
	 *
	 * @param in          请求输入流
	 * @param maxBodySize 缓存上限（字节），> 0
	 * @return 已读取的字节
	 */
	private static ByteArrayOutputStream readCapped(ServletInputStream in, long maxBodySize) {
		try {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			byte[] chunk = new byte[8192];
			while (buffer.size() <= maxBodySize) {
				int len = in.read(chunk, 0, (int) Math.min(chunk.length, maxBodySize + 1 - buffer.size()));
				if (len == -1) {
					break;
				}
				buffer.write(chunk, 0, len);
			}
			return buffer;
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public BufferedReader getReader() throws IOException {
		// 必须显式指定字符集：new InputStreamReader(InputStream) 取 JVM 平台默认（Windows 中文版为 GBK），
		// 会把 UTF-8 编码的请求体按 GBK 解码成乱码，且静默生效
		return new BufferedReader(new InputStreamReader(getInputStream(), resolveCharset()));
	}

	/**
	 * 解析请求体字符集
	 * <p>请求声明的字符集优先（Content-Type 的 charset，或上游过滤器通过 setCharacterEncoding 设定的编码），
	 * 未声明或声明了无法识别的字符集时回落 UTF-8
	 * <p>不回落平台默认字符集——那正是跨平台乱码的根源
	 *
	 * @return 请求体字符集
	 */
	private Charset resolveCharset() {
		String encoding = getRequest().getCharacterEncoding();
		if (encoding == null || encoding.isBlank()) {
			return StandardCharsets.UTF_8;
		}

		try {
			return Charset.forName(encoding);
		} catch (IllegalArgumentException e) {
			// 客户端可能声明非法或不支持的字符集，此处不可抛出（会让请求直接 500），回落 UTF-8
			log.warn("请求声明了不支持的字符集，回落 UTF-8：encoding={}", encoding);
			return StandardCharsets.UTF_8;
		}
	}

	@Override
	public ServletInputStream getInputStream() {
		if (overflowPrefix != null) {
			// 超限降级：仅首次读取可用（已读前缀 + 原始流剩余），重复读显式失败，避免静默返回空 body
			if (overflowConsumed) {
				throw new IllegalStateException("请求体超过重复读缓存上限（" + maxBodySize
						+ " 字节），本次请求已降级为不可重复读");
			}
			overflowConsumed = true;
			return new DelegatingServletInputStream(
					new SequenceInputStream(new ByteArrayInputStream(overflowPrefix), overflowSource));
		}

		// 每次调用都基于缓存字节新建流，实现可重复读取
		return new CachedServletInputStream(body);
	}

	/**
	 * 基于字节缓存的一次性读取流（每次 getInputStream 都新建实例，故可重复读）
	 */
	private static final class CachedServletInputStream extends ServletInputStream {

		private final ByteArrayInputStream source;

		private CachedServletInputStream(byte[] body) {
			this.source = new ByteArrayInputStream(body);
		}

		@Override
		public int read() {
			return source.read();
		}

		@Override
		public int read(byte[] b, int off, int len) {
			return source.read(b, off, len);
		}

		@Override
		public int available() {
			return source.available();
		}

		@Override
		public boolean isFinished() {
			// Servlet 规范语义：流内数据读尽才为 true
			return source.available() == 0;
		}

		@Override
		public boolean isReady() {
			// 缓存流为同步读模型，数据随时可读
			return true;
		}

		@Override
		public void setReadListener(ReadListener readListener) {
			// 同步读模型：缓存流无异步回调需求
		}
	}

	/**
	 * 超限降级路径的透传流：已读前缀与原始流剩余拼接后一次性读完
	 */
	private static final class DelegatingServletInputStream extends ServletInputStream {

		private final InputStream source;

		private boolean finished;

		private DelegatingServletInputStream(InputStream source) {
			this.source = source;
		}

		@Override
		public int read() throws IOException {
			int read = source.read();
			if (read == -1) {
				finished = true;
			}
			return read;
		}

		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			int read = source.read(b, off, len);
			if (read == -1) {
				finished = true;
			}
			return read;
		}

		@Override
		public int available() throws IOException {
			return source.available();
		}

		@Override
		public boolean isFinished() {
			// 不能以 available() 判定：底层网络流可能暂时无数据但未结束
			return finished;
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setReadListener(ReadListener readListener) {
			// 同步读模型：不做异步回调注册
		}
	}

}
