package ai.yue.library.web.config.argument.resolver;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import ai.yue.library.web.util.servlet.ServletUtils;

/**
 * 包装HttpServletRequest实现输入流可重复读取
 * 
 * @author	ylyue
 * @since	2020年9月3日
 */
public class RepeatedlyReadServletRequestWrapper extends HttpServletRequestWrapper {
	
	/**
	 * 流中的数据
	 */
	private final byte[] body;

	public RepeatedlyReadServletRequestWrapper(HttpServletRequest request) {
		super(request);
		// 获取流中的数据放到字节数组中
		body = ServletUtils.getBodyBytes(request);
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		// 缓存数据
		final ByteArrayInputStream body = new ByteArrayInputStream(this.body);

		return new ServletInputStream() {
			
			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {

			}

			@Override
			public int read() throws IOException {
				// 从缓存的数据中读取数据
				return body.read();
			}

		};
	}

}
