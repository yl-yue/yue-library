package ai.yue.library.test.remove.redis.controller.web.exception;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.stereotype.Component;

/**
 * @author	ylyue
 * @since	2019年10月12日
 */
@Component
public class FilterExceptionFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
//		throw new ResultException("Filter抛出异常拦截测试");
		chain.doFilter(request, response);
	}
	
}
