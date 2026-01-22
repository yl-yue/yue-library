package ai.yue.library.test.webflux.controller.base.async;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 异步测试
 *
 * @author ylyue
 * @since 2020/12/14
 */
@Slf4j
@RestController
@RequestMapping("/async")
public class AsyncController {

    @Autowired
    AsyncService asyncService;

    @GetMapping("/async")
    public Result<?> async() {
//        InetSocketAddress inetSocketAddress = httpServerRequest.hostAddress();
//        log.info("1. 异步测试-开始调用异步方法，inetSocketAddress：{}", inetSocketAddress);
        asyncService.async();
        log.info("2. 异步测试-异步方法正在执行");
        return R.success();
    }

    @GetMapping("/asyncException")
    public Result<?> asyncException() {
//        InetSocketAddress inetSocketAddress = httpServerRequest.hostAddress();
//        log.info("1. 异步测试-开始调用异步方法，inetSocketAddress：{}", inetSocketAddress);
        asyncService.asyncException();
        log.info("2. 异步测试-异步方法正在执行");
        return R.success();
    }

    @GetMapping("/sync")
    public Result<?> sync() {
        log.info("1. 同步-开始调用同步方法");
        return asyncService.sync();
    }

}
