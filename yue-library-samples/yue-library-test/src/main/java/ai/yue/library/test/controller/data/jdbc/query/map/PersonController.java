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

/**
 * @author	ylyue
 * @since	2020年11月2日
 */
@RestController
@RequestMapping("/person")
public class PersonController {

	@Autowired
	PersonDAO personDAO;
	
	@GetMapping("/{id}")
	public Result<?> get(@PathVariable Long id) {
		PersonDO personDO = personDAO.get(id);
		PersonVO personVO = Convert.toJavaBean(personDO, PersonVO.class);
		return R.success(personVO);
	}
	
	public static void main(String[] args) {
		String json = "{\"uid\": \"5635b5b4057248ef9099fab158894dfc\", \"url\": \"/UIE201908290944556888286FFCEA718.jpg\", \"name\": \"UIE201908290944556888286FFCEA718.jpg\", \"size\": 1000, \"type\": \"image/jpeg\", \"bucket\": \"headimg\", \"object\": \"UIE201908290944556888286FFCEA718.jpg\", \"status\": \"done\", \"ossType\": \"MINIO\"}";
		String array = "[{\"uid\": \"5635b5b4057248ef9099fab158894dfc\", \"url\": \"/UIE201908290944556888286FFCEA718.jpg\", \"name\": \"UIE201908290944556888286FFCEA718.jpg\", \"size\": 1000, \"type\": \"image/jpeg\", \"bucket\": \"headimg\", \"object\": \"UIE201908290944556888286FFCEA718.jpg\", \"status\": \"done\", \"ossType\": \"MINIO\"}]";
		JSONObject parseObject = JSONObject.parseObject(json);
		JSONArray parseArray = JSONArray.parseArray(array);
		System.out.println(parseObject);
		System.out.println(parseArray);
	}
	
}
