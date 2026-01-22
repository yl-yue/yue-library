package ai.yue.library.test.controller.base.crypto;

import ai.yue.library.base.annotation.ApiVersion;
import ai.yue.library.base.crypto.annotation.RequestDecrypt;
import ai.yue.library.base.crypto.annotation.ResponseEncrypt;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.ipo.ValidationIPO;
import ai.yue.library.web.util.ServletUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Annotation加解密测试
 *
 * @author ylyue
 * @since 2021/4/14
 */
@ApiVersion(1)
@RestController
@RequestMapping("/open/{version}/cryptoAnnotation")
public class CryptoAnnotationController {

    @RequestDecrypt(symmetricCryptoFactoryBeanName = "aesDecryptEncryptFactory")
    @ResponseEncrypt(symmetricCryptoFactoryBeanName = "aesDecryptEncryptFactory")
    @PostMapping("/aesEncryptDecrypt")
    public Result<?> aesEncryptDecrypt(@RequestBody ValidationIPO validationIPO) {
        System.out.println(validationIPO);
        return R.success(validationIPO);
    }

    @RequestDecrypt(symmetricCryptoFactoryBeanName = "sm4DecryptEncryptFactory")
    @ResponseEncrypt(symmetricCryptoFactoryBeanName = "sm4DecryptEncryptFactory")
    @PostMapping("/sm4EncryptDecrypt")
    public Result<?> sm4EncryptDecrypt(@RequestBody ValidationIPO validationIPO) {
        System.out.println(validationIPO);
        return R.success(validationIPO);
    }

    @RequestDecrypt(symmetricCryptoFactoryBeanName = "aesDecryptEncryptFactory", decryptHeaderNames = {"header1", "header2"})
    @PostMapping("/headerDecrypt")
    public Result<?> headerDecrypt(@RequestBody ValidationIPO validationIPO, HttpServletRequest request) {
        System.out.println(validationIPO);
        System.out.println(ServletUtils.getHeaderMap(request));
        return R.success(validationIPO);
    }

    @RequestDecrypt(symmetricCryptoFactoryBeanName = "aesDecryptEncryptFactory", decryptParamNames = {"name", "cellphone"})
    @PostMapping("/paramDecrypt")
    public Result<?> paramDecrypt(@RequestBody ValidationIPO validationIPO) {
        System.out.println(validationIPO);
        return R.success(validationIPO);
    }

}
