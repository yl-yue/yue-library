package ai.yue.library.test.controller.base.async;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.web.util.ServletUtils;
import com.alibaba.fastjson.JSONObject;
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
    public Result<?> async(JSONObject paramJson) {
        String asyncContext = ServletUtils.getRequest().getHeader("asyncContext");
        log.info("1. 异步测试-开始调用异步方法，asyncContext：{}", asyncContext);
//        ServletUtils.getRequest().setAttribute(AsyncProperties.SERVLET_ASYNC_CONTEXT_TIMEOUT_MILLIS,1);
        asyncService.async(paramJson);
        log.info("2. 异步测试-异步方法正在执行");
        return R.success();
    }

    @GetMapping("/sync")
    public Result<?> sync() {
        log.info("1. 同步-开始调用同步方法");
        return asyncService.sync();
    }

}
