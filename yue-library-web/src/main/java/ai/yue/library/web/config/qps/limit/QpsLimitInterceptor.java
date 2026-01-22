package ai.yue.library.web.config.qps.limit;

import ai.yue.library.base.annotation.QpsLimit;
import ai.yue.library.base.exception.ResultException;
import ai.yue.library.base.view.R;
import ai.yue.library.web.config.properties.WebProperties;
import com.google.common.util.concurrent.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.v7.core.text.StrUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

/**
 * QPS 限流拦截器
 *
 * @author ylyue
 * @since  2021/5/28
 */
@Slf4j
@RequiredArgsConstructor
public class QpsLimitInterceptor implements HandlerInterceptor {

	private static final String GLOBAL_QPS_LIMIT = "checkGlobalQpsLimit";
	final WebProperties webProperties;

	/**
	 * 在控制器（controller方法）执行之前
	 * <p>执行 QPS 接口限流
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}

		// 全局QPS限制
		if (webProperties.isEnableGlobalApiQpsLimit()) {
			checkGlobalQpsLimit();
		}

		// 接口QPS限制
		if (webProperties.isEnableApiQpsLimit()) {
			Method method = ((HandlerMethod) handler).getMethod();

			// 获取目标方法上的 QPS 限流注解
			QpsLimit qpsLimit = method.getAnnotation(QpsLimit.class);
			if (qpsLimit != null) {
				checkApiQpsLimit(request, qpsLimit);
			}
		}

		return true;
	}

	/**
	 * 执行全局 QPS 接口限流, 校验通过则放行, 校验失败则抛出异常, 并通过统一异常处理返回友好提示
	 */
	private void checkGlobalQpsLimit() {
		int qps = webProperties.getGlobalApiQps();
		RateLimiter rateLimiter = RateLimiterContext.cacheRateLimiter(GLOBAL_QPS_LIMIT, qps);

		// QPS判断
		if (rateLimiter.tryAcquire() == false) {
			String dataPrompt = StrUtil.format("【QPS 限流】全局限制 {} QPS，当前 {} QPS", qps, rateLimiter.getRate());
			throw new ResultException(R.qpsLimit(dataPrompt));
		}
	}

	/**
	 * 执行接口 QPS 接口限流, 校验通过则放行, 校验失败则抛出异常, 并通过统一异常处理返回友好提示
	 */
	private void checkApiQpsLimit(HttpServletRequest request, QpsLimit qpsLimit) {
		String requestURI = request.getRequestURI();
		int qps = qpsLimit.qps();
		RateLimiter rateLimiter = RateLimiterContext.cacheRateLimiter(requestURI, qps);

		// QPS判断
		if (rateLimiter.tryAcquire() == false) {
			String dataPrompt = StrUtil.format("【QPS 限流】接口 {} 限制 {} QPS，当前 {} QPS", requestURI, qps, rateLimiter.getRate());
			throw new ResultException(R.qpsLimit(dataPrompt));
		}
	}

}
