package ai.yue.library.web.config.idempotent;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.exception.ParamException;
import ai.yue.library.base.exception.ResultException;
import ai.yue.library.base.util.SpringUtils;
import ai.yue.library.base.view.R;
import ai.yue.library.data.redis.client.Redis;
import ai.yue.library.data.redis.config.properties.RedisProperties;
import ai.yue.library.data.redis.constant.RedisConstant;
import ai.yue.library.data.redis.dto.LockMapInfo;
import ai.yue.library.data.redis.idempotent.ApiIdempotent;
import ai.yue.library.web.util.ServletUtils;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 幂等性校验拦截器
 * <p>{@code @ConditionalOnBean(Redis.class)}代码为象征意义上的必须依赖Redis Bean（因为在这里此注解并不会生效，需在WebMvcConfigurer实现类中控制）</p>
 *
 * @author ylyue
 * @since  2021/5/28
 */
@Slf4j
@ConditionalOnBean(Redis.class)
public class IdempotentInterceptor implements HandlerInterceptor {

	LockMapInfo lockMapInfo;

	/**
	 * 在控制器（controller方法）执行之前
	 * <p>执行接口幂等性校验
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}

		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();

		// 获取目标方法上的幂等注解
		ApiIdempotent apiIdempotent = method.getAnnotation(ApiIdempotent.class);
		if (apiIdempotent != null) {
			// 幂等性校验, 校验通过则放行, 校验失败则抛出异常, 并通过统一异常处理返回友好提示
			checkApiIdempotent(request, apiIdempotent, method);
		}

		return true;
	}

	/**
	 * 在请求执行完毕之后，response响应之前
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 幂等解锁
		if (lockMapInfo != null && lockMapInfo.isLock()) {
			Redis redis = SpringUtils.getBean(Redis.class);
			redis.unlockMap(lockMapInfo);
		}
	}

	private void checkApiIdempotent(HttpServletRequest request, ApiIdempotent apiIdempotent, Method method) {
		// 获得幂等请求参数（加锁key）
		String[] paramKeys = apiIdempotent.paramKeys();
		String paramKeyName = null;
		String paramValueName = null;
		if (ArrayUtil.isEmpty(paramKeys)) {
			paramKeyName = "all";
			paramValueName = "all";
		} else {
			for (int i = 0; i < paramKeys.length; i++) {
				String paramKey = paramKeys[i];
				// 1. header中取
				String value = request.getHeader(paramKey);

				// 2. parameter中取
				if (StrUtil.isBlank(value)) {
					value = request.getParameter(paramKey);
				}

				// 3. body中取
				if (StrUtil.isBlank(value)) {
					// 获取请求体：遵守一种行业默认的行为规范，对GET请求不处理body，避免一些难以解释的复杂问题
					boolean getMethod = ServletUtils.isGetMethod(request);
					if (getMethod == false) {
						String body = ServletUtils.getBody(request);
						if (StrUtil.isNotEmpty(body)) {
							JSONObject jsonBody = Convert.toJSONObject(body);
							value = jsonBody.getString(paramKey);
						}
					}
				}

				// 4. paramKey不存在
				if (StrUtil.isBlank(value)) {
					throw new ParamException(StrUtil.format("【幂等性】幂等校验失败，请求中未包含 {} 参数", paramKey));
				}

				if (i == 0) {
					paramKeyName = paramKey;
					paramValueName = value;
				} else {
					paramKeyName = paramKeyName + "," + paramKey;
					paramValueName = paramValueName + "_" + value;
				}
			}
		}

		// 获得请求执行方法名
		String requestHandlerMethod = method.getDeclaringClass() + "." + method.getName() + "()";

		// 设置Redis
		Redis redis = SpringUtils.getBean(Redis.class);
		String redisKey = RedisConstant.API_IDEMPOTENT_KEY_PREFIX + requestHandlerMethod;

		// map加锁
		int expire = apiIdempotent.expire();
		if (expire == -1) {
			RedisProperties redisProperties = SpringUtils.getBean(RedisProperties.class);
			expire = redisProperties.getApiIdempotentExpire();
		}
		lockMapInfo = redis.lockMap(redisKey, paramValueName, expire);

		// 幂等判断
		if (lockMapInfo.isLock() == false) {
			String dataPrompt = StrUtil.format("【幂等性】幂等校验失败，请求校验参数 key: {}，请求校验参数 value: {}", paramKeyName, paramValueName);
			throw new ResultException(R.apiIdempotent(dataPrompt));
		}
	}

}
