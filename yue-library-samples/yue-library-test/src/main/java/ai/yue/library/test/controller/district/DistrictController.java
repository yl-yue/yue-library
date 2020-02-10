package ai.yue.library.test.controller.district;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.util.ObjectUtils;
import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.ResultInfo;
import ai.yue.library.test.dataobject.DistrictDO;
import ai.yue.library.test.service.district.DistrictService;

/**
 * @author  孙金川
 * @version 创建时间：2018年7月10日
 */
@RestController
@RequestMapping("/district")
public class DistrictController {

	@Autowired
	DistrictService districtService;
	@Autowired
	RestTemplate restTemplate;
	final String url = "https://restapi.amap.com/v3/config/district?key={key}&keywords={keywords}";
	final String key = "121756572d00df3ce48ca8f03c59813d";
	
	/**
	 * 获得省份
	 * @return
	 */
	@GetMapping("/listProvince")
	public Result<List<DistrictDO>> listProvince() {
		return districtService.listProvince();
	}
	
	/**
	 * 获得城市
	 * @param provinceId
	 * @return
	 */
	@GetMapping("/listCity")
	public Result<List<DistrictDO>> listCity(@RequestParam(value = "provinceId", required = false) String provinceId) {
		return districtService.listCity(provinceId);
	}

	/**
	 * 行政区域查询
	 * @param keywords
	 * @return
	 */
	@GetMapping("/listDistrict")
	public Result<List<DistrictDO>> listDistrict(@RequestParam("keywords") String keywords) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("key", key);
		paramMap.put("keywords", keywords);
		JSONObject rest = restTemplate.getForObject(url, JSONObject.class, paramMap);
		JSONObject china = ObjectUtils.toJSONObject(rest.getJSONArray("districts").get(0));
		JSONArray jsonArray = china.getJSONArray("districts");
		jsonArray.forEach(action -> {
			JSONObject json = ObjectUtils.toJSONObject(action);

			try {
				String[] strs = json.getString("center").split(",");
				String lng = strs[0];
				String lat = strs[1];

				JSONObject paramJson = new JSONObject();
				paramJson.put("lng", lng);
				paramJson.put("lat", lat);
				json.put("location", paramJson);
			} catch (Exception e) {
				JSONObject paramJson = new JSONObject();
				paramJson.put("lng", 0);
				paramJson.put("lat", 0);
				json.put("location", paramJson);
			}
		});

		return ResultInfo.success(jsonArray.toJavaList(DistrictDO.class));
	}
	
}
