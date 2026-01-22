package ai.yue.library.test.controller.other.gitee.I2ALJW;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.v7.core.xml.XmlUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author ylyue
 * @since 2021/1/20
 */
@RestController
@RequestMapping("/wms/api/qimen")
@Slf4j
public class ReturnReceiptController {

    /**
     * 入库回调确认
     */
    @ResponseBody
    @PostMapping(params = "method=entryorder.confirm",produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_XML_VALUE)
    public void entryOrderConfirm(@Valid QimenApiRequest request, @RequestBody String xml) {
        System.out.println(request);
        log.info("入库单回调参数:" + xml);
        Map<String, Object> params = XmlUtil.xmlToMap(xml);
        System.out.println(params);
        // 这里返回xml
        // QimenApiResponse response = xxxService.doSth(params);
//        QimenApiResponse response = ...;
//        return response;
    }

}
