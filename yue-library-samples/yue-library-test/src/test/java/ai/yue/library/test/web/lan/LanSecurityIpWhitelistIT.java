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
		"yue.web.lan-security.basic-auth.enabled=false"
})
class LanSecurityIpWhitelistIT {

	@Resource
	private MockMvc mockMvc;

	@Test
	void lanPing_innerIp_shouldReturn200() throws Exception {
		mockMvc.perform(get("/lan/v1/test/ping"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.data").value("pong"));
	}
}
