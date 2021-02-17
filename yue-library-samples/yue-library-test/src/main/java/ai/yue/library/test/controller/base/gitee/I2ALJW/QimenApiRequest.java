package ai.yue.library.test.controller.base.gitee.I2ALJW;

import cn.hutool.core.date.DateUtil;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author ylyue
 * @since 2021/1/20
 */
@Data
public class QimenApiRequest {

    @NotBlank
    private String method;
    @NotBlank
    private String format = "xml";
    @NotBlank
    private String app_key = "wms";
    @NotBlank
    private String v = "1.0";
    @NotBlank
    private String sign;
    @NotBlank
    private String sign_method = "md5";
    @NotBlank
    private String customerId = "wms";
    @NotBlank
    private String timestamp = DateUtil.now();

}
