package ai.yue.library.data.redis.config;

import ai.yue.library.base.util.ClassUtils;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.redisson.codec.JsonJacksonCodec;

import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 最大兼容的Json序列化与反序列化
 */
public class JsonCodec extends JsonJacksonCodec {

    private static final Class<?>[] types = {Long.class, LocalDateTime.class, LocalDate.class, LocalTime.class};

    @Override
    protected void initTypeInclusion(ObjectMapper mapObjectMapper) {
        TypeResolverBuilder<?> mapTyper = new ObjectMapper.DefaultTypeResolverBuilder(ObjectMapper.DefaultTyping.NON_FINAL) {
            @Override
            public boolean useForType(JavaType t) {
                switch (_appliesFor) {
                    case NON_CONCRETE_AND_ARRAYS:
                        while (t.isArrayType()) {
                            t = t.getContentType();
                        }
                        // fall through
                    case OBJECT_AND_NON_CONCRETE:
                        return t.getRawClass() == Object.class || !t.isConcrete();
                    case NON_FINAL:
                        while (t.isArrayType()) {
                            t = t.getContentType();
                        }
                        // to fix problem with wrong long to int conversion
                        if (ClassUtils.isContains(types, t.getRawClass())) {
                            return true;
                        }
                        if (t.getRawClass() == XMLGregorianCalendar.class) {
                            return false;
                        }
                        return !t.isFinal(); // includes Object.class
                    default:
                        // case JAVA_LANG_OBJECT:
                        return t.getRawClass() == Object.class;
                }
            }
        };

        mapTyper.init(JsonTypeInfo.Id.CLASS, null);
        mapTyper.inclusion(JsonTypeInfo.As.PROPERTY);
        mapObjectMapper
                .setDefaultTyping(mapTyper)
                .registerModule(new JavaTimeModule())
                ;
    }

}
