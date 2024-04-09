package ai.yue.library.test.controller.data.redis;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.redis.client.Redis;
import ai.yue.library.test.entity.TableExampleStandard;
import ai.yue.library.test.ipo.TableExampleIPO;
import ai.yue.library.test.service.TableExampleStandardService;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RMapCache;
import org.redisson.api.map.WriteMode;
import org.redisson.api.options.LocalCachedMapOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

/**
 * Redis缓存示例
 *
 * @author ylyue
 * @since 2021/6/3
 */
@EnableCaching
@RestController
//@CacheConfig(cacheNames = "cache_test")
@RequestMapping("/cache")
public class CacheController {

    @Autowired
    Redis redis;
    @Autowired
    TableExampleStandardService tableExampleService;
    @Autowired
    RedisSerializerController redisSerializerController;

    /**
     * 以`cache_test`作为缓存名，参数`id`作为缓存key，如果命中缓存，直接返回结果。如果缓存不存在，就执行方法逻辑，并将方法的返回结果缓存。
     */
    @Cacheable(value = "cache_test", key = "#id")
    @GetMapping("/getCache")
    public Result<?> getCache(Long id) {
        System.out.println("未命中Redis缓存，使用JDBC去数据库查询数据。");
        return R.success(tableExampleService.getById(id));
    }

    /**
     * 以`cache_test`作为缓存名，参数`id`作为缓存key，当方法逻辑执行完之后，将返回结果进行覆盖缓存。
     */
    @CachePut(value = "cache_test", key = "#tableExampleIPO.id")
    @PutMapping("/put")
    public Result<?> put(@Validated TableExampleIPO tableExampleIPO) {
        tableExampleService.updateById(Convert.toJavaBean(tableExampleIPO, TableExampleStandard.class));
        return R.success(tableExampleService.getById(tableExampleIPO.getId()));
    }

    /**
     * 以`cache_test`作为缓存名，参数`id`作为缓存key，当方法逻辑执行完之后，删除缓存。
     */
    @CacheEvict(value = "cache_test", key = "#id")
    @DeleteMapping("/delete")
    public Result<?> delete(Long id) {
        tableExampleService.removeById(id);
        return R.success();
    }

    @Cacheable("redisSerializer")
    @GetMapping("/cacheableSerializer")
    public Result<?> cacheableSerializer() {
        return redisSerializerController.serializer(null);
    }

    @CacheEvict("redisSerializer")
    @DeleteMapping("/cacheEvictDeserialize")
    public Result<?> cacheEvictDeserialize() {
        return redisSerializerController.deserialize(null);
    }

    // ======二级缓存测试======

    // 测试redis耗时，计算是否走二级缓存
    @GetMapping("/testRedisTime")
    public Result<?> testRedisTime() {
        Object o = redis.get("heartbeat");
        return R.success(o);
    }

    // RMapCache开源版本不支持二级缓存，但支持单map key过期
    @GetMapping("/putMapCache")
    public Result<?> putMapCache(Long id) {
        RMapCache<String, Object> mapCache = redis.getRedisson().getMapCache("mapCache");
        mapCache.put("cacheValue", id);
        return R.success(id);
    }
    @GetMapping("/getMapCache")
    public Result<?> getMapCache() {
        RMapCache<String, Object> mapCache = redis.getRedisson().getMapCache("mapCache");
        return R.success(mapCache.get("cacheValue"));
    }

    // RLocalCachedMap开源版本支持二级缓存，但不支持单map key过期
    RLocalCachedMap<String, Object> localCachedMap;

    @PostConstruct
    public void fun() {
        LocalCachedMapOptions<String, Object> testCache2 = LocalCachedMapOptions.name("localCachedMap");
        testCache2.writeMode(WriteMode.WRITE_BEHIND);
        testCache2.writeRetryAttempts(3);
        localCachedMap  = redis.getRedisson().getLocalCachedMap(testCache2);
    }

    @GetMapping("/putLocalCachedMap")
    public Result<?> putLocalCachedMap(Long id) {
        localCachedMap.put("cacheValue", id);
        return R.success(id);
    }

    @GetMapping("/getLocalCachedMap")
    public Result<?> getLocalCachedMap() {
        return R.success(localCachedMap.get("cacheValue"));
    }

}
