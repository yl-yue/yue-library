package ai.yue.library.test.base;

import java.time.LocalDateTime;
import java.util.Date;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.test.dataobject.example.data.jdbc.UserDO;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Console;

/**
 * @author	ylyue
 * @since	2019年7月23日
 */
public class ConvertTest {

	@Test
	public void toJavaBean() {
		try {
			// JSON
			JSONObject paramJson = new JSONObject();
			paramJson.put("id", 666666);
			paramJson.put("nickname", "nickname");
			paramJson.put("age", 66);
			paramJson.put("date", new Date());
			paramJson.put("dateTime", new DateTime());
			paramJson.put("localDateTime", LocalDateTime.now());
			Console.log(paramJson);
			UserDO userDO = Convert.toJavaBean(paramJson, UserDO.class);
			UserDO userDO2 = Convert.convert(paramJson, UserDO.class);
			Console.log(userDO);
			Console.log(userDO2);
			Console.log(JSONObject.toJSONString(userDO));
			Console.log(JSONObject.toJSONString(userDO2));
			
			// String
			String str = paramJson.toJSONString();
			UserDO strDO = Convert.toJavaBean(str, UserDO.class);
			Console.log(strDO);
			Console.log(JSONObject.toJSONString(strDO));
			UserDO strDO2 = Convert.convert(str, UserDO.class);
			Console.log(strDO2);
			Console.log(JSONObject.toJSONString(strDO2));
			
			// null
//			JSONObject a = null;
//			JSONObject b = Convert.toJSONObject(null);
//			System.out.println(a);
//			System.out.println(b);
			
//			var b = Convert.toJavaBean(a, null);
//			System.out.println(b);
//			fail("Not yet implemented");
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void convert() {
		try {
			// String - JSONObject
			String json = "{\r\n" + 
					"    \"key\": 666\r\n" + 
					"}";
//			JSONObject jsonObject = new JSONObject();
//			jsonObject.put("key", 666666);
//			ConverterRegistry converterRegistry = ConverterRegistry.getInstance();
//			converterRegistry.putCustom(JSONObject.class, JSONObjectConverter.class);
			JSONObject result = Convert.convert(json, JSONObject.class);
//			JSONObject result = Convert.convert(JSONObject.class, json);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
