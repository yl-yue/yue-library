package ai.yue.library.test.base.convert;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.test.dto.ConvertDTO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;
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

	private JSONObject paramJson() {
		Map<String, Object> map = new HashMap();
		map.put("key1", "value1");
		map.put("key2", "value2");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("aaa", 1);
		jsonObject.put("bbb", 2);
		jsonObject.put("ccc", "11111");

		JSONArray jsonArray = new JSONArray();
		jsonArray.add(jsonObject);

		JSONObject paramJson = new JSONObject();
		// JSON - JSON
		paramJson.put("map", map);
		paramJson.put("jsonObject", jsonObject);
		paramJson.put("jsonArray", jsonArray);
		paramJson.put("jsonObjectList", jsonArray);
		// JSONString - JSON
		paramJson.put("strToMap", map);
		paramJson.put("strToJsonObject", jsonObject.toJSONString());
		paramJson.put("strToJsonArray", jsonArray.toJSONString());
		paramJson.put("strToJsonObjectList", jsonArray.toJSONString());

		// 基本类型
		paramJson.put("character", "c");
		paramJson.put("str", "STR");
		paramJson.put("inta", "1");
		paramJson.put("intb", "2");
		paramJson.put("longa", "3");
		paramJson.put("longb", 888l);
		paramJson.put("booleana", "1");
		paramJson.put("booleanb", true);

		// 数组
		paramJson.put("arrayStr", new String[]{"aaaa", "bbbbb", "cccc"});
		paramJson.put("arrayLong", new Long[]{1L, 2L, 3L});
		paramJson.put("list", new String[]{"aaaa", "bbbbb", "cccc"});

		// 时间类型
		paramJson.put("date", "2021-03-23");
		paramJson.put("dateTime", "2021-03-24");
		paramJson.put("localDate", "2021-03-24");
		paramJson.put("localTime", "16:03:24");
		paramJson.put("localDateTime", "2021-03-24 16:03:24");

		// 其它
		paramJson.put("convertEnum", "A_A");

		return paramJson;
	}

	@Test
	public void toJavaBean() {
		ConvertDTO convertDTO = Convert.toJavaBean(paramJson(), ConvertDTO.class);
		Assert.notNull(convertDTO, "Convert.toJavaBean转换失败，结果为：{}", convertDTO);
		log.info("Convert.toJavaBean转换成功，结果为：{}", convertDTO);
	}

	@Test
	public void strToJSONObject() {
		String json = "{\r\n" +
				"    \"key\": 666\r\n" +
				"}";
		JSONObject result = Convert.convert(json, JSONObject.class);
		Assert.notNull(result);
	}

	@Test
	public void parseJson() {
		String json = "{\"uid\": \"5635b5b4057248ef9099fab158894dfc\", \"url\": \"/UIE201908290944556888286FFCEA718.jpg\", \"name\": \"UIE201908290944556888286FFCEA718.jpg\", \"size\": 1000, \"type\": \"image/jpeg\", \"bucket\": \"headimg\", \"object\": \"UIE201908290944556888286FFCEA718.jpg\", \"status\": \"done\", \"ossType\": \"MINIO\"}";
		String array = "[{\"uid\": \"5635b5b4057248ef9099fab158894dfc\", \"url\": \"/UIE201908290944556888286FFCEA718.jpg\", \"name\": \"UIE201908290944556888286FFCEA718.jpg\", \"size\": 1000, \"type\": \"image/jpeg\", \"bucket\": \"headimg\", \"object\": \"UIE201908290944556888286FFCEA718.jpg\", \"status\": \"done\", \"ossType\": \"MINIO\"}]";
		JSONObject parseObject = JSONObject.parseObject(json);
		JSONArray parseArray = JSONArray.parseArray(array);
		System.out.println(parseObject);
		System.out.println(parseArray);
		Map<String, Object> map = new HashMap<>();
		map.put("aaa", 1);
		map.put("bbb", 2);
		map.put("ccc", "11111");
		String s = JSONObject.toJSONString(map.toString());
		System.out.println(s);
	}

	@Test
	public void hutoolMapToBean() {
		JSONObject paramJson = paramJson();
		paramJson.remove("booleana");
		paramJson.remove("booleanb");
		paramJson.put("is_booleana", "1");
		paramJson.put("is_booleanb", true);

		// 加载自定义转换器
		Convert.toJavaBean(paramJson, ConvertDTO.class);
		ConvertDTO convertDTO = BeanUtil.toBean(paramJson, ConvertDTO.class);
		Assert.notNull(convertDTO, "BeanUtil.toBean转换失败，结果为：{}", convertDTO);
		log.info("BeanUtil.toBean转换成功，结果为：{}", convertDTO);

		// 对比fastjson转换结果
		ConvertDTO fastjsonConvertDTO = TypeUtils.castToJavaBean(paramJson, ConvertDTO.class, ParserConfig.getGlobalInstance());
		log.info("TypeUtils.castToJavaBean转换成功，结果为：{}", fastjsonConvertDTO);
	}

	private void println(Object o) {
		System.out.println(Convert.serializer(o));
	}

	@Test
	public void serializer() {
		Integer i1 = 0;
		println(i1);
		Integer i2 = 324;
		println(i2);
		Long l1 = -1L;
		println(l1);
		Double d1 = 0.01D;
		println(d1);
		Date date = new Date();
		println(date);
		LocalDateTime localDateTime = LocalDateTime.now();
		println(localDateTime);
		String str1 = "strDf2@DFA[]FAWE#4$90()[]{}";
		println(str1);
		String json = "{\"uid\": \"5635b5b4057248ef9099fab158894dfc\", \"url\": \"/UIE201908290944556888286FFCEA718.jpg\", \"name\": \"UIE201908290944556888286FFCEA718.jpg\", \"size\": 1000, \"type\": \"image/jpeg\", \"bucket\": \"headimg\", \"object\": \"UIE201908290944556888286FFCEA718.jpg\", \"status\": \"done\", \"ossType\": \"MINIO\"}";
		println(json);
	}

	@Test
	public void deserialize() {
		String d1 = "32.23242D";
		String date = "2024-02-21T10:00:06.689125500";
		String timestamp = "1708480806681";

		Double aDouble = Convert.deserialize(d1, Double.class);
		System.out.println(aDouble);
		Date date1 = Convert.deserialize(date, Date.class);
		System.out.println(date1);
		LocalDateTime localDateTime1 = Convert.deserialize(date, LocalDateTime.class);
		System.out.println(localDateTime1);
		Date date2 = Convert.deserialize(timestamp, Date.class);
		System.out.println(date2);
		LocalDateTime localDateTime2 = Convert.deserialize(timestamp, LocalDateTime.class);
		System.out.println(localDateTime2);

		ConvertDTO convertDTO = Convert.deserialize(paramJson(), ConvertDTO.class);
		System.out.println(convertDTO);
		Assert.notNull(convertDTO, "Convert.toJavaBean转换失败，结果为：{}", convertDTO);
	}

}
