package ai.yue.library.test.web.mvc;

import ai.yue.library.web.config.properties.WebProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 请求体重复读配置项绑定测试
 * <p>校验 {@code yue.web.repeatedly-read-max-body-size} 能按 DataSize 形式（如 8B / 2MB）绑定，
 * 防止配置项名称或类型与文档不一致导致配置静默失效
 *
 * @author ylyue
 */
@SpringBootTest(properties = {
		"yue.web.enable-jvm-idle-optimization=false",
		"yue.web.repeatedly-read-max-body-size=8B"
})
class RepeatedlyReadPropertiesIT {

	@Autowired
	private WebProperties webProperties;

	@Test
	void maxBodySize_bindsDataSize() {
		assertEquals(8L, webProperties.getRepeatedlyReadMaxBodySize().toBytes());
	}

}
