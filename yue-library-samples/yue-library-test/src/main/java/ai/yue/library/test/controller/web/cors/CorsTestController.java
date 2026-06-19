package ai.yue.library.test.controller.web.cors;

import java.util.Collections;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 跨域测试Controller
 * <p>用于验证 CorsFilter 与 Spring Security 共存场景下的 CORS 头行为</p>
 *
 * @author ylyue
 * @since 2025/6/19
 */
@RestController
@RequestMapping("/cors-secure")
public class CorsTestController {

	/**
	 * 测试端点（配合测试专用 SecurityFilterChain 的 authenticated 规则触发 401）
	 */
	@GetMapping("/test")
	public Map<String, Object> test() {
		return Collections.singletonMap("msg", "ok");
	}

}
