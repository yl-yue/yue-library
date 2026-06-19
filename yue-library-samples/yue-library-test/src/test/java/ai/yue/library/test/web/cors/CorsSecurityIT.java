package ai.yue.library.test.web.cors;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 跨域与Spring Security共存场景测试
 * <p>使用真实Servlet容器(RANDOM_PORT)，准确验证Tomcat环境下的CORS头行为。
 * 端点 CorsTestController(/cors-secure/test) 配合下方测试专用 SecurityFilterChain 触发认证失败。</p>
 *
 * @author ylyue
 * @since 2025/6/19
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(CorsSecurityIT.TestSecurityConfig.class)
class CorsSecurityIT {

	@LocalServerPort
	int port;

	@Resource
	TestRestTemplate restTemplate;

	/**
	 * 测试专用 SecurityFilterChain：对 /cors-secure/** 要求认证，触发认证失败场景
	 * <p>Order(0) 优先于 yue-library 的 actuatorSecurityFilterChain(Order 2)</p>
	 */
	@TestConfiguration
	static class TestSecurityConfig {

		@Bean
		@Order(0)
		SecurityFilterChain corsSecureTestChain(HttpSecurity http) throws Exception {
			return http
					.securityMatcher("/cors-secure/**")
					.authorizeHttpRequests(a -> a.anyRequest().authenticated())
					.httpBasic(Customizer.withDefaults())
					.csrf(c -> c.disable())
					.build();
		}
	}

	/**
	 * preflight预检请求应返回CORS头
	 * <p>验证 CorsFilter 正确识别并短路处理 preflight(OPTIONS) 请求</p>
	 */
	@Test
	void preflight_request_shouldReturnCorsHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setOrigin("https://example.com");
		headers.setAccessControlRequestMethod(HttpMethod.GET);
		HttpEntity<Void> entity = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate.exchange(
				"/cors-secure/test", HttpMethod.OPTIONS, entity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getHeaders().getAccessControlAllowOrigin(),
				"preflight响应必须包含Access-Control-Allow-Origin");
	}

	/**
	 * 认证失败的跨域请求应返回CORS头
	 * <p>验证 SecurityFilterChain 认证失败(sendError)时，CorsFilter 先前设置的 CORS 头不被清除。
	 * 注：yue-library 的 ResultErrorController 会将 sendError(401) 转换为 Result(404)，
	 * 故此处断言 4xx 而非具体 401，核心验证点是 CORS 头存在。</p>
	 */
	@Test
	void unauthorizedRequest_withOrigin_shouldReturnCorsHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setOrigin("https://example.com");
		HttpEntity<Void> entity = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate.exchange(
				"/cors-secure/test", HttpMethod.GET, entity, String.class);

		assertTrue(response.getStatusCode().is4xxClientError(),
				"认证失败应返回4xx，实际：" + response.getStatusCode());
		assertNotNull(response.getHeaders().getAccessControlAllowOrigin(),
				"认证失败响应必须包含Access-Control-Allow-Origin，否则浏览器CORS错误");
	}

}
