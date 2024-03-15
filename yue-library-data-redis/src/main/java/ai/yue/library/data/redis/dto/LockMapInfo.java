package ai.yue.library.data.redis.dto;

import ai.yue.library.base.util.DateUtils;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Redis Lock
 *
 * @author ylyue
 * @since 2023/2/6
 */
@Data
public class LockMapInfo<K> {

    /**
     * 是否成功拿到锁
     */
    private boolean lock;

    /**
     * 分布式锁的redisKey（redisKey + mapKey = 全局唯一性）
     */
    @NotBlank
    private String redisKey;

    /**
     * 分布式锁的mapKey（redisKey + mapKey = 全局唯一性）
     */
    @NotBlank
    private K mapKey;

    /**
     * 分布式锁的超时时间（单位：毫秒），到期后锁将自动超时
     */
    @NotBlank
    private Integer lockTimeoutMs;

    /**
     * 分布式锁的超时时间戳（<code style="color:red">当前时间戳 + 超时毫秒</code>，即：{@link DateUtils#getTimestamp(int)} + {@link #lockTimeoutMs}）
     */
    @NotBlank
    private String lockTimeoutStamp;

}
