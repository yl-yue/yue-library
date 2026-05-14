package ai.yue.library.test.data.log;

import ai.yue.library.data.log.entity.LoginLogEntity;
import ai.yue.library.data.log.entity.OperLogEntity;
import ai.yue.library.data.log.mapper.LoginLogMapper;
import ai.yue.library.data.log.mapper.OperLogMapper;
import com.alibaba.fastjson2.JSONObject;
import cn.hutool.v7.core.collection.CollUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 日志模块使用场景模拟测试
 * <p>在示例项目中模拟真实使用场景，验证框架能力端到端行为</p>
 *
 * @author ylyue
 * @since 2025/5/13
 */
@SpringBootTest
@AutoConfigureMockMvc
class LogIT {

    @Resource
    private MockMvc mockMvc;

    @Resource
    private OperLogMapper operLogMapper;

    @Resource
    private LoginLogMapper loginLogMapper;

    @BeforeEach
    void cleanUp() {
        operLogMapper.delete(null);
        loginLogMapper.delete(null);
    }

    @Test
    void operLog_getRequest_inferR() throws Exception {
        mockMvc.perform(get("/data/log/get")
                        .param("username", "admin")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true));

        List<OperLogEntity> logs = operLogMapper.selectList(null);
        assertEquals(1, logs.size());
        OperLogEntity logEntity = logs.get(0);
        assertEquals("查询用户", logEntity.getTitle());
        assertEquals("R", logEntity.getOperType());
        assertEquals(1, logEntity.getStatus());
        assertNotNull(logEntity.getRequestParam());
        assertTrue(logEntity.getRequestParam().contains("admin"));
    }

    @Test
    void operLog_postRequest_inferC() throws Exception {
        mockMvc.perform(post("/data/log/post")
                        .param("username", "admin")
                        .param("password", "123456")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true));

        List<OperLogEntity> logs = operLogMapper.selectList(null);
        assertEquals(1, logs.size());
        OperLogEntity logEntity = logs.get(0);
        assertEquals("新增用户", logEntity.getTitle());
        assertEquals("C", logEntity.getOperType());
        assertEquals(1, logEntity.getStatus());
        assertNotNull(logEntity.getRequestParam());
        assertTrue(logEntity.getRequestParam().contains("admin"));
        assertFalse(logEntity.getRequestParam().contains("123456"));
    }

    @Test
    void operLog_putRequest_inferU() throws Exception {
        mockMvc.perform(put("/data/log/put")
                        .param("username", "admin")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());

        List<OperLogEntity> logs = operLogMapper.selectList(null);
        assertEquals(1, logs.size());
        assertEquals("U", logs.get(0).getOperType());
    }

    @Test
    void operLog_patchRequest_inferU() throws Exception {
        mockMvc.perform(patch("/data/log/patch")
                        .param("username", "admin")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());

        List<OperLogEntity> logs = operLogMapper.selectList(null);
        assertEquals(1, logs.size());
        assertEquals("U", logs.get(0).getOperType());
    }

    @Test
    void operLog_deleteRequest_inferD() throws Exception {
        mockMvc.perform(delete("/data/log/delete")
                        .param("username", "admin")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());

        List<OperLogEntity> logs = operLogMapper.selectList(null);
        assertEquals(1, logs.size());
        assertEquals("D", logs.get(0).getOperType());
    }

    @Test
    void operLog_explicitOperTypeAndBizType() throws Exception {
        mockMvc.perform(post("/data/log/import")
                        .param("data", "test")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());

        List<OperLogEntity> logs = operLogMapper.selectList(null);
        assertEquals(1, logs.size());
        OperLogEntity logEntity = logs.get(0);
        assertEquals("C", logEntity.getOperType());
        assertEquals("IMPORT", logEntity.getBizType());
        assertEquals("导入数据", logEntity.getTitle());
    }

    @Test
    void operLog_failStatus() throws Exception {
        mockMvc.perform(post("/data/log/fail")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(false));

        List<OperLogEntity> logs = operLogMapper.selectList(null);
        assertEquals(1, logs.size());
        assertEquals(0, logs.get(0).getStatus());
    }

    @Test
    void operLog_exceptionStatus() throws Exception {
        mockMvc.perform(post("/data/log/exception")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is5xxServerError());

        List<OperLogEntity> logs = operLogMapper.selectList(null);
        assertEquals(1, logs.size());
        assertEquals(0, logs.get(0).getStatus());
    }

    @Test
    void operLog_responseResult_jsonFormat() throws Exception {
        mockMvc.perform(get("/data/log/get")
                        .param("username", "admin")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());

        List<OperLogEntity> logs = operLogMapper.selectList(null);
        assertEquals(1, logs.size());
        String responseResult = logs.get(0).getResponseResult();
        assertNotNull(responseResult);
        assertTrue(responseResult.startsWith("{"));
        assertTrue(responseResult.contains("admin"));
    }

    @Test
    void operLog_requestParam_formUrlencoded() throws Exception {
        mockMvc.perform(post("/data/log/post")
                        .param("username", "testuser")
                        .param("password", "secret")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());

        List<OperLogEntity> logs = operLogMapper.selectList(null);
        assertEquals(1, logs.size());
        String requestParam = logs.get(0).getRequestParam();
        assertNotNull(requestParam);
        assertTrue(requestParam.contains("testuser"));
        assertFalse(requestParam.contains("secret"));
    }

    @Test
    void operLog_requestParam_jsonBody() throws Exception {
        JSONObject body = new JSONObject();
        body.put("username", "jsonuser");
        body.put("password", "jsonsecret");

        mockMvc.perform(post("/data/log/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.toJSONString()))
                .andExpect(status().isOk());

        List<OperLogEntity> logs = operLogMapper.selectList(null);
        assertEquals(1, logs.size());
        String requestParam = logs.get(0).getRequestParam();
        assertNotNull(requestParam);
    }

    @Test
    void loginLog_recordLogin() throws Exception {
        mockMvc.perform(post("/data/log/loginLog")
                        .param("username", "admin")
                        .param("success", "true")
                        .param("msg", "登录成功")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());

        List<LoginLogEntity> logs = loginLogMapper.selectList(null);
        assertEquals(1, logs.size());
        LoginLogEntity logEntity = logs.get(0);
        assertEquals("admin", logEntity.getUsername());
        assertEquals(1, logEntity.getStatus());
        assertEquals("登录成功", logEntity.getMsg());
    }

    @Test
    void loginLog_recordLoginFail() throws Exception {
        mockMvc.perform(post("/data/log/loginLog")
                        .param("username", "admin")
                        .param("success", "false")
                        .param("msg", "密码错误")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());

        List<LoginLogEntity> logs = loginLogMapper.selectList(null);
        assertEquals(1, logs.size());
        LoginLogEntity logEntity = logs.get(0);
        assertEquals("admin", logEntity.getUsername());
        assertEquals(0, logEntity.getStatus());
        assertEquals("密码错误", logEntity.getMsg());
    }

    @Test
    void noLogAnnotation_noRecord() throws Exception {
        mockMvc.perform(get("/data/log/noLog")
                        .param("username", "admin")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());

        List<OperLogEntity> logs = operLogMapper.selectList(null);
        assertEquals(0, logs.size());
    }

}
