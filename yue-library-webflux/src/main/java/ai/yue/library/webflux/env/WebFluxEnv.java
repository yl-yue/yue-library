package ai.yue.library.webflux.env;

import org.springframework.stereotype.Component;

import ai.yue.library.base.view.Result;
import ai.yue.library.base.webenv.WebEnv;
import lombok.extern.slf4j.Slf4j;

/**
 * @author	ylyue
 * @since	2020年4月16日
 */
@Slf4j
@Component
public class WebFluxEnv implements WebEnv {

	@Override
	public void resultResponse(Result<?> result) {
		log.error("无效的Result.response()方法，webflux暂未实现。");
//		ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).syncBody(result);
	}
	
}
