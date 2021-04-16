package ai.yue.library.test.controller.base.crypto;

import ai.yue.library.base.crypto.annotation.key.exchange.RequestDecrypt;
import ai.yue.library.base.crypto.annotation.key.exchange.ResponseEncrypt;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.ipo.UserIPO;
import org.springframework.web.bind.annotation.*;

/**
 * 加密测试
 *
 * @author ylyue
 * @since 2021/4/14
 */
@RestController
@RequestMapping("/controllerEncrypt")
public class ControllerEncryptTest {

    @RequestDecrypt
    @PostMapping("/decrypt")
    public Result<?> decrypt(@RequestBody UserIPO userIPO) {
        return R.success(userIPO);
    }

    @ResponseEncrypt
    @GetMapping("/{encrypt}")
    public Result<?> encrypt(@PathVariable String encrypt) {
        return R.success(encrypt);
    }

}
