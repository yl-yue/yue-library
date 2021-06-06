package ai.yue.library.test.controller.data.redis;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.dao.data.jdbc.JdbcDAO;
import ai.yue.library.test.dataobject.jdbc.UserDO;
import ai.yue.library.test.ipo.UserGroupIPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Redis缓存示例
 *
 * @author ylyue
 * @since 2021/6/3
 */
@EnableCaching
//@CacheConfig(cacheNames = "cache_test")
@RestController
@RequestMapping("/cache")
public class CacheController {

    @Autowired
    JdbcDAO jdbcDAO;

    /**
     * 以`cache_test`作为缓存名，参数`id`作为缓存key，如果命中缓存，直接返回结果。如果缓存不存在，就执行方法逻辑，并将方法的返回结果缓存。
     */
    @Cacheable(value = "cache_test", key = "#id")
    @GetMapping("/get")
    public Result<?> get(Long id) {
        System.out.println("未命中Redis缓存，使用JDBC去数据库查询数据。");
        return R.success(jdbcDAO.get(id));
    }

    /**
     * 以`cache_test`作为缓存名，参数`id`作为缓存key，当方法逻辑执行完之后，将返回结果进行覆盖缓存。
     */
    @CachePut(value = "cache_test", key = "#userGroupIPO.id")
    @PutMapping("/put")
    public Result<?> put(@Validated UserGroupIPO userGroupIPO) {
        jdbcDAO.updateById(Convert.toJSONObject(userGroupIPO));
        return R.success(jdbcDAO.get(userGroupIPO.getId()));
    }

    /**
     * 以`cache_test`作为缓存名，参数`id`作为缓存key，当方法逻辑执行完之后，删除缓存。
     */
    @CacheEvict(value = "cache_test", key = "#id")
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam("id") Long id) {
        jdbcDAO.deleteLogic(id);
        return R.success();
    }

    @Caching(
        cacheable = {
            @Cacheable(value = "cache_test", key = "#id")
        },
        put = {
            @CachePut(value = "cache_test", key = "#result.cellphone", condition = "#result != null"),
            @CachePut(value = "cache_test", key = "#result.email", condition = "#result != null")
        }
    )
    public UserDO getUserDO(Long id) {
        return jdbcDAO.get(id);
    }

}
