package ai.yue.library.web.config.idempotent;

import ai.yue.library.base.exception.ParamException;
import ai.yue.library.base.exception.ResultException;
import ai.yue.library.base.util.SpringUtils;
import ai.yue.library.base.view.R;
import ai.yue.library.data.redis.client.Redis;
import ai.yue.library.data.redis.constant.RedisConstant;
import ai.yue.library.data.redis.idempotent.ApiIdempotent;
import cn.hutool.core.util.StrUtil;
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

	/**
	 * 在控制器（controller方法）执行之前
	 * <p>执行接口幂等性校验
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}

		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();

		// 获取目标方法上的幂等注解
		ApiIdempotent methodAnnotation = method.getAnnotation(ApiIdempotent.class);
		if (methodAnnotation != null) {
			// 幂等性校验, 校验通过则放行, 校验失败则抛出异常, 并通过统一异常处理返回友好提示
			checkApiIdempotent(request);
		}

		return true;
	}

	private void checkApiIdempotent(HttpServletRequest request) {
		String version = request.getHeader(RedisConstant.API_IDEMPOTENT_VERSION_REQUEST_KEY);
		if (StrUtil.isBlank(version)) {
			// header中不存在version
			version = request.getParameter(RedisConstant.API_IDEMPOTENT_VERSION_REQUEST_KEY);
			if (StrUtil.isBlank(version)) {
				// parameter中也不存在version
				throw new ParamException(StrUtil.format("【幂等性】幂等校验失败，请求中未包含 {} 参数", RedisConstant.API_IDEMPOTENT_VERSION_REQUEST_KEY));
			}
		}

		Redis redis = SpringUtils.getBean(Redis.class);
		String redisKey = RedisConstant.API_IDEMPOTENT_KEY_PREFIX + version;
		if (!redis.getRedisTemplate().hasKey(redisKey)) {
			String msgPrompt = "请勿重复操作";
			String dataPrompt = StrUtil.format("【幂等性】幂等校验失败，{} 参数已失效，当前 value: {}", RedisConstant.API_IDEMPOTENT_VERSION_REQUEST_KEY, version);
			throw new ResultException(R.errorPrompt(msgPrompt, dataPrompt));
		}

		if (!redis.del(redisKey)) {
			log.warn("【幂等性】幂等校验失败，{} 参数未能正确的解锁", redisKey);
		}
	}

}
