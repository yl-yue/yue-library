package ai.yue.library.test.data.log;

import ai.yue.library.data.log.entity.LoginLogEntity;
import ai.yue.library.data.log.entity.OperLogEntity;
import ai.yue.library.data.log.mapper.LoginLogMapper;
import ai.yue.library.data.log.mapper.OperLogMapper;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
		"yue.data.log.async=false",
		"yue.data.log.storage-api.enabled=true"
})
class LogStorageProviderDataVerifyIT {

	@Resource
	private MockMvc mockMvc;

	@Resource
	private LoginLogMapper loginLogMapper;

	@Resource
	private OperLogMapper operLogMapper;

	@Test
	void verify_loginLog_insertAndPersist() throws Exception {
		JSONObject body = new JSONObject();
		body.put("username", "verify_admin");
		body.put("ip", "10.10.10.10");
		body.put("loginTime", System.currentTimeMillis());
		body.put("status", 1);
		body.put("msg", "验证数据入库");

		mockMvc.perform(post("/lan/v1/log/loginLog/insert")
						.contentType(MediaType.APPLICATION_JSON)
						.content(body.toJSONString()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.flag").value(true));

		List<LoginLogEntity> logs = loginLogMapper.selectList(null);
		log.info("【数据验证】登录日志查询结果：{}", logs);

		assertTrue(logs.size() >= 1, "登录日志表应该至少有1条数据");
		LoginLogEntity inserted = logs.stream()
				.filter(l -> "verify_admin".equals(l.getUsername()))
				.findFirst()
				.orElse(null);
		assertNotNull(inserted, "应该能找到 username=verify_admin 的记录");
		assertEquals("10.10.10.10", inserted.getIp());
		assertEquals(1, inserted.getStatus());
		assertEquals("验证数据入库", inserted.getMsg());
		log.info("【数据验证】登录日志字段验证通过：id={}, username={}, ip={}, status={}, msg={}, createUser={}, createTime={}, tenantCoId={}",
				inserted.getId(), inserted.getUsername(), inserted.getIp(), inserted.getStatus(), inserted.getMsg(),
				inserted.getCreateUser(), inserted.getCreateTime(), inserted.getTenantCoId());
	}

	@Test
	void verify_operLog_insertAndPersist() throws Exception {
		JSONObject body = new JSONObject();
		body.put("title", "验证操作日志入库");
		body.put("operType", "C");
		body.put("bizType", "VERIFY");
		body.put("username", "verify_admin");
		body.put("ip", "10.10.10.10");
		body.put("operTime", System.currentTimeMillis());
		body.put("requestUrl", "/api/verify/insert");
		body.put("requestMethod", "POST");
		body.put("status", 1);

		mockMvc.perform(post("/lan/v1/log/operLog/insert")
						.contentType(MediaType.APPLICATION_JSON)
						.content(body.toJSONString()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.flag").value(true));

		List<OperLogEntity> logs = operLogMapper.selectList(null);
		log.info("【数据验证】操作日志查询结果：{}", logs);

		assertTrue(logs.size() >= 1, "操作日志表应该至少有1条数据");
		OperLogEntity inserted = logs.stream()
				.filter(l -> "验证操作日志入库".equals(l.getTitle()))
				.findFirst()
				.orElse(null);
		assertNotNull(inserted, "应该能找到 title=验证操作日志入库 的记录");
		assertEquals("C", inserted.getOperType());
		assertEquals("VERIFY", inserted.getBizType());
		assertEquals("verify_admin", inserted.getUsername());
		assertEquals("10.10.10.10", inserted.getIp());
		assertEquals("POST", inserted.getRequestMethod());
		assertEquals(1, inserted.getStatus());
		log.info("【数据验证】操作日志字段验证通过：id={}, title={}, operType={}, bizType={}, username={}, ip={}, requestMethod={}, status={}, createUser={}, createTime={}, tenantCoId={}",
				inserted.getId(), inserted.getTitle(), inserted.getOperType(), inserted.getBizType(),
				inserted.getUsername(), inserted.getIp(), inserted.getRequestMethod(), inserted.getStatus(),
				inserted.getCreateUser(), inserted.getCreateTime(), inserted.getTenantCoId());
	}

}