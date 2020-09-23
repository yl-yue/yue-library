package ai.yue.library.test.webflux.controller.webflux.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.yue.library.base.exception.ParamException;
import ai.yue.library.base.exception.ResultException;
import ai.yue.library.base.view.R;
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
@RequestMapping("/exception")
public class ExceptionController {

	@PostMapping("/exception")
	public Result<?> exception() {
		throw new ParamException("异常测试");
	}
	
	@PostMapping("/attack")
	public Result<?> attack() {
		return R.attack();
	}
	
	// 阻塞2秒钟
	private ResponseEntity<Result<?>> createResult() {
//		throw new ResultException("flux 错误测试");
//		try {
		List<Integer> a = new ArrayList<>();
		
			for (int i = 0; i < 10000; i++) {
				for (int j = 0; j < i; j++) {
					a.add(i);
				}
			}
//		try {
//			Thread.sleep(2100);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//			TimeUnit.SECONDS.sleep(2);
//		} catch (InterruptedException e) {
//		}
		
	    return R.errorPrompt("flux return 错误测试").castToResponseEntity();
	}
	
	// 普通的SpringMVC方法
	@GetMapping("/webMvc")
	private Result<?> webMvc() {
	    log.info("webMvc start");
	    ResponseEntity<?> result = createResult();
	    log.info("webMvc end.");
	    return (Result<?>) result.getBody();
	}
	
	// WebFlux(返回的是Mono)
	@GetMapping("/mono")
	private Mono<ResponseEntity<Result<?>>> mono() {
	    log.info("webFlux start");
//	    ResponseEntity<?> createResult = createResult();
//	    return Mono.just(createResult.castToResponseEntity());
	    Mono<ResponseEntity<Result<?>>> result = Mono.fromSupplier(() -> createResult());
	    log.info("webFlux end.");
	    return result;
	}
	
	/**
	 * Flux : 返回0-n个元素 注：需要指定MediaType
	 * 
	 * @return
	 */
	@GetMapping(value = "/flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	private Flux<Result<?>> flux() {
		Flux<Result<?>> result = Flux.fromStream(IntStream.range(1, 5).mapToObj(i -> {
			if (i == 3) {
				throw new ResultException("flux 错误测试");
			}
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
			}
			
			return R.errorPrompt("flux data--" + i);
		}));
		
		return result;
	}
	
}
