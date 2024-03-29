package ai.yue.library.test.controller.data.redis;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.entity.TableExampleStandard;
import ai.yue.library.test.ipo.TableExampleIPO;
import ai.yue.library.test.service.TableExampleStandardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    TableExampleStandardService tableExampleService;
    @Autowired
    RedisSerializerController redisSerializerController;

    /**
     * 以`cache_test`作为缓存名，参数`id`作为缓存key，如果命中缓存，直接返回结果。如果缓存不存在，就执行方法逻辑，并将方法的返回结果缓存。
     */
    @Cacheable(value = "cache_test", key = "#id")
    @GetMapping("/get")
    public Result<?> get(Long id) {
        System.out.println("未命中Redis缓存，使用JDBC去数据库查询数据。");
        return R.success(tableExampleService.getById(id));
    }

    /**
     * 以`cache_test`作为缓存名，参数`id`作为缓存key，当方法逻辑执行完之后，将返回结果进行覆盖缓存。
     */
    @CachePut(value = "cache_test", key = "#userGroupIPO.id")
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

}
