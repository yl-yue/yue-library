package ai.yue.library.data.redis.idempotent;

import ai.yue.library.base.annotation.api.version.ApiVersion;
import ai.yue.library.base.util.IdUtils;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.redis.client.Redis;
import ai.yue.library.data.redis.config.properties.RedisProperties;
import ai.yue.library.data.redis.constant.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 接口幂等性
 *
 * @author ylyue
 * @since 2021/4/12
 */
@ApiVersion(2.3)
@RestController
@RequestMapping("/open/{version}/apiIdempotent")
@EnableConfigurationProperties({ApiIdempotentProperties.class})
@ConditionalOnProperty(prefix = ApiIdempotentProperties.PREFIX, name = "enabled", havingValue = "true")
public class ApiIdempotentController {

    @Autowired
    ApiIdempotentProperties apiIdempotentProperties;
    @Autowired
    Redis redis;

    /**
     * 获得幂等版本号
     */
    @GetMapping("/getVersion")
    public Result<?> getVersion() {
        String version = IdUtils.getSimpleUUID();
        String key = RedisConstant.API_IDEMPOTENT_KEY_PREFIX + version;
        redis.set(key, version, apiIdempotentProperties.getVersionTimeout());
        return R.success(version);
    }

}
