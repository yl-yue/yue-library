package ai.yue.library.template.boot.aspect;

import ai.yue.library.auth.service.client.User;
import ai.yue.library.web.util.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author	ylyue
 * @since	2019年9月25日
 */
@Slf4j
@Aspect
@Component
public class HttpAspect {
	
	@Autowired
	User user;
	@Autowired
	HttpServletRequest request;
	
	@Pointcut(ServletUtils.POINTCUT)
	public void pointcut() {}
	
	@Before("pointcut()")
	public void doVerifyBefore(JoinPoint joinPoint) {
		// 1. 登录校验
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		String uri = request.getRequestURI();
        Long user_id = null;
		if (!uri.startsWith("/open") && !uri.equals("/")) {
			user_id = user.getUserId();
		}
		
		// 2. 开发环境-打印日志
		String ip = request.getRemoteHost();
        log.info("ip={}", ip);
		log.info("uri={}", uri);
		log.info("user_id={}", user_id);
		log.info("method={}", request.getMethod());
		log.info("class_method={}", methodSignature.getDeclaringTypeName() + "." + methodSignature.getName() + "()");
	}
	
}
