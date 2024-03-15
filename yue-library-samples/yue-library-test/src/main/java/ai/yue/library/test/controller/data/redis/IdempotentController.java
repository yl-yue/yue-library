package ai.yue.library.test.controller.data.redis;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.redis.idempotent.ApiIdempotent;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 幂等性测试
 *
 * @author ylyue
 * @since 2021/5/28
 */
@RestController
@RequestMapping("/idempotent")
public class IdempotentController {

    /**
     * 幂等性测试
     */
    @ApiIdempotent
//    @ApiIdempotent(paramKeys = "cellphone")
    @PostMapping("/test")
    public Result<?> test(JSONObject paramJson) {
//        ThreadUtil.sleep(20000L);
//        ThreadUtil.sleep(100L);
        return R.success(paramJson);
    }

}
