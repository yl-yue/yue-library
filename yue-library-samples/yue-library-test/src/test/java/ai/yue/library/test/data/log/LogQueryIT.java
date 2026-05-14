package ai.yue.library.test.data.log;

import ai.yue.library.data.log.entity.LoginLogEntity;
import ai.yue.library.data.log.entity.OperLogEntity;
import ai.yue.library.data.log.mapper.LoginLogMapper;
import ai.yue.library.data.log.mapper.OperLogMapper;
import ai.yue.library.data.log.service.LoginLogService;
import ai.yue.library.data.log.service.OperLogService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 日志查询接口使用场景模拟测试
 * <p>验证 LogQueryController 的分页查询与详情查询能力</p>
 *
 * @author ylyue
 * @since 2025/5/14
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
		"yue.data.log.async=false",
		"yue.data.log.query-api.enabled=true"
})
class LogQueryIT {

	@Resource
	private MockMvc mockMvc;

	@Resource
	private LoginLogMapper loginLogMapper;

	@Resource
	private OperLogMapper operLogMapper;

	@Resource
	private LoginLogService loginLogService;

	@Resource
	private OperLogService operLogService;

	@BeforeEach
	void setUp() {
		loginLogMapper.delete(null);
		operLogMapper.delete(null);

		long now = System.currentTimeMillis();
		for (int i = 0; i < 5; i++) {
			LoginLogEntity loginLog = LoginLogEntity.builder()
					.username("user" + i)
					.ip("192.168.1." + i)
					.loginTime(now - i * 3600 * 1000)
					.status(i % 2)
					.msg(i % 2 == 0 ? "登录成功" : "密码错误")
					.build();
			loginLogMapper.insert(loginLog);
		}

		for (int i = 0; i < 5; i++) {
			OperLogEntity operLog = OperLogEntity.builder()
					.title("操作" + i)
					.bizType(i == 0 ? "IMPORT" : "")
					.operType(i % 2 == 0 ? "C" : "R")
					.username("admin" + i)
					.ip("10.0.0." + i)
					.operTime(now - i * 3600 * 1000)
					.requestUrl("/api/test/" + i)
					.requestMethod(i % 2 == 0 ? "POST" : "GET")
					.status(i % 2)
					.build();
			operLogMapper.insert(operLog);
		}
	}

	@AfterEach
	void cleanUp() {
		loginLogMapper.delete(null);
		operLogMapper.delete(null);
	}

	@Test
	void pageLoginLog_noFilter() throws Exception {
		mockMvc.perform(get("/auth/v1/log/loginLog/page")
						.param("pageNum", "1")
						.param("pageSize", "10")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.flag").value(true))
				.andExpect(jsonPath("$.data.total").value(5))
				.andExpect(jsonPath("$.data.list").isArray());
	}

	@Test
	void pageLoginLog_filterByUsername() throws Exception {
		mockMvc.perform(get("/auth/v1/log/loginLog/page")
						.param("pageNum", "1")
						.param("pageSize", "10")
						.param("username", "user0")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.flag").value(true))
				.andExpect(jsonPath("$.data.total").value(1))
				.andExpect(jsonPath("$.data.list[0].username").value("user0"));
	}

	@Test
	void pageLoginLog_filterByStatus() throws Exception {
		mockMvc.perform(get("/auth/v1/log/loginLog/page")
						.param("pageNum", "1")
						.param("pageSize", "10")
						.param("status", "1")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.flag").value(true))
				.andExpect(jsonPath("$.data.total").value(2));
	}

	@Test
	void getLoginLogById() throws Exception {
		List<LoginLogEntity> allLogs = loginLogMapper.selectList(null);
		Long id = allLogs.get(0).getId();

		mockMvc.perform(get("/auth/v1/log/loginLog/getById")
						.param("id", String.valueOf(id))
						.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.flag").value(true))
				.andExpect(jsonPath("$.data.id").value(id))
				.andExpect(jsonPath("$.data.username").exists());
	}

	@Test
	void pageOperLog_noFilter() throws Exception {
		mockMvc.perform(get("/auth/v1/log/operLog/page")
						.param("pageNum", "1")
						.param("pageSize", "10")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.flag").value(true))
				.andExpect(jsonPath("$.data.total").value(5))
				.andExpect(jsonPath("$.data.list").isArray());
	}

	@Test
	void pageOperLog_filterByBizType() throws Exception {
		mockMvc.perform(get("/auth/v1/log/operLog/page")
						.param("pageNum", "1")
						.param("pageSize", "10")
						.param("bizType", "IMPORT")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.flag").value(true))
				.andExpect(jsonPath("$.data.total").value(1))
				.andExpect(jsonPath("$.data.list[0].bizType").value("IMPORT"));
	}

	@Test
	void pageOperLog_filterByOperType() throws Exception {
		mockMvc.perform(get("/auth/v1/log/operLog/page")
						.param("pageNum", "1")
						.param("pageSize", "10")
						.param("operType", "C")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.flag").value(true))
				.andExpect(jsonPath("$.data.total").value(3));
	}

	@Test
	void getOperLogById() throws Exception {
		List<OperLogEntity> allLogs = operLogMapper.selectList(null);
		Long id = allLogs.get(0).getId();

		mockMvc.perform(get("/auth/v1/log/operLog/getById")
						.param("id", String.valueOf(id))
						.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.flag").value(true))
				.andExpect(jsonPath("$.data.id").value(id))
				.andExpect(jsonPath("$.data.title").exists());
	}

}