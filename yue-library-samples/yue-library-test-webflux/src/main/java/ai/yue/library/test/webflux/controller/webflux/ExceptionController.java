package ai.yue.library.test.webflux.controller.webflux;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.yue.library.base.exception.ParamException;
import ai.yue.library.base.exception.ResultException;
import ai.yue.library.base.view.Result;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author	ylyue
 * @since	2019年10月12日
 */
@Slf4j
@RestController
@RequestMapping("/webFlux/exception")
public class ExceptionController {

	/**
	 * exception
	 * 
	 * @return
	 */
	@PostMapping("/exception")
	public Result<?> exception() {
		throw new ParamException("异常测试");
	}
	
	// 阻塞5秒钟
	private String createStr() {
		throw new ResultException("flux 错误测试");
//	    try {
//	        TimeUnit.SECONDS.sleep(5);
//	    } catch (InterruptedException e) {
//	    }
//	    return "some string";
	}
	
	// 普通的SpringMVC方法
	@GetMapping("/webMvc")
	private String webMvc() {
	    log.info("webMvc start");
	    String result = createStr();
	    log.info("webMvc end.");
	    return result;
	}
	
	// WebFlux(返回的是Mono)
	@GetMapping("/webFlux")
	private Mono<String> webFlux() {
	    log.info("webFlux start");
	    Mono<String> result = Mono.fromSupplier(() -> createStr());
	    log.info("webFlux end.");
	    return result;
	}
	
	/**
	 * Flux : 返回0-n个元素 注：需要指定MediaType
	 * 
	 * @return
	 */
	@GetMapping(value = "/flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	private Flux<String> flux() {
		Flux<String> result = Flux.fromStream(IntStream.range(1, 5).mapToObj(i -> {
			if (i == 3) {
				throw new ResultException("flux 错误测试");
			}
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
			}
			return "flux data--" + i;
		}));
		
		return result;
	}
	
}
