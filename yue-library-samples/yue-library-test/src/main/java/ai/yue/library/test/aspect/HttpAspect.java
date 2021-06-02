package ai.yue.library.template.simple.aspect;

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
 * HTTP请求日志
 *
 * @author	ylyue
 * @since	2019年9月25日
 */
@Slf4j
@Aspect
@Component
public class HttpAspect {
	
	@Autowired
	HttpServletRequest request;
	
	@Pointcut(ServletUtils.POINTCUT)
	public void pointcut() {}
	
	@Before("pointcut()")
	public void doVerifyBefore(JoinPoint joinPoint) {
		// 1. 获取request接口信息
		String requestIp = request.getRemoteHost();
		String requestUri = request.getRequestURI();
		String requestMethod = request.getMethod();

		// 2. 获取controller方法信息
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		String requestHandlerMethod = methodSignature.getDeclaringTypeName() + "." + methodSignature.getName() + "()";

		// 3. 打印日志
        log.info("requestIp={}", requestIp);
		log.info("requestUri={}", requestUri);
		log.info("requestMethod={}", requestMethod);
		log.info("requestHandlerMethod={}", requestHandlerMethod);
	}
	
}
