package ai.yue.library.web.config.argument.resolver;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * 配置-自定义顺序方法参数解析器
 * 
 * @author	ylyue
 * @since	2019年8月2日
 */
@Configuration
public class CustomArgumentResolversConfig {

	@Autowired
	public void prioritizeCustomMethodArgumentHandlers(RequestMappingHandlerAdapter adapter) {
		List<HandlerMethodArgumentResolver> prioritizeCustomArgumentResolvers = new ArrayList<>();
		prioritizeCustomArgumentResolvers.add(new JSONObjectArgumentResolver());
		prioritizeCustomArgumentResolvers.addAll(adapter.getArgumentResolvers());
		adapter.setArgumentResolvers(prioritizeCustomArgumentResolvers);
	}
    
}
