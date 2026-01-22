package ai.yue.library.data.redis.cache;

import com.alibaba.fastjson2.JSON;
import com.alicp.jetcache.support.AbstractJsonEncoder;

import java.nio.charset.StandardCharsets;

public class Fastjson2ValueEncoder extends AbstractJsonEncoder {

    public static final Fastjson2ValueEncoder INSTANCE = new Fastjson2ValueEncoder(false);

    public Fastjson2ValueEncoder(boolean useIdentityNumber) {
        super(useIdentityNumber);
    }

    @Override
    protected byte[] encodeSingleValue(Object value) {
        return JSON.toJSONBytes(value, StandardCharsets.UTF_8);
    }

}
