package ai.yue.library.test.controller.data.redis;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.redis.client.Redis;
import ai.yue.library.test.dto.ConvertDTO;
import ai.yue.library.test.service.ParamService;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Redis序列化测试
 *
 * @author ylyue
 * @since 2021/1/22
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/redisSerializer")
public class RedisSerializerController {

    final Redis redis;
    final ParamService paramService;

    @PostMapping("/serializer")
    public Result<?> serializer(Integer serializerNumber) {
        double doubleValue = 332D;
        String str = "332str";
        Date date = new Date();
        LocalDateTime localDateTime = LocalDateTime.now();
        JSONObject map = paramService.getParamJson();
        ConvertDTO javaBean = Convert.toJavaBean(map, ConvertDTO.class);

        // set value
        if (serializerNumber == null || serializerNumber == 1) {
            redis.set("redisSerializer:set:doubleValue", doubleValue);
            redis.set("redisSerializer:set:str", str);
            redis.set("redisSerializer:set:date", date);
            redis.set("redisSerializer:set:localDateTime", localDateTime);
            redis.set("redisSerializer:set:map", map);
            redis.set("redisSerializer:set:javaBean", javaBean);
        }

        // set map
        if (serializerNumber == null || serializerNumber == 2) {
            redis.addMapValue("redisSerializer:addMapValue:doubleValue", str, doubleValue);
            redis.addMapValue("redisSerializer:addMapValue:str", str, str);
            redis.addMapValue("redisSerializer:addMapValue:date", str, date);
            redis.addMapValue("redisSerializer:addMapValue:localDateTime", str, localDateTime);
            redis.addMapValue("redisSerializer:addMapValue:map", 332, map);
            redis.addMapValue("redisSerializer:addMapValue:javaBean", 332, javaBean);
            redis.addMapValue("redisSerializer:addMapValue:map", str, map);
            redis.addMapValue("redisSerializer:addMapValue:javaBean", str, javaBean);
        }

        // set list
        if (serializerNumber == null || serializerNumber == 3) {
            redis.addListValue("redisSerializer:addListValue:doubleValue", doubleValue);
            redis.addListValue("redisSerializer:addListValue:str", str);
            redis.addListValue("redisSerializer:addListValue:date", date);
            redis.addListValue("redisSerializer:addListValue:localDateTime", localDateTime);
            redis.addListValue("redisSerializer:addListValue:map", map);
            redis.addListValue("redisSerializer:addListValue:javaBean", javaBean);
        }

        // redisson value
        Redisson redisson = redis.getRedisson();
        if (serializerNumber == null || serializerNumber == 4) {
            RBucket<Double> bucket1 = redisson.getBucket("redissonSerializer:bucket:doubleValue");
            RBucket<LocalDateTime> bucket2 = redisson.getBucket("redissonSerializer:bucket:localDateTime");
            RBucket<ConvertDTO> bucket3 = redisson.getBucket("redissonSerializer:bucket:javaBean");
            bucket1.set(doubleValue);
            bucket2.set(localDateTime);
            bucket3.set(javaBean);
        }

        // redisson map
        if (serializerNumber == null || serializerNumber == 5) {
            RMap<Long, Double> redissonMap1 = redisson.getMap("redissonSerializer:getMap:doubleValue");
            RMap<String, LocalDateTime> redissonMap2 = redisson.getMap("redissonSerializer:getMap:localDateTime");
            RMap<String, ConvertDTO> redissonMap3 = redisson.getMap("redissonSerializer:getMap:javaBean");
            RMapCache<String, Long> redissonMap4 = redisson.getMapCache("redissonSerializer:getMap:longValue");
            redissonMap1.put(332L, doubleValue);
            redissonMap2.put("332L", localDateTime);
            redissonMap3.put("332L", javaBean);
            redissonMap4.put("longValue",System.currentTimeMillis());
        }

        // redisson list
        if (serializerNumber == null || serializerNumber == 6) {
            RList<Double> redissonList1 = redisson.getList("redissonSerializer:getList:doubleValue");
            RList<LocalDateTime> redissonList2 = redisson.getList("redissonSerializer:getList:localDateTime");
            RList<ConvertDTO> redissonList3 = redisson.getList("redissonSerializer:getList:javaBean");
            redissonList1.add(doubleValue);
            redissonList2.add(localDateTime);
            redissonList3.add(javaBean);
        }

        return R.success(map);
    }

