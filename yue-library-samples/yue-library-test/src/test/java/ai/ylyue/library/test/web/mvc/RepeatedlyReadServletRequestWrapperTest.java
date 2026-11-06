package ai.yue.library.test.web.mvc;

import ai.yue.library.web.config.argument.resolver.RepeatedlyReadServletRequestWrapper;
import jakarta.servlet.ServletInputStream;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * RepeatedlyReadServletRequestWrapper 单元测试
 * <p>字符集用例的判别性：{@code getReader()} 未指定字符集的实现会按 JVM 平台默认解码，
 * 在 UTF-8 默认平台（Linux/macOS）上「声明 GBK」用例必然失败，因而缺陷可被暴露；
 * 反之若实现忽略请求声明、硬编码 UTF-8，则「声明 GBK」用例同样失败。
 *
 * @author ylyue
 */
class RepeatedlyReadServletRequestWrapperTest {

	private static final String CHINESE = "中文测试";

	private static final Charset GBK = Charset.forName("GBK");

	private static MockHttpServletRequest request(byte[] body, String characterEncoding) {
		MockHttpServletRequest request = new MockHttpServletRequest("POST", "/repeatedlyReadServletRequest");
		request.setContent(body);
		if (characterEncoding != null) {
			request.setCharacterEncoding(characterEncoding);
		}
		return request;
	}

	private static String readAll(BufferedReader reader) {
		return reader.lines().collect(Collectors.joining("\n"));
	}

	@Test
	void getReader_usesDeclaredCharset() throws IOException {
		MockHttpServletRequest request = request(CHINESE.getBytes(GBK), "GBK");
		RepeatedlyReadServletRequestWrapper wrapper = new RepeatedlyReadServletRequestWrapper(request);

		try (BufferedReader reader = wrapper.getReader()) {
			assertEquals(CHINESE, readAll(reader));
		}
	}

	@Test
	void getReader_fallbackUtf8WhenCharsetNotDeclared() throws IOException {
		MockHttpServletRequest request = request(CHINESE.getBytes(StandardCharsets.UTF_8), null);
		RepeatedlyReadServletRequestWrapper wrapper = new RepeatedlyReadServletRequestWrapper(request);

		try (BufferedReader reader = wrapper.getReader()) {
			assertEquals(CHINESE, readAll(reader));
		}
	}

	@Test
	void getReader_fallbackUtf8WhenDeclaredCharsetInvalid() {
		MockHttpServletRequest request = request(CHINESE.getBytes(StandardCharsets.UTF_8), "no-such-charset");
		RepeatedlyReadServletRequestWrapper wrapper = new RepeatedlyReadServletRequestWrapper(request);

		String body = assertDoesNotThrow(() -> {
			try (BufferedReader reader = wrapper.getReader()) {
				return readAll(reader);
			}
		});
		assertEquals(CHINESE, body);
	}

	@Test
	void getReader_repeatable() throws IOException {
		MockHttpServletRequest request = request(CHINESE.getBytes(StandardCharsets.UTF_8), "UTF-8");
		RepeatedlyReadServletRequestWrapper wrapper = new RepeatedlyReadServletRequestWrapper(request);

		try (BufferedReader first = wrapper.getReader(); BufferedReader second = wrapper.getReader()) {
			assertEquals(CHINESE, readAll(first));
			assertEquals(CHINESE, readAll(second));
		}
	}

	@Test
	void getInputStream_followsServletInputStreamContract() throws IOException {		byte[] body = CHINESE.getBytes(StandardCharsets.UTF_8);
		MockHttpServletRequest request = request(body, "UTF-8");
		RepeatedlyReadServletRequestWrapper wrapper = new RepeatedlyReadServletRequestWrapper(request);

		ServletInputStream in = wrapper.getInputStream();
		assertTrue(in.isReady(), "缓存流随时可读，isReady 必须为 true");
		assertFalse(in.isFinished(), "未读时 isFinished 必须为 false");
		assertEquals(body.length, in.available());

		byte[] buffer = new byte[body.length];
		assertEquals(body.length, in.read(buffer, 0, buffer.length));
		assertTrue(in.isFinished(), "读尽后 isFinished 必须为 true");
		assertEquals(0, in.available());

		// 重复读：每次 getInputStream 都是全新的流
		assertEquals(body.length, wrapper.getInputStream().available());
	}

	@Test
	void maxBodySize_withinLimit_repeatable() throws IOException {
		byte[] body = CHINESE.getBytes(StandardCharsets.UTF_8);
		MockHttpServletRequest request = request(body, "UTF-8");
		RepeatedlyReadServletRequestWrapper wrapper = new RepeatedlyReadServletRequestWrapper(request, 1024);

		assertArrayEquals(body, wrapper.getInputStream().readAllBytes());
		assertArrayEquals(body, wrapper.getInputStream().readAllBytes());
	}

	@Test
	void maxBodySize_exceeded_firstReadReturnsFullBody_thenFailsFast() throws IOException {
		byte[] body = "0123456789".getBytes(StandardCharsets.UTF_8);
		MockHttpServletRequest request = request(body, "UTF-8");
		RepeatedlyReadServletRequestWrapper wrapper = new RepeatedlyReadServletRequestWrapper(request, 4);

		// 首次读取必须是完整 body（已读前缀 + 原始流剩余拼接），不能截断
		assertArrayEquals(body, wrapper.getInputStream().readAllBytes());
		// 超限后不再缓存，重复读必须显式失败，不能静默返回空 body
		assertThrows(IllegalStateException.class, wrapper::getInputStream);
	}

	@Test
	void maxBodySize_notPositive_unlimited() throws IOException {
		byte[] body = "0123456789".getBytes(StandardCharsets.UTF_8);
		MockHttpServletRequest request = request(body, "UTF-8");
		RepeatedlyReadServletRequestWrapper wrapper = new RepeatedlyReadServletRequestWrapper(request, 0);

		assertArrayEquals(body, wrapper.getInputStream().readAllBytes());
		assertArrayEquals(body, wrapper.getInputStream().readAllBytes());
	}

}
