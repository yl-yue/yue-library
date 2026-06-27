package ai.yue.library.webflux.config.exception;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局统一异常兜底处理
 * <p>仅拦截未被 {@link ResultExceptionHandler} 精细处理及其它业务自定义 {@code @ControllerAdvice} 处理的剩余异常。
 * <p>使用最低优先级 {@link Ordered#LOWEST_PRECEDENCE}，确保业务自定义异常拦截优先于此兜底生效，
 * 同时不抢占 {@link ResultExceptionHandler}（最高优先级）已处理的异常。
 *
 * @author	ylyue
 * @since	2026年6月27日
 */
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
@ControllerAdvice
public class ResultExceptionFallbackHandler {

	/**
	 * 拦截所有未知异常
	 */
	@ResponseBody
	@ExceptionHandler(Exception.class)
	public Result<?> exception(Exception e) {
		return R.getResult(e);
	}

}
