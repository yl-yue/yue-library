package ai.yue.library.test.controller.data.jdbc.query.map;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author	ylyue
 * @since	2020年11月2日
 */
@RestController
@RequestMapping("/queryMap/dataConvert")
public class DataConvertController {

	@Autowired
	PersonDAO personDAO;
	
	@GetMapping("/{id}")
	public Result<?> get(@PathVariable Long id) {
		PersonDO personDO = personDAO.get(id);
		System.out.println(personDO);
		PersonVO personVO = Convert.toJavaBean(personDO, PersonVO.class);
		System.out.println(personVO);
		PersonVO personVO2 = personDAO.getPersonVO(id);
		System.out.println(personVO2);
//		return R.success(personDO);
//		return R.success(personVO);
		return R.success(personVO2);
	}

	public static void main(String[] args) {
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
	
}
