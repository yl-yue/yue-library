package ai.yue.library.test.aspect;

import ai.yue.library.web.util.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * HTTP请求日志
 *
 * @author	ylyue
 * @since	2019年9月25日
 */
@Slf4j
public class HttpRequestInterceptor implements HandlerInterceptor {

	/**
	 * 在控制器（controller方法）执行之前
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 1. 获取request接口信息
		String requestIp = ServletUtils.getClientIP(request);
		String requestUri = request.getMethod() + " " + request.getRequestURI();

		// 2. 获取controller方法信息
		String requestHandlerMethod = "";
		if (handler instanceof HandlerMethod) {
			Method method = ((HandlerMethod) handler).getMethod();
			requestHandlerMethod = method.getDeclaringClass().getName() + "." + method.getName() + "()";
		}

		// 3. 打印日志
		log.info("requestIp={}", requestIp);
		log.info("requestUri={}", requestUri);
		log.info("requestHandlerMethod={}", requestHandlerMethod);
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	/**
	 * 在控制器（controller方法）执行之后，视图渲染之前
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
						   ModelAndView modelAndView) throws Exception {
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	/**
	 * 在请求执行完毕之后，response响应之前
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}

}
