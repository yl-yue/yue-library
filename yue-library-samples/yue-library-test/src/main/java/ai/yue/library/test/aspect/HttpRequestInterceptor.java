package ai.yue.library.test.aspect;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

/**
 * @author	ylyue
 * @version 创建时间：2018年12月3日
 */
@Slf4j
public class HttpRequestInterceptor implements HandlerInterceptor {

	/**
	 * 在控制器（controller方法）执行之前
	 * <p>
	 * 如：登录校验、
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 1. 获得请求参数
		String ip = request.getRemoteHost();
		String uri = request.getRequestURI();
		
		// 2. 登录校验
		if (!uri.startsWith("/open")) {
			// 获取token
//			Cookie cookie = CookieUtils.get(request, TokenConstant.COOKIE_TOKEN_KEY);
//	        String token = "";
//	        if (null == cookie) {
//	        	token = request.getHeader("token");
//	        }else {
//	        	token = cookie.getValue();
//	        }
//	        if (StringUtils.isEmpty(token)) {
//	            log.warn("【登录校验】Cookie中查不到token");
//	            R.unauthorized().response(response);
//	            return false;
//	        }
	        
	        // redis校验token
//	        String tokenValue = redis.get(String.format(TokenConstant.REDIS_TOKEN_PREFIX, token));
//	        if (StringUtils.isEmpty(tokenValue)) {
//	            log.warn("【登录校验】Redis中查不到token");
//	            R.unauthorized().response(response);
//	            return false;
//	        }
		}

		
//		UserDTO user_info = null;
		
        log.info("ip={}", ip);
		log.info("uri={}", uri);
//		log.info("user_info={}", user_info);
		log.info("method={}", request.getMethod());
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	
	/**
	 * 在控制器（controller方法）执行之后，视图渲染之前
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	/**
	 * 在请求执行完毕之后，response响应之前
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}

}
