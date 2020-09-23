package ai.yue.library.test.webflux.controller.webflux;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.R;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author	ylyue
 * @since	2020年4月16日
 */
@Slf4j
@RestController
@RequestMapping("/webFlux")
public class WebFluxController {

	// 阻塞5秒钟
	private String createStr() {
	    try {
	        TimeUnit.SECONDS.sleep(5);
	    } catch (InterruptedException e) {
	    }
	    return "some string";
	}
	
	// 普通的SpringMVC方法
	@GetMapping("/webMvc")
	private String webMvc() {
	    log.info("webMvc start");
	    String result = createStr();
	    log.info("webMvc end.");
	    return result;
	}
	
	// 普通的SpringMVC方法
	@GetMapping("/webMvc2")
	private Result<?> webMvc2() {
	    log.info("webMvc start");
//	    String result = createStr();
	    log.info("webMvc end.");
//	    return result;
	    return R.clientFallback();
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
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
			}
			return "flux data--" + i;
		}));
		return result;
	}
	
}
