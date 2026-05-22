package ai.yue.library.test.web.lan;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
		"yue.web.lan-security.basic-auth.enabled=true",
		"yue.web.lan-security.basic-auth.username=testadmin",
		"yue.web.lan-security.basic-auth.password=testpass123",
		"yue.web.lan-security.exclude-path-patterns[0]=/lan/*/callback/**"
})
class LanSecurityExcludePathIT {

	@Resource
	private MockMvc mockMvc;

	@Test
	void excludedPath_publicIp_noAuth_shouldReturn200() throws Exception {
		mockMvc.perform(get("/lan/v1/callback/emqx/status").with(request -> {
					request.setRemoteAddr("203.0.113.1");
					return request;
				}))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.data").value("emqx-callback-status"));
	}

	@Test
	void nonExcludedPath_publicIp_noAuth_shouldReturn401() throws Exception {
		mockMvc.perform(get("/lan/v1/test/ping").with(request -> {
					request.setRemoteAddr("203.0.113.1");
					return request;
				}))
			.andExpect(status().isUnauthorized());
	}

	@Test
	void excludedPath_innerIp_shouldReturn200() throws Exception {
		mockMvc.perform(get("/lan/v1/callback/emqx/status"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.data").value("emqx-callback-status"));
	}
}
