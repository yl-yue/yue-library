package ai.yue.library.base.webenv;

import ai.yue.library.base.view.Result;

/**
 * <b>Web环境实现</b>
 * <p>如：WebMvc、WebFlux
 * 
 * @author	ylyue
 * @since	2020年4月16日
 */
public interface WebEnv {
	
	/**
	 * {@link Result#response()}
	 * @param result
	 */
	public void resultResponse(Result<?> result);
	
}
