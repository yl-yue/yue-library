package ai.yue.library.template.aspect;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties.Redis;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

/**
 * @author  孙金川
 * @version 创建时间：2017年10月14日
 */
@Slf4j
@Aspect
@Component
public class HttpAspect {
	
	@Autowired
	Redis redis;
	
	@Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public void requestMapping() {}
	@Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
	public void getMapping() {}
	@Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
	public void postMapping() {}
	@Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
	public void putMapping() {}
	@Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
	public void deleteMapping() {}
	
	@Pointcut("requestMapping() || getMapping() || postMapping() || putMapping() || deleteMapping()")
	public void pointcut() {}
	
	@Before("pointcut()")
	public void doVerifyBefore(JoinPoint joinPoint) {
		// 1. 获得请求上下文
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		
        // 2. 参数校验
        Signature signature = joinPoint.getSignature();
        
        // 3. 开发环境-打印日志
		String ip = request.getRemoteHost();
		String uri = request.getRequestURI();
//		UserDTO user_info = null;
//		if (!uri.startsWith("/open")) {
//			user_info = UserUtils.getUser(request, redis, UserDTO.class);
//		}
		
        log.info("ip={}", ip);
		log.info("uri={}", uri);
//		log.info("user_info={}", user_info);
		log.info("method={}", request.getMethod());
		log.info("class_method={}", signature.getDeclaringTypeName() + "." + signature.getName() + "()");
	}
	
}
