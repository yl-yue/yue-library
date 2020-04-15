package ai.yue.library.data.redis.constant;

import java.io.Serializable;

import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;

/**
 * Redis 存储对象序列/反序列化
 * 
 * @author	ylyue
 * @since	2020年4月15日
 */
public enum RedisSerializerEnum {
	
	/**
	 * 序列化任何 {@link Serializable} 对象
	 */
	JDK {
		@Override
		public RedisSerializer<Object> getRedisSerializer() {
			return new JdkSerializationRedisSerializer();
		}
	},
	FASTJSON {
		@Override
		public RedisSerializer<Object> getRedisSerializer() {
			return new GenericFastJsonRedisSerializer();
		}
	},
	JACKSON {
		@Override
		public RedisSerializer<Object> getRedisSerializer() {
			return new GenericJackson2JsonRedisSerializer();
		}
	};
	
	public abstract RedisSerializer<Object> getRedisSerializer();
	
}
