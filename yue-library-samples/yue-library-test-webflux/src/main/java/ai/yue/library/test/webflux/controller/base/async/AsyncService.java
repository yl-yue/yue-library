package ai.yue.library.test.webflux.controller.base.async;

import ai.yue.library.base.exception.ResultException;
import ai.yue.library.base.util.ThreadUtils;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 异步
 *
 * @author ylyue
 * @since 2020/12/14
 */
@Slf4j
@Service
public class AsyncService {

    @Async
    public void async() {
//        log.info("3. asyncContext: {}", request.getHeader("asyncContext"));
//        PageHelper.startPage(ServletUtils.getRequest());
//        List<TableExampleStandard> list = tableExampleStandardService.list();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("jsonObject", 666666);
        ThreadUtils.sleep(2000);

        log.info("4. 异步测试-异步方法执行完毕，jsonObject", jsonObject);
        log.info("5. jsonObject: {}", jsonObject);
    }

    @Async
    public void asyncException() {
        async();
        throw new ResultException("异步异常测试");
    }

    public Result<?> sync() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("jsonObject", 666666);
        ThreadUtils.sleep(2000);
        log.info("2. 同步-同步方法执行完毕，jsonObject：{}", jsonObject);
        return R.success(jsonObject);
    }

}
