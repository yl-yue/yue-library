package ai.yue.library.test.web.mvc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * RepeatedlyReadServletRequest 使用场景模拟测试
 * <p>验证 Filter 在 Spring 容器中正确注册，且非 multipart 请求 body 可重复读端到端工作正常
 *
 * @author ylyue
 */
@SpringBootTest(properties = "yue.web.enable-jvm-idle-optimization=false")
@AutoConfigureMockMvc
class RepeatedlyReadServletRequestIT {

	@Autowired
	private MockMvc mockMvc;

	/**
	 * 非 multipart 请求经 Filter 包装后，Controller 内多次读取 body 与参数均正常
	 */
	@Test
	void nonMultipart_bodyRepeatable() throws Exception {
		mockMvc.perform(post("/repeatedlyReadServletRequest")
				.contentType("application/json")
				.content("{\"name\":\"ylyue\",\"age\":18}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.flag").value(true));
	}

}
