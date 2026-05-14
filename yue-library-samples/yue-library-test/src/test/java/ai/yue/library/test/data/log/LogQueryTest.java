package ai.yue.library.test.data.log;

import ai.yue.library.data.log.entity.LoginLogEntity;
import ai.yue.library.data.log.entity.OperLogEntity;
import ai.yue.library.data.log.ipo.LoginLogPageIPO;
import ai.yue.library.data.log.ipo.OperLogPageIPO;
import ai.yue.library.data.log.mapper.LoginLogMapper;
import ai.yue.library.data.log.mapper.OperLogMapper;
import ai.yue.library.data.log.service.LoginLogService;
import ai.yue.library.data.log.service.OperLogService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * 日志查询 Service 单元测试
 * <p>验证分页查询与详情查询逻辑</p>
 *
 * @author ylyue
 * @since 2025/5/14
 */
@SpringBootTest
@TestPropertySource(properties = {
		"yue.data.log.async=false",
		"yue.data.log.query-api.enabled=true"
})
class LogQueryTest {

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

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter("pageNum", "1");
		request.setParameter("pageSize", "10");
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	}

	@AfterEach
	void tearDown() {
		loginLogMapper.delete(null);
		operLogMapper.delete(null);
		RequestContextHolder.resetRequestAttributes();
	}

	@Test
	void pageLoginLog_allFields() {
		long now = System.currentTimeMillis();
		LoginLogEntity entity = LoginLogEntity.builder()
				.username("testuser")
				.ip("127.0.0.1")
				.loginTime(now)
				.status(1)
				.msg("登录成功")
				.build();
		loginLogMapper.insert(entity);

		LoginLogPageIPO ipo = LoginLogPageIPO.builder()
				.username("test")
				.status(1)
				.startTime(now - 1000)
				.endTime(now + 1000)
				.build();
		PageInfo<LoginLogEntity> page = loginLogService.pageLoginLog(ipo);

		assertEquals(1, page.getTotal());
		assertEquals("testuser", page.getList().get(0).getUsername());
	}

	@Test
	void pageLoginLog_noMatch() {
		LoginLogEntity entity = LoginLogEntity.builder()
				.username("admin")
				.ip("127.0.0.1")
				.loginTime(System.currentTimeMillis())
				.status(1)
				.msg("登录成功")
				.build();
		loginLogMapper.insert(entity);

		LoginLogPageIPO ipo = LoginLogPageIPO.builder()
				.username("nonexist")
				.build();
		PageInfo<LoginLogEntity> page = loginLogService.pageLoginLog(ipo);

		assertEquals(0, page.getTotal());
	}

	@Test
	void getLoginLog_found() {
		LoginLogEntity entity = LoginLogEntity.builder()
				.username("admin")
				.ip("127.0.0.1")
				.loginTime(System.currentTimeMillis())
				.status(1)
				.msg("登录成功")
				.build();
		loginLogMapper.insert(entity);

		LoginLogEntity result = loginLogService.getLoginLog(entity.getId());

		assertNotNull(result);
		assertEquals("admin", result.getUsername());
	}

	@Test
	void getLoginLog_notFound() {
		LoginLogEntity result = loginLogService.getLoginLog(999999L);
		assertNull(result);
	}

	@Test
	void pageOperLog_allFields() {
		long now = System.currentTimeMillis();
		OperLogEntity entity = OperLogEntity.builder()
				.title("用户管理")
				.bizType("IMPORT")
				.operType("C")
				.username("admin")
				.ip("127.0.0.1")
				.operTime(now)
				.requestUrl("/api/user")
				.requestMethod("POST")
				.status(1)
				.build();
		operLogMapper.insert(entity);

		OperLogPageIPO ipo = OperLogPageIPO.builder()
				.title("用户")
				.bizType("IMPORT")
				.operType("C")
				.username("adm")
				.status(1)
				.startTime(now - 1000)
				.endTime(now + 1000)
				.build();
		PageInfo<OperLogEntity> page = operLogService.pageOperLog(ipo);

		assertEquals(1, page.getTotal());
		assertEquals("用户管理", page.getList().get(0).getTitle());
	}

	@Test
	void pageOperLog_filterByOperType() {
		long now = System.currentTimeMillis();
		OperLogEntity c = OperLogEntity.builder().title("新增").operType("C").username("admin").ip("127.0.0.1").operTime(now).status(1).build();
		OperLogEntity r = OperLogEntity.builder().title("查询").operType("R").username("admin").ip("127.0.0.1").operTime(now).status(1).build();
		operLogMapper.insert(c);
		operLogMapper.insert(r);

		OperLogPageIPO ipo = OperLogPageIPO.builder().operType("C").build();
		PageInfo<OperLogEntity> page = operLogService.pageOperLog(ipo);

		assertEquals(1, page.getTotal());
		assertEquals("新增", page.getList().get(0).getTitle());
	}

	@Test
	void getOperLog_found() {
		OperLogEntity entity = OperLogEntity.builder()
				.title("测试操作")
				.operType("R")
				.username("admin")
				.ip("127.0.0.1")
				.operTime(System.currentTimeMillis())
				.status(1)
				.build();
		operLogMapper.insert(entity);

		OperLogEntity result = operLogService.getOperLog(entity.getId());

		assertNotNull(result);
		assertEquals("测试操作", result.getTitle());
	}

	@Test
	void getOperLog_notFound() {
		OperLogEntity result = operLogService.getOperLog(999999L);
		assertNull(result);
	}

}