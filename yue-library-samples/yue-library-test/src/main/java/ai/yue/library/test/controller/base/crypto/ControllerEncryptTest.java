package ai.yue.library.test.controller.base.crypto;

import ai.yue.library.base.crypto.annotation.key.exchange.ResponseEncrypt;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 加密测试
 *
 * @author ylyue
 * @since 2021/4/14
 */
@RestController
@RequestMapping("/controllerEncrypt")
public class ControllerEncryptTest {

    @ResponseEncrypt
    @GetMapping("/{encrypt}")
    public Result<?> encrypt(@PathVariable String encrypt) {
        return R.success(encrypt);
    }

}
