package ai.yue.library.test.data.log;

import ai.yue.library.data.log.entity.LoginLogEntity;
import ai.yue.library.data.log.entity.OperLogEntity;
import ai.yue.library.data.log.mapper.LoginLogMapper;
import ai.yue.library.data.log.mapper.OperLogMapper;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
		"yue.data.log.async=false",
		"yue.data.log.storage-api.enabled=true"
})
class LogStorageProviderIT {

	@Resource
	private MockMvc mockMvc;

	@Resource
	private LoginLogMapper loginLogMapper;

	@Resource
	private OperLogMapper operLogMapper;

	@AfterEach
	void cleanUp() {
		loginLogMapper.delete(null);
		operLogMapper.delete(null);
	}

	@Test
	void storageApi_insertLoginLog() throws Exception {
		JSONObject body = new JSONObject();
		body.put("username", "admin");
		body.put("ip", "192.168.1.100");
		body.put("loginTime", System.currentTimeMillis());
		body.put("status", 1);
		body.put("msg", "登录成功");

		mockMvc.perform(post("/lan/v1/log/loginLog/insert")
						.contentType(MediaType.APPLICATION_JSON)
						.content(body.toJSONString()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.flag").value(true));

		List<LoginLogEntity> logs = loginLogMapper.selectList(null);
		assertEquals(1, logs.size());
		LoginLogEntity logEntity = logs.get(0);
		assertEquals("admin", logEntity.getUsername());
		assertEquals("192.168.1.100", logEntity.getIp());
		assertEquals(1, logEntity.getStatus());
		assertEquals("登录成功", logEntity.getMsg());
	}

	@Test
	void storageApi_insertLoginLog_fail() throws Exception {
		JSONObject body = new JSONObject();
		body.put("username", "hacker");
		body.put("ip", "10.0.0.1");
		body.put("loginTime", System.currentTimeMillis());
		body.put("status", 0);
		body.put("msg", "密码错误");

		mockMvc.perform(post("/lan/v1/log/loginLog/insert")
						.contentType(MediaType.APPLICATION_JSON)
						.content(body.toJSONString()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.flag").value(true));

		List<LoginLogEntity> logs = loginLogMapper.selectList(null);
		assertEquals(1, logs.size());
		assertEquals(0, logs.get(0).getStatus());
		assertEquals("密码错误", logs.get(0).getMsg());
	}

	@Test
	void storageApi_insertOperLog() throws Exception {
		JSONObject body = new JSONObject();
		body.put("title", "新增订单");
		body.put("operType", "C");
		body.put("username", "admin");
		body.put("ip", "192.168.1.100");
		body.put("operTime", System.currentTimeMillis());
		body.put("requestUrl", "/api/order/insert");
		body.put("requestMethod", "POST");
		body.put("status", 1);

		mockMvc.perform(post("/lan/v1/log/operLog/insert")
						.contentType(MediaType.APPLICATION_JSON)
						.content(body.toJSONString()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.flag").value(true));

		List<OperLogEntity> logs = operLogMapper.selectList(null);
		assertEquals(1, logs.size());
		OperLogEntity logEntity = logs.get(0);
		assertEquals("新增订单", logEntity.getTitle());
		assertEquals("C", logEntity.getOperType());
		assertEquals("admin", logEntity.getUsername());
		assertEquals("192.168.1.100", logEntity.getIp());
		assertEquals("POST", logEntity.getRequestMethod());
		assertEquals(1, logEntity.getStatus());
	}

	@Test
	void storageApi_insertOperLog_withBizType() throws Exception {
		JSONObject body = new JSONObject();
		body.put("title", "导出报表");
		body.put("operType", "R");
		body.put("bizType", "EXPORT");
		body.put("username", "admin");
		body.put("ip", "192.168.1.100");
		body.put("operTime", System.currentTimeMillis());
		body.put("requestUrl", "/api/report/export");
		body.put("requestMethod", "GET");
		body.put("status", 1);

		mockMvc.perform(post("/lan/v1/log/operLog/insert")
						.contentType(MediaType.APPLICATION_JSON)
						.content(body.toJSONString()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.flag").value(true));

		List<OperLogEntity> logs = operLogMapper.selectList(null);
		assertEquals(1, logs.size());
		assertEquals("EXPORT", logs.get(0).getBizType());
		assertEquals("导出报表", logs.get(0).getTitle());
	}

	@Test
	void storageApi_insertOperLog_fail() throws Exception {
		JSONObject body = new JSONObject();
		body.put("title", "删除用户");
		body.put("operType", "D");
		body.put("username", "admin");
		body.put("ip", "192.168.1.100");
		body.put("operTime", System.currentTimeMillis());
		body.put("requestUrl", "/api/user/delete");
		body.put("requestMethod", "DELETE");
		body.put("status", 0);

		mockMvc.perform(post("/lan/v1/log/operLog/insert")
						.contentType(MediaType.APPLICATION_JSON)
						.content(body.toJSONString()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.flag").value(true));

		List<OperLogEntity> logs = operLogMapper.selectList(null);
		assertEquals(1, logs.size());
		assertEquals(0, logs.get(0).getStatus());
	}

}