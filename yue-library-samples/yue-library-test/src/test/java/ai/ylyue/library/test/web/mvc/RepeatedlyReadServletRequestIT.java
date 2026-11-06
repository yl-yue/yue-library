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
	 * <p>请求体含中文：若 getReader 使用平台默认字符集或写死非 UTF-8，读取结果会出现乱码
	 */
	@Test
	void nonMultipart_bodyRepeatable() throws Exception {
		mockMvc.perform(post("/repeatedlyReadServletRequest")
				.contentType("application/json")
				.content("{\"name\":\"中文测试\",\"age\":18}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.flag").value(true))
				.andExpect(jsonPath("$.data[0]").value("{\"name\":\"中文测试\",\"age\":18}"))
				.andExpect(jsonPath("$.data[1]").value("{\"name\":\"中文测试\",\"age\":18}"))
				.andExpect(jsonPath("$.data[2].name").value("中文测试"))
				.andExpect(jsonPath("$.data[3].name").value("中文测试"));
	}

}
