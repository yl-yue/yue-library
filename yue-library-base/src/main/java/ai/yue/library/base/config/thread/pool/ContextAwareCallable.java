package ai.yue.library.base.config.thread.pool;

import java.util.concurrent.Callable;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author  孙金川
 * @version 创建时间：2018年11月27日
 */
public class ContextAwareCallable<T> implements Callable<T> {
	
	private Callable<T> task;
	private RequestAttributes context;
	
	public ContextAwareCallable(Callable<T> task, RequestAttributes context) {
		this.task = task;
		this.context = context;
	}
	
	@Override
	public T call() throws Exception {
		if (context != null) {
			RequestContextHolder.setRequestAttributes(context);
		}

		try {
			return task.call();
		} finally {
			RequestContextHolder.resetRequestAttributes();
		}
	}
	
}