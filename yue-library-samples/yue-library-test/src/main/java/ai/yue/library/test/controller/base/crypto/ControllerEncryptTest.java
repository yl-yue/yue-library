package ai.yue.library.test.controller.base.crypto;

import ai.yue.library.base.crypto.annotation.key.exchange.RequestDecrypt;
import ai.yue.library.base.crypto.annotation.key.exchange.ResponseEncrypt;
import ai.yue.library.base.crypto.constant.key.exchange.ExchangeKeyEnum;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.ipo.TableExampleIPO;
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
    public Result<?> decrypt(@RequestBody TableExampleIPO tableExampleIPO) {
        return R.success(tableExampleIPO);
    }

    @RequestDecrypt(exchangeKeyType = ExchangeKeyEnum.SM2_SM4)
    @PostMapping("/decrypt/SM2_SM4")
    public Result<?> decryptSM2_SM4(@RequestBody TableExampleIPO tableExampleIPO) {
        return R.success(tableExampleIPO);
    }

    @ResponseEncrypt
    @GetMapping("/{encrypt}")
    public Result<?> encrypt(@PathVariable String encrypt) {
        return R.success(encrypt);
    }

    @ResponseEncrypt(exchangeKeyType = ExchangeKeyEnum.SM2_SM4)
    @GetMapping("/{encrypt}/SM2_SM4")
    public Result<?> encryptSM2_SM4(@PathVariable String encrypt) {
        return R.success(encrypt);
    }

}
