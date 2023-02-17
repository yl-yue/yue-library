package ai.yue.library.test.controller.base.i18n;

import ai.yue.library.base.annotation.api.version.ApiVersion;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.constant.I18nMsgConstant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 国际化测试
 *
 * @author yl-yue
 * @since 2023/2/17
 */
@ApiVersion(1)
@RequestMapping("/open/{version}/i18n")
@RestController
public class I18nController {

    @GetMapping("/getSuccess")
    public Result<?> getSuccess() {
        return R.success();
    }

    @GetMapping("/getErrorPrompt")
    public Result<?> getErrorPrompt() {
        return R.errorPrompt(I18nMsgConstant.TEST_MSG);
    }

    @GetMapping("/getNotI18n")
    public Result<?> getNotI18n() {
        Result<?> result = R.errorPrompt("不使用国际化定义");
        System.out.println(result.castToJSONString());
        return result;
    }

}
