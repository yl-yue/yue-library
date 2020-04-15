package ai.yue.library.test.controller.test.data.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.ResultInfo;
import ai.yue.library.data.redis.client.Redis;
import ai.yue.library.test.dataobject.example.data.jdbc.UserDO;

/**
 * @author	ylyue
 * @since	2020年2月21日
 */
@RestController
@RequestMapping("/test/redis")
public class RedisController {

	@Autowired
	Redis redis;
	@Autowired
	RedisTemplate<Object, Object> redisTemplate;
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	
	/**
	 * 
	 * @param userDO
	 * @return
	 */
	@PostMapping("/redis")
	public Result<?> redis(UserDO userDO) {
//		Redis redis = ApplicationContextUtils.getBean(Redis.class);
//		Redis redis = SpringUtils.getBean(Redis.class);
		String cellphone = userDO.getCellphone();
		redis.set(cellphone, userDO);
		UserDO resultUserDO = redis.get(cellphone, UserDO.class);
		System.out.println(resultUserDO);
		return ResultInfo.success(resultUserDO);
	}
	
	/**
	 * 
	 * @param userDO
	 * @return
	 */
	@PostMapping("/redisTemplate")
	public Result<?> redisTemplate(UserDO userDO) {
		String cellphone = userDO.getCellphone();
		redisTemplate.opsForValue().set(cellphone, userDO);
		UserDO resultUserDO = (UserDO) redisTemplate.opsForValue().get(cellphone);
		System.out.println(resultUserDO);
		return ResultInfo.success(resultUserDO);
	}
	
	/**
	 * 
	 * @param userDO
	 * @return
	 */
	@PostMapping("/stringRedisTemplate")
	public Result<?> stringRedisTemplate(UserDO userDO) {
		String cellphone = userDO.getCellphone();
		stringRedisTemplate.opsForValue().set(cellphone, JSONObject.toJSONString(userDO));
		UserDO resultUserDO = JSONObject.parseObject(stringRedisTemplate.opsForValue().get(cellphone), UserDO.class);
		System.out.println(resultUserDO);
		return ResultInfo.success(resultUserDO);
	}
	
}
