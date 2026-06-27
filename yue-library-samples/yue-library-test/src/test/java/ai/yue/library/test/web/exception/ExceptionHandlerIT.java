package ai.yue.library.test.web.exception;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 统一异常处理测试
 * <p>验证 Sa-Token NotRoleException→403 在精细层强制生效（不被业务 Exception 兜底抢占），
 * 以及框架精细处理（ParamException→433）正常工作。
 *
 * @author ylyue
 * @since 2026年6月27日
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(ExceptionHandlerIT.TestBusinessAdviceConfig.class)
class ExceptionHandlerIT {

	@Resource
	private MockMvc mockMvc;

	/**
	 * 测试用业务 advice：模拟业务项目"不小心"定义的 Exception 兜底（返回 code=600 作为覆盖标记）。
	 * <p>未标注 @Order（默认最低优先级），用于验证框架精细 advice（HIGHEST_PRECEDENCE）的
	 * Sa-Token 异常处理不被此业务兜底抢占——若优先级机制失效，将返回 600 而非 403。
	 */
	@TestConfiguration
	@RestControllerAdvice
	static class TestBusinessAdviceConfig {

		@org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
		public Result<?> businessFallback(Exception e) {
			return R.errorPrompt("业务兜底覆盖标记");
		}
	}

	/**
	 * Sa-Token NotRoleException 应被框架精细层识别为 403，不被业务 Exception 兜底抢占
	 */
	@Test
	void notRoleException_shouldReturn403_notOverriddenByBusinessAdvice() throws Exception {
		mockMvc.perform(post("/exception/notroleexception"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(403));
	}

	/**
	 * 框架精细处理的 ParamException 应正常返回 433，不受业务 Exception 兜底影响
	 */
	@Test
	void paramException_shouldReturn433_notOverriddenByBusinessAdvice() throws Exception {
		mockMvc.perform(post("/exception/exception"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(433));
	}

}
