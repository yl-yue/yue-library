package ai.yue.library.base.webenv;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.util.ParamUtils;
import ai.yue.library.base.view.Result;

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
	public void resultResponse(Result<?> result);
	
	// ParamUtils
	
	/**
	 * {@link ParamUtils#getParam()}
	 * 
	 * @return JSON对象
	 */
	public JSONObject getParam();
	
	/**
	 * {@link ParamUtils#getParam(Class)}
	 * 
	 * @param <T> 泛型
	 * @param clazz 想要获取的参数类型
	 * @return 想要的对象实例
	 */
	public <T> T getParam(Class<T> clazz);
	
}
