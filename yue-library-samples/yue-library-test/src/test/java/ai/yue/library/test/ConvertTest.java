package ai.yue.library.test;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.base.convert.ConvertDO;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 类型转换器测试
 *
 * @author	ylyue
 * @since	2019年7月23日
 */
@Slf4j
public class ConvertTest {

	RestTemplate restTemplate = new RestTemplate();

	@Test
	public void toJavaBean() {
		for (int i = 0; i < 10000; i++) {
			Result forObject = restTemplate.getForObject("http://localhost:8080/auth/v5.0/apiVersion/get?cellphone=18523146316&id=1", Result.class);
			Assert.assertEquals("185231463161", forObject.getData());
		}
	}

}
