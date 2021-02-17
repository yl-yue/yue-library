package ai.yue.library.test.controller.data.redis.serializer;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.redis.client.Redis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Redis序列化测试
 *
 * @author ylyue
 * @since 2021/1/22
 */
@RestController
@RequestMapping("/redisSerializer")
public class RedisSerializerController {

    @Autowired
    Redis redis;
    private static final String redisSerializerKey = "redisSerializer";

    @PostMapping("/post")
    public Result<?> post(SerializerIPO serializerIPO) {
        redis.set(redisSerializerKey, serializerIPO);
        return R.success(serializerIPO);
    }

    @GetMapping("/get")
    public Result<?> get() {
        return R.success(redis.get(redisSerializerKey, SerializerIPO.class));
    }

}
