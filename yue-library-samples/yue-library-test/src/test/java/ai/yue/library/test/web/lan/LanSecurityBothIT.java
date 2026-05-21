package ai.yue.library.test.web.lan;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
		"yue.web.lan-security.basic-auth.enabled=true",
		"yue.web.lan-security.basic-auth.username=bothadmin",
		"yue.web.lan-security.basic-auth.password=bothpass123"
})
class LanSecurityBothIT {

	@Resource
	private MockMvc mockMvc;

	@Test
	void lanPing_innerIp_noAuth_shouldReturn200() throws Exception {
		mockMvc.perform(get("/lan/v1/test/ping"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.data").value("pong"));
	}

	@Test
	void lanPing_publicIp_validAuth_shouldReturn200() throws Exception {
		String credentials = Base64.getEncoder().encodeToString("bothadmin:bothpass123".getBytes());
		mockMvc.perform(get("/lan/v1/test/ping").with(request -> {
					request.setRemoteAddr("203.0.113.1");
					return request;
				}).header("Authorization", "Basic " + credentials))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.data").value("pong"));
	}

	@Test
	void lanPing_publicIp_noAuth_shouldReturn401() throws Exception {
		mockMvc.perform(get("/lan/v1/test/ping").with(request -> {
					request.setRemoteAddr("203.0.113.1");
					return request;
				}))
			.andExpect(status().isUnauthorized());
	}
}
