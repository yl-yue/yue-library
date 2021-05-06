package ai.yue.library.test.base.api.version;

import ai.yue.library.base.view.Result;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.springframework.web.client.RestTemplate;

/**
 * 版本控制性能测试
 *
 * @author	ylyue
 * @since	2019年7月23日
 */
@Slf4j
public class ApiVersionTest {

	private RestTemplate restTemplate = new RestTemplate();

//	@Test
	public void apiVersionPerformanceTest() {
		for (int i = 0; i < 10000; i++) {
			Result forObject = restTemplate.getForObject("http://localhost:8080/auth/v5.0/apiVersion/get?cellphone=18523146316&id=1", Result.class);
			Assertions.assertEquals("185231463161", forObject.getData());
		}
	}

}
