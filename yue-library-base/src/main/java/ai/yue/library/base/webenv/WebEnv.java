package ai.yue.library.base.webenv;

import ai.yue.library.base.util.ParamUtils;
import ai.yue.library.base.view.Result;
import com.alibaba.fastjson2.JSONObject;

/**
 * <b>Web环境实现</b>
 * <p>如：WebMvc、WebFlux
 * 
 * @author	ylyue
 * @since	2020年4月16日
 */
public interface WebEnv {
	
	// Result
	
	/**
	 * {@link Result#response()}
	 * @param result
	 */
    void resultResponse(Result<?> result);
	
	/**
	 * 将Result写入指定的原生响应对象中（如 Servlet 的 HttpServletResponse、WebFlux 的 ServerHttpResponse），不依赖 RequestContextHolder ThreadLocal。
	 * <p>适用于 Servlet Filter 等 ThreadLocal 未就绪的场景。
	 * <p>默认委托给 {@link #resultResponse(Result)}}，由各 Web 环境实现覆写以支持直接写入。
	 *
	 * @param nativeResponse 原生响应对象（Servlet 环境为 {@code HttpServletResponse}，WebFlux 环境为 {@code ServerHttpResponse}）
	 * @param result         响应结果
	 */
	default void resultResponse(Object nativeResponse, Result<?> result) {
		resultResponse(result);
	}
	
	// ParamUtils
	
	/**
	 * {@link ParamUtils#getParam()}
	 * 
	 * @return JSON对象
	 */
    JSONObject getParam();
	
	/**
	 * {@link ParamUtils#getParam(Class)}
	 * 
	 * @param <T> 泛型
	 * @param clazz 想要获取的参数类型
	 * @return 想要的对象实例
	 */
    <T> T getParam(Class<T> clazz);
	
}
