package ai.yue.library.test.controller.base.async;

import ai.yue.library.base.exception.ResultException;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.entity.TableExampleStandard;
import ai.yue.library.test.service.TableExampleStandardService;
import ai.yue.library.web.util.ServletUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 异步
 *
 * @author ylyue
 * @since 2020/12/14
 */
@Slf4j
@Service
public class AsyncService {

    @Autowired
    TableExampleStandardService tableExampleStandardService;

    @Async
    public void async() {
        HttpServletRequest request = ServletUtils.getRequest();
        log.info("request: {}", request);
        log.info("3. asyncContext: {}", request.getHeader("asyncContext"));
        PageHelper.startPage(ServletUtils.getRequest());
        List<TableExampleStandard> list = tableExampleStandardService.list();
        log.info("4. 异步测试-异步方法执行完毕，分页数据大小：{}", PageInfo.of(list).getTotal());
        HttpServletRequest request2 = ServletUtils.getRequest();
        log.info("request2: {}", request2);
        log.info("5. asyncContext: {}", request2.getHeader("asyncContext"));
        log.info("6. asyncMDC: {}", MDC.getCopyOfContextMap());
    }

    @Async
//    public Future<Result<?>> asyncFuture() {
    public CompletableFuture<Result<?>> asyncFuture() {
        HttpServletRequest request = ServletUtils.getRequest();
        log.info("request: {}", request);
        log.info("3. asyncContext: {}", request.getHeader("asyncContext"));
        PageHelper.startPage(ServletUtils.getRequest());
        List<TableExampleStandard> list = tableExampleStandardService.list();
        log.info("4. 异步测试-异步方法执行完毕，分页数据大小：{}", PageInfo.of(list).getTotal());
        HttpServletRequest request2 = ServletUtils.getRequest();
        log.info("request2: {}", request2);
        log.info("5. asyncContext: {}", request2.getHeader("asyncContext"));
//        return new AsyncResult<>(R.success());
        return CompletableFuture.completedFuture(R.success());
    }

    @Async
    public void asyncException() {
        async();
        throw new ResultException("异步异常测试");
    }

    public Result<?> sync() {
        List<TableExampleStandard> list = tableExampleStandardService.list();
        PageInfo<TableExampleStandard> pageInfo = PageInfo.of(list);
        log.info("2. 同步-同步方法执行完毕，分页数据大小：{}", pageInfo.getTotal());
        String syncContext = ServletUtils.getRequest().getHeader("syncContext");
        log.info("3. 同步-syncContext: {}", syncContext);
        return R.success(pageInfo);
    }

    public Result<?> sync2() {
        List<TableExampleStandard> list = tableExampleStandardService.list();
        PageInfo<TableExampleStandard> pageInfo = PageInfo.of(list);
        log.info("2. 同步-同步方法执行完毕，分页数据大小：{}", pageInfo.getTotal());
        String header = ServletUtils.getRequest().getHeader("random");
        log.info("3. 同步-header: {}", header);
        return R.success(pageInfo);
    }

}
