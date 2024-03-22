package ai.yue.library.test.service;

import ai.yue.library.test.dto.FastJsonHttpMessageConverterDTO;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParamService {

    public JSONObject getParamJson() {
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
        paramJson.put("str", "alfjja1@afa...afjIW2323");
        paramJson.put("strChinese", "你大爷");
        paramJson.put("strUrl", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fmedia-cdn.tripadvisor.com%2Fmedia%2Fphoto-s%2F01%2F3e%2F05%2F40%2Fthe-sandbar-that-links.jpg&refer=http%3A%2F%2Fmedia-cdn.tripadvisor.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1613901291&t=ce8beb9cc15509951f4dbb423875427c");
        paramJson.put("inta", "1");
        paramJson.put("intb", "2");
        paramJson.put("longa", "3");
        paramJson.put("longb", 888L);
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
        // fastJsonHttpMessageConverterDTO
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
        fastJsonHttpMessageConverterDTO.setMap2(map2);
        fastJsonHttpMessageConverterDTO.setJsonObject2(jsonObject2);
        fastJsonHttpMessageConverterDTO.setList2(list2);
        paramJson.put("fastJsonHttpMessageConverterDTO", fastJsonHttpMessageConverterDTO);

        return paramJson;
    }

}
