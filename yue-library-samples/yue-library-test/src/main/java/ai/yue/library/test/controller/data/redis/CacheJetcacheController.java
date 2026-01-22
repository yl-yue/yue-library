package ai.yue.library.test.controller.data.redis;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.entity.TableExampleStandard;
import ai.yue.library.test.ipo.TableExampleIPO;
import ai.yue.library.test.service.TableExampleStandardService;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CacheUpdate;
import com.alicp.jetcache.anno.Cached;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.alicp.jetcache.template.QuickConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Redis缓存示例
 *
 * @author ylyue
 * @since 2021/6/3
 */
@RestController
@RequestMapping("/cacheJetcache")
@EnableMethodCache(basePackages = "ai.yue.library.test")
public class CacheJetcacheController {

    @Autowired
    TableExampleStandardService tableExampleService;
    @Autowired
    RedisSerializerController redisSerializerController;

    @GetMapping("/getCache")
    @Cached(name = "cacheTest:", key = "#id", cacheType = CacheType.BOTH, syncLocal = true, expire = 3600)
    public Result<?> getCache(Long id) {
        System.out.println("未命中Redis缓存，使用JDBC去数据库查询数据。");
        return R.success(tableExampleService.getById(id));
    }

    @PutMapping("/put")
    @CacheUpdate(name = "cacheTest:", key = "#tableExampleIPO.id", value = "#result")
    public Result<?> put(@Validated TableExampleIPO tableExampleIPO) {
        tableExampleService.updateById(Convert.toJavaBean(tableExampleIPO, TableExampleStandard.class));
        return R.success(tableExampleService.getById(tableExampleIPO.getId()));
    }

    @DeleteMapping("/delete")
    @CacheInvalidate(name="cacheTest:", key="#id")
    public Result<?> delete(Long id) {
        tableExampleService.removeById(id);
        return R.success();
    }

    @Cached(name = "redisSerializer:", cacheType = CacheType.BOTH, syncLocal = true, expire = 3600)
    @GetMapping("/cacheableSerializer")
    public Result<?> cacheableSerializer() {
        return redisSerializerController.serializer(null);
    }

    @CacheInvalidate(name = "redisSerializer:")
    @DeleteMapping("/cacheEvictDeserialize")
    public Result<?> cacheEvictDeserialize() {
        return redisSerializerController.deserialize(null);
    }

    // ====== api ======

    @Autowired
    private CacheManager cacheManager;
    private Cache<Long, TableExampleStandard> cacheTest;

    @PostConstruct
    public void init() {
        QuickConfig qc = QuickConfig.newBuilder("cacheTestBatch:")
                .expire(Duration.ofSeconds(100))
                .cacheType(CacheType.BOTH) // two level cache
                .syncLocal(true) // invalidate local cache in all jvm process after update
                .build();
        cacheTest = cacheManager.getOrCreateCache(qc);
    }

    @GetMapping("/getCacheBatch")
    public Result<?> getCacheBatch(List<Long> ids) {
        // 查询缓存
        HashSet<Long> hashSet = new HashSet<>();
        hashSet.addAll(ids);
        Map<Long, TableExampleStandard> all = cacheTest.getAll(hashSet);

        if (all.size() != ids.size()) {
            System.out.println("未命中Redis缓存，使用JDBC去数据库查询数据。");
            // 查询数据库
            List<TableExampleStandard> list = tableExampleService.listByIds(ids);
            for (TableExampleStandard tableExampleStandard : list) {
                all.put(tableExampleStandard.getId(), tableExampleStandard);
            }

            // 设置缓存
            cacheTest.putAll(all);
        }

        return R.success(all);
    }

    public Result<?> getCacheBatch2(HashSet<Long> ids) {
        // 查询缓存
        Map<Long, TableExampleStandard> all = cacheTest.getAll(ids);

        if (all.size() != ids.size()) {
            System.out.println("未命中Redis缓存，使用JDBC去数据库查询数据。");
            // 查询数据库
            List<TableExampleStandard> list = tableExampleService.listByIds(ids);
            for (TableExampleStandard tableExampleStandard : list) {
                all.put(tableExampleStandard.getId(), tableExampleStandard);
            }

            // 设置缓存
            cacheTest.putAll(all);
        }

        return R.success(all);
    }

}
