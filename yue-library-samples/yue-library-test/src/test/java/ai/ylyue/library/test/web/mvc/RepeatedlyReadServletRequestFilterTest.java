package ai.yue.library.test.web.mvc;

import ai.yue.library.web.config.argument.resolver.RepeatedlyReadServletRequestFilter;
import ai.yue.library.web.config.argument.resolver.RepeatedlyReadServletRequestWrapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RepeatedlyReadServletRequestFilter 单元测试
 * <p>验证 content-type 感知策略：非 multipart 包装、multipart+false 跳过包装、multipart+true 纯字节缓存可重复读
 *
 * @author ylyue
 */
class RepeatedlyReadServletRequestFilterTest {

	private static final byte[] BODY = "name=ylyue&age=18".getBytes(StandardCharsets.UTF_8);

	/**
	 * 非 multipart 请求应被包装为可重复读
	 */
	@Test
	void nonMultipart_shouldWrap() throws Exception {
		MockHttpServletRequest request = buildRequest("POST", "application/json", BODY);

		MockFilterChain chain = new MockFilterChain();
		RepeatedlyReadServletRequestFilter filter = new RepeatedlyReadServletRequestFilter();

		filter.doFilter(request, new MockHttpServletResponse(), chain);

		assertInstanceOf(RepeatedlyReadServletRequestWrapper.class, chain.getRequest());
	}

	/**
	 * multipart 请求 + 默认配置（repeatedlyReadMultipart=false）应跳过包装，保全文件上传
	 */
	@Test
	void multipart_whenDisabled_shouldSkipWrap() throws Exception {
		MockHttpServletRequest request = buildRequest("POST", "multipart/form-data; boundary=----WebKitFormBoundary", BODY);

		MockFilterChain chain = new MockFilterChain();
		RepeatedlyReadServletRequestFilter filter = new RepeatedlyReadServletRequestFilter(false);

		filter.doFilter(request, new MockHttpServletResponse(), chain);

		// 未包装，chain 收到的是原始 request（非 Wrapper）
		HttpServletRequest passedRequest = (HttpServletRequest) chain.getRequest();
		assertFalse(passedRequest instanceof RepeatedlyReadServletRequestWrapper);
	}

	/**
	 * multipart 请求 + repeatedlyReadMultipart=true 应纯字节缓存，body 可重复读
	 */
	@Test
	void multipart_whenEnabled_shouldWrapAndRepeatable() throws Exception {
		MockHttpServletRequest request = buildRequest("POST", "multipart/form-data; boundary=----WebKitFormBoundary", BODY);

		MockFilterChain chain = new MockFilterChain();
		RepeatedlyReadServletRequestFilter filter = new RepeatedlyReadServletRequestFilter(true);

		filter.doFilter(request, new MockHttpServletResponse(), chain);

		RepeatedlyReadServletRequestWrapper wrapper = (RepeatedlyReadServletRequestWrapper) chain.getRequest();
		// body 可重复读：两次读取均得到完整字节
		assertArrayEquals(BODY, wrapper.getInputStream().readAllBytes());
		assertArrayEquals(BODY, wrapper.getInputStream().readAllBytes());
	}

	private MockHttpServletRequest buildRequest(String method, String contentType, byte[] content) {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod(method);
		request.setContentType(contentType);
		request.setContent(content);
		return request;
	}

}
