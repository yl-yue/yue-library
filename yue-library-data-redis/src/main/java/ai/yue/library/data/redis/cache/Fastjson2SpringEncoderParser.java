package ai.yue.library.data.redis.cache;

import com.alicp.jetcache.anno.support.DefaultSpringEncoderParser;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class Fastjson2SpringEncoderParser extends DefaultSpringEncoderParser {

    @Override
    public Function<Object, byte[]> parseEncoder(String valueEncoder) {
        return Fastjson2ValueEncoder.INSTANCE;
    }

    @Override
    public Function<byte[], Object> parseDecoder(String valueDecoder) {
        return Fastjson2ValueDecoder.INSTANCE;
    }

}
