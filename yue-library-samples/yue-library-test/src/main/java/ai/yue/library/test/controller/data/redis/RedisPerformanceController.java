package ai.yue.library.test.controller.data.redis;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.redis.client.Redis;
import ai.yue.library.test.service.ParamService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RKeys;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;

/**
 * Redis序列化测试
 *
 * @author ylyue
 * @since 2021/1/22
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/redisPerformance")
public class RedisPerformanceController {

    final Redis redis;
    final ParamService paramService;

    @PostMapping("/performance")
    public Result<?> performance() {
        RKeys keys = redis.getRedisson().getKeys();
        System.out.println(11111111);
        Iterable<String> keysByPattern = keys.getKeysByPattern("test:aaa:bbb", 1000000);
        Iterator<String> iterator = keysByPattern.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            System.out.println(22222222);
            System.out.println(next);
        }
//        for (String s : keysByPattern) {
//            System.out.println(s);
//        }
//        System.out.println(ListUtils.toList(keysByPattern));
        System.out.println(33333333);
        return R.success();
    }

}
