package ai.yue.library.test.controller.base.crypto;

import ai.yue.library.base.annotation.ApiVersion;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.ipo.ValidationIPO;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 密钥交换加解密测试
 *
 * @author ylyue
 * @since 2021/4/14
 */
@ApiVersion(1)
@RestController
@RequestMapping("/open/{version}/keyExchange")
public class KeyExchangeController {

    @PostMapping("/keyExchangeEncryptDecrypt")
    public Result<?> keyExchangeEncryptDecrypt(@Valid ValidationIPO validationIPO) {
        System.out.println(validationIPO);
        return R.success(validationIPO);
    }

    @PostMapping("/requestBody")
    public Result<?> requestBody(@RequestBody @Valid ValidationIPO validationIPO) {
        return R.success(validationIPO);
    }

    @GetMapping("/{encrypt}")
    public Result<?> encrypt(@PathVariable String encrypt) {
        return R.success(encrypt);
    }

}