    @GetMapping("/deserialize")
    public Result<?> deserialize(Integer deserializeNumber) {
        String str = "332str";

        // get value
        if (deserializeNumber == null || deserializeNumber == 1) {
            Double doubleValue = redis.get("redisSerializer:set:doubleValue");
            String strValue = redis.get("redisSerializer:set:str");
            Date date = redis.get("redisSerializer:set:date");
            LocalDateTime localDateTime = redis.get("redisSerializer:set:localDateTime");
            JSONObject map = redis.get("redisSerializer:set:map");
            ConvertDTO javaBean = redis.get("redisSerializer:set:javaBean");
            System.out.println("======get value======");
            System.out.println("doubleValue: " + doubleValue);
            System.out.println("strValue: " + strValue);
            System.out.println("date: " + date);
            System.out.println("localDateTime: " + localDateTime);
            System.out.println("map: " + map);
            System.out.println("javaBean: " + javaBean);
            System.out.println();
            System.out.println();
        }

        // get map
        if (deserializeNumber == null || deserializeNumber == 2) {
            Double doubleValue2 = redis.getMapValue("redisSerializer:addMapValue:doubleValue", str);
            String strValue2 = redis.getMapValue("redisSerializer:addMapValue:str", str);
            Date date2 = redis.getMapValue("redisSerializer:addMapValue:date", str);
            LocalDateTime localDateTime2 = redis.getMapValue("redisSerializer:addMapValue:localDateTime", str);
            JSONObject map2 = redis.getMapValue("redisSerializer:addMapValue:map", 332);
            ConvertDTO javaBean2 = redis.getMapValue("redisSerializer:addMapValue:javaBean", 332);
            JSONObject map22 = redis.getMapValue("redisSerializer:addMapValue:map", str);
            ConvertDTO javaBean22 = redis.getMapValue("redisSerializer:addMapValue:javaBean", str);
            System.out.println("======get map======");
            System.out.println("doubleValue2: " + doubleValue2);
            System.out.println("strValue2: " + strValue2);
            System.out.println("date2: " + date2);
            System.out.println("localDateTime2: " + localDateTime2);
            System.out.println("map2: " + map2);
            System.out.println("javaBean2: " + javaBean2);
            System.out.println("map22: " + map22);
            System.out.println("javaBean22: " + javaBean22);
            System.out.println();
            System.out.println();
        }

        // get list
        if (deserializeNumber == null || deserializeNumber == 3) {
            List<Double> listDoubleValue = redis.getListValue("redisSerializer:addListValue:doubleValue", 0, 1);
            String listStr = redis.getListValue("redisSerializer:addListValue:str", 0);
            Date listDate = redis.getListValue("redisSerializer:addListValue:date", 1);
            List<LocalDateTime> listLocalDateTime = redis.getListValue("redisSerializer:addListValue:localDateTime", 0, 1);
            JSONObject listMap = redis.getListValue("redisSerializer:addListValue:map", 0);
            ConvertDTO listJavaBean = redis.getListValue("redisSerializer:addListValue:javaBean", 1);
            System.out.println("======get list======");
            System.out.println("listDoubleValue: " + listDoubleValue);
            System.out.println("listStr: " + listStr);
            System.out.println("listDate: " + listDate);
            System.out.println("listLocalDateTime: " + listLocalDateTime);
            System.out.println("listMap: " + listMap);
            System.out.println("listJavaBean: " + listJavaBean);
            System.out.println();
            System.out.println();
        }

        // redisson value
        Redisson redisson = redis.getRedisson();
        if (deserializeNumber == null || deserializeNumber == 4) {
            RBucket<Double> bucket1 = redisson.getBucket("redissonSerializer:bucket:doubleValue");
            RBucket<LocalDateTime> bucket2 = redisson.getBucket("redissonSerializer:bucket:localDateTime");
            RBucket<ConvertDTO> bucket3 = redisson.getBucket("redissonSerializer:bucket:javaBean");
            Double redissonDouble = bucket1.get();
            LocalDateTime redissonLocalDateTime = bucket2.get();
            ConvertDTO redissonConvertDTO = bucket3.get();
            System.out.println("======redisson value======");
            System.out.println("redissonDouble: " + redissonDouble);
            System.out.println("redissonLocalDateTime: " + redissonLocalDateTime);
            System.out.println("redissonConvertDTO: " + redissonConvertDTO);
            System.out.println();
            System.out.println();
        }

        // redisson map
        if (deserializeNumber == null || deserializeNumber == 5) {
            RMap<Long, Double> redissonMap1 = redisson.getMap("redissonSerializer:getMap:doubleValue");
            RMap<String, LocalDateTime> redissonMap2 = redisson.getMap("redissonSerializer:getMap:localDateTime");
            RMap<String, ConvertDTO> redissonMap3 = redisson.getMap("redissonSerializer:getMap:javaBean");
            RMapCache<String, Long> redissonMap4 = redisson.getMapCache("redissonSerializer:getMap:longValue");
            Double redissonDouble2 = redissonMap1.get(332L);
            LocalDateTime redissonLocalDateTime2 = redissonMap2.get("332L");
            ConvertDTO redissonConvertDTO2 = redissonMap3.get("332L");
            Long longValue = redissonMap4.get("longValue");
            System.out.println("======redisson map======");
            System.out.println("redissonDouble2: " + redissonDouble2);
            System.out.println("redissonLocalDateTime2: " + redissonLocalDateTime2);
            System.out.println("redissonConvertDTO2: " + redissonConvertDTO2);
            System.out.println("longValue: " + longValue);
            System.out.println();
            System.out.println();
        }

        // redisson list
        if (deserializeNumber == null || deserializeNumber == 6) {
            RList<Double> redissonList1 = redisson.getList("redissonSerializer:getList:doubleValue");
            RList<LocalDateTime> redissonList2 = redisson.getList("redissonSerializer:getList:localDateTime");
            RList<ConvertDTO> redissonList3 = redisson.getList("redissonSerializer:getList:javaBean");
            List<Double> doubles = redissonList1.get(0, 1);
            LocalDateTime localDateTimes = redissonList2.get(0);
            ConvertDTO convertDTOS = redissonList3.get(1);
            System.out.println("======redisson list======");
            System.out.println("doubles: " + doubles);
            System.out.println("localDateTimes: " + localDateTimes);
            System.out.println("convertDTOS: " + convertDTOS);
            System.out.println();
            System.out.println();
        }

        return R.success();
    }

}
