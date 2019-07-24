package ai.yue.library.template.test.base;

import java.time.LocalDateTime;
import java.util.Date;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.template.dataobject.UserDO;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Console;

/**
 * @author	孙金川
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
			UserDO userDO3 = Convert.convert(paramJson, UserDO.class);
			Console.log(userDO);
			Console.log(userDO3);
			Console.log(JSONObject.toJSONString(userDO));
			Console.log(JSONObject.toJSONString(userDO3));
			
			// String
//			String str = "{\"dateTime\":\"2019-07-24T16:02:06.566693300\",\"localDateTime\":\"2019-07-24T16:02:06.566693300\",\"nickname\":\"nickname\",\"stringDateTime\":\"2019-07-24 16:02:06\",\"id\":666666,\"age\":66}";
//			String str = "{\"date\":\"2019-07-24 16:02:06\",\"dateTime\":\"2019-07-24 16:02:06\",\"localDateTime\":\"2019-07-24T16:11:16.274764100\",\"nickname\":\"nickname\",\"id\":666666,\"age\":66}";
//			String str = "{\"date\":1563955326541,\"localDateTime\":\"2019-07-24T16:02:06.566693300\",\"nickname\":\"nickname\",\"stringDateTime\":\"2019-07-24 16:02:06\",\"id\":666666,\"age\":66}";
			String str = paramJson.toJSONString();
//			System.out.println(JSONObject.parseObject(str));
			UserDO strDO = Convert.toJavaBean(str, UserDO.class);
			Console.log(strDO);
			Console.log(JSONObject.toJSONString(strDO));
//			UserDO strDO1 = JSONObject.parseObject(str, UserDO.class, Feature.AllowISO8601DateFormat);
//			Console.log(strDO1);
//			UserDO strDO2 = ObjectUtils.toJavaObject(str, UserDO.class);
//			Console.log(strDO2);
//			UserDO strDO3 = Convert.convert(JSONObject.parseObject(str), UserDO.class);
//			Console.log(strDO3);
//			Console.log(JSONObject.toJSONString(strDO3));
			
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
		String json = "{\r\n" + 
				"    \"key\": 666\r\n" + 
				"}";
//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("key", 666666);
		JSONObject result = Convert.convert(JSONObject.class, json);
		System.out.println(result);
	}
	
}
