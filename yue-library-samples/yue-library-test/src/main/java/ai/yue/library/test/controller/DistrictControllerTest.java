package ai.yue.library.test.controller;

/**
 * @author  孙金川
 * @version 创建时间：2018年8月3日
 */
public class DistrictControllerTest {

//	/**
//	 * 插入区县
//	 * @return
//	 */
//	@GetMapping("/insertDistrict")
//	public Result<?> insertDistrict() {
//		districtService.listCity(null).getData().forEach(action -> {
//			int adcode = action.getAdcode();
//			String url = "https://restapi.amap.com/v3/config/district?key=121756572d00df3ce48ca8f03c59813d&keywords=" + adcode;
//			System.out.println(adcode);
//			JSONObject rest = restTemplate.getForObject(url, JSONObject.class);
//			JSONObject zg = JSONObject.parseObject(JSONObject.toJSONString(rest.getJSONArray("districts").get(0)));
//			JSONArray jsonArray = zg.getJSONArray("districts");
//			
//			var array = ListUtils.toJSONS(jsonArray);
//			for (JSONObject json : array) {
//				try {
//					String[] strs = json.getString("center").split(",");
//					String lng = strs[0];
//					String lat = strs[1];
//					
//					JSONObject paramJSON = new JSONObject();
//					paramJSON.put("lng", lng);
//					paramJSON.put("lat", lat);
//					json.put("location", paramJSON);
//				}catch (Exception e) {
//					JSONObject paramJSON = new JSONObject();
//					paramJSON.put("lng", 0);
//					paramJSON.put("lat", 0);
//					json.put("location", paramJSON);
//				}
//			}
//			
//			db.insertBatch("district_district", array);
//		});
//		return ResultInfo.success();
//	}
	
//	/**
//	 * 插入城市
//	 * @return
//	 */
//	@GetMapping("/insertCity")
//	public Result<?> insertCity() {
//		districtService.listProvince().getData().forEach(action -> {
//			int adcode = action.getAdcode();
//			if (adcode == 710000) {
//				return;
//			}
//			String url = "https://restapi.amap.com/v3/config/district?key=121756572d00df3ce48ca8f03c59813d&offset=100&keywords=" + adcode;
//			System.out.println(adcode);
//			JSONObject rest = restTemplate.getForObject(url, JSONObject.class);
//			JSONObject zg = JSONObject.parseObject(JSONObject.toJSONString(rest.getJSONArray("districts").get(0)));
//			JSONArray jsonArray = zg.getJSONArray("districts");
//			System.out.println(jsonArray.size());
//			
//			var array = ListUtils.toJSONS(jsonArray);
//			for (JSONObject json : array) {
//				String[] strs = json.getString("center").split(",");
//				String lng = strs[0];
//				String lat = strs[1];
//				
//				JSONObject paramJSON = new JSONObject();
//				paramJSON.put("lng", lng);
//				paramJSON.put("lat", lat);
//				json.put("location", paramJSON);
//			}
//			
//			db.insertBatch("district_city", array);
//		});
//		return ResultInfo.success();
//	}
	
//	@GetMapping
//	public JSONObject get() {
//		String url = "https://restapi.amap.com/v3/config/district?key=121756572d00df3ce48ca8f03c59813d";
//		JSONObject rest = restTemplate.getForObject(url, JSONObject.class);
//		JSONObject zg = JSONObject.parseObject(JSONObject.toJSONString(rest.getJSONArray("districts").get(0)));
//		JSONArray jsonArray = zg.getJSONArray("districts");
//		
//		var array = ListUtils.toJSONS(jsonArray);
//		for (JSONObject json : array) {
//			String[] strs = json.getString("center").split(",");
//			String lng = strs[0];
//			String lat = strs[1];
//			
//			JSONObject paramJSON = new JSONObject();
//			paramJSON.put("lng", lng);
//			paramJSON.put("lat", lat);
//			json.put("location", paramJSON);
//		}
//		
//		db.insertBatch("district_province_copy1", array);
//		return rest;
//	}
	
}
