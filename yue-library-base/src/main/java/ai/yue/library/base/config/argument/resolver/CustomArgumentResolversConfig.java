package ai.yue.library.base.config.argument.resolver;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * @author	ylyue
 * @since	2019年8月2日
 */
@Configuration
public class CustomArgumentResolversConfig {

	@Autowired
	public void prioritizeCustomMethodArgumentHandlers(RequestMappingHandlerAdapter adapter) {
		List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();
		argumentResolvers.add(new JSONObjectArgumentResolver());
		argumentResolvers.addAll(adapter.getArgumentResolvers());
		argumentResolvers.addAll(adapter.getCustomArgumentResolvers());
		adapter.setArgumentResolvers(argumentResolvers);
	}
    
}
