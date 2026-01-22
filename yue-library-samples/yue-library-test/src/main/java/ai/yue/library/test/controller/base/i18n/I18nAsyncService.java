package ai.yue.library.test.controller.base.i18n;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.constant.I18nCode;
import ai.yue.library.web.util.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.concurrent.Future;

@Slf4j
@Service
public class I18nAsyncService {

    @Async
    public Future<Result<?>> async() {
        HttpServletRequest request = ServletUtils.getRequest();
        Locale locale = LocaleContextHolder.getLocale();
        log.info("2. Accept-Language: {}", request.getHeader("Accept-Language"));
        log.info("3. locale: {}", locale);
//        Result<?> result = R.errorPromptI18n(I18nMsgConstant.TEST_MSG);
        Result<?> result = R.errorPromptCodeI18n(I18nCode.TEST_MSG);
        log.info("4. {}", result.getMsg());
        return new AsyncResult<>(result);
    }

    public Result<?> asyncUtils() {
        HttpServletRequest request = ServletUtils.getRequest();
        Locale locale = LocaleContextHolder.getLocale();
        log.info("2. Accept-Language: {}", request.getHeader("Accept-Language"));
        log.info("3. locale: {}", locale);
//        Result<?> result = R.errorPromptI18n(I18nMsgConstant.TEST_MSG);
        Result<?> result = R.errorPromptCodeI18n(I18nCode.TEST_MSG);
        log.info("4. {}", result.getMsg());
        return result;
    }

}
