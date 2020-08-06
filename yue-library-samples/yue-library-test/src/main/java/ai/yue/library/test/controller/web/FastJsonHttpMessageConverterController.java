package ai.yue.library.test.controller.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.ResultInfo;
import ai.yue.library.test.dto.FastJsonHttpMessageConverterDTO;

/**
 * @author	ylyue
 * @since	2020年8月4日
 */
@RestController
@RequestMapping("/fastJsonHttpMessageConverter")
public class FastJsonHttpMessageConverterController {

	/**
	 * 
	 * @param paramJson
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("/get")
	public Result<?> get() {
		HashMap<Object, Object> map2 = new HashMap<>();
		Boolean a = null;
		map2.put("aaa", null);
		map2.put(1, null);
		map2.put(null, a);
		
		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put("aaa", null);
		jsonObject2.put("", null);
		jsonObject2.put(null, a);
		
		List list2 = new ArrayList<>();
		Map listMap = null;
		list2.add(null);
		list2.add(a);
		list2.add("");
		list2.add(listMap);
		FastJsonHttpMessageConverterDTO fastJsonHttpMessageConverterDTO = new FastJsonHttpMessageConverterDTO();
//		fastJsonHttpMessageConverterDTO.setMap2(map2);
//		fastJsonHttpMessageConverterDTO.setJsonObject2(jsonObject2);
		fastJsonHttpMessageConverterDTO.setList2(list2);
		
		return ResultInfo.success(fastJsonHttpMessageConverterDTO);
		
//		HashMap<Object, Object> map3 = new HashMap<>();
//		map3.put("aaa", null);
//		map3.put(1, null);
//		map3.put(true, a);
//		return ResultInfo.success(map3);
//		return ResultInfo.success(null);
	}
	
}
