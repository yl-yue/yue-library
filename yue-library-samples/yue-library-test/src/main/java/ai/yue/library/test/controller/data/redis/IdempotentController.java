package ai.yue.library.test.controller.data.redis;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.redis.annotation.Idempotent;
import cn.hutool.v7.core.thread.ThreadUtil;
import com.alibaba.fastjson2.JSONObject;
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
    @Idempotent(paramKeys = "cellphone")
    @PostMapping("/test")
    public Result<?> test(JSONObject paramJson) {
        ThreadUtil.sleep(3000L);
        System.out.println(paramJson.getString("cellphone"));
        return R.success(paramJson);
    }

}
