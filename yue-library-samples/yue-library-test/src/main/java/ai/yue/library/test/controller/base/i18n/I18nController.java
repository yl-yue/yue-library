package ai.yue.library.test.controller.base.i18n;

import ai.yue.library.base.annotation.ApiVersion;
import ai.yue.library.base.util.AsyncUtils;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.constant.I18nCode;
import ai.yue.library.test.constant.I18nMsgConstant;
import ai.yue.library.test.ipo.ValidationI18nIPO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;

/**
 * 国际化测试
 *
 * @author yl-yue
 * @since 2023/2/17
 */
@Slf4j
@ApiVersion(1)
@RequestMapping("/open/{version}/i18n")
@RestController
@RequiredArgsConstructor
public class I18nController {

    final I18nAsyncService i18nAsyncService;

    @GetMapping("/getSuccess")
    public Result<?> getSuccess() {
        return R.success();
    }

    @GetMapping("/getErrorPrompt")
    public Result<?> getErrorPrompt() {
        return R.errorPromptI18n(I18nMsgConstant.TEST_MSG);
    }

    @GetMapping("/errorPromptCodeI18n")
    public Result<?> errorPromptCodeI18n() {
        return R.errorPromptCodeI18n(I18nCode.TEST_MSG_ARGS, "ARGS");
    }

    @GetMapping("/getNotI18n")
    public Result<?> getNotI18n() {
        Result<?> result = R.errorPrompt("不使用国际化定义");
        System.out.println(result.toJSONString());
        return result;
    }

    @PostMapping("/actValidationI18n")
    public Result<?> actValidationI18n(@Validated ValidationI18nIPO validationI18nIPO) {
        return R.success(validationI18nIPO);
    }

    @SneakyThrows
    @PostMapping("/async")
    public Result<?> async() {
        Future<Result<?>> async = i18nAsyncService.async();
        log.info("1. 异步测试-开始调用异步方法");
        Result<?> result = async.get();
        log.info("5. {}", result.getMsg());
        return result;
    }

    @SneakyThrows
    @PostMapping("/asyncUtils")
    public Result<?> asyncUtils() {
        Future<Result<?>> async = AsyncUtils.execAsync(() -> i18nAsyncService.asyncUtils());
        log.info("1. 异步测试-开始调用异步方法");
        Result<?> result = async.get();
        log.info("5. {}", result.getMsg());
        return result;
    }

}
