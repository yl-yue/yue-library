package ai.yue.library.base.config.thread.pool;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author	ylyue
 * @since	2018年11月27日
 */
public class ContextAwareAsyncExecutor extends ThreadPoolTaskExecutor {
	
	private static final long serialVersionUID = -946431910133805893L;
	
	@Override
    public <T> Future<T> submit(Callable<T> task) {
        return super.submit(new ContextAwareCallable<T>(task, RequestContextHolder.currentRequestAttributes()));
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        return super.submitListenable(new ContextAwareCallable<T>(task, RequestContextHolder.currentRequestAttributes()));
    }
    
}