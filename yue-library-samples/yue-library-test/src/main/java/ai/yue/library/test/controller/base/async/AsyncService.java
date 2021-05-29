package ai.yue.library.test.controller.base.async;

import ai.yue.library.base.exception.ResultException;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.jdbc.ipo.PageIPO;
import ai.yue.library.data.jdbc.vo.PageVO;
import ai.yue.library.test.controller.data.jdbc.query.map.PersonDAO;
import ai.yue.library.test.controller.data.jdbc.query.map.PersonDO;
import ai.yue.library.web.util.ServletUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    PersonDAO personDAO;

    @Async
    public void async(JSONObject paramJson) {
        HttpServletRequest request = ServletUtils.getRequest();
        log.info("request: {}", request);
        log.info("3. asyncContext: {}", request.getHeader("asyncContext"));
        PageVO<PersonDO> pageTVO = personDAO.page(PageIPO.parsePageIPO(paramJson));
        log.info("4. 异步测试-异步方法执行完毕，分页数据大小：{}", pageTVO.getCount());
        HttpServletRequest request2 = ServletUtils.getRequest();
        log.info("request2: {}", request2);
        log.info("5. asyncContext: {}", request2.getHeader("asyncContext"));
    }

    @Async
    public void asyncException(JSONObject paramJson) {
        async(paramJson);
        throw new ResultException("异步异常测试");
    }

    public Result<List<PersonDO>> sync() {
        PageIPO pageIPO = PageIPO.builder().page(1).limit(10).build();
        PageVO<PersonDO> pageTVO = personDAO.page(pageIPO);
        log.info("2. 同步-同步方法执行完毕，分页数据大小：{}", pageTVO.getCount());
        String syncContext = ServletUtils.getRequest().getHeader("syncContext");
        log.info("3. 同步-syncContext: {}", syncContext);
        return pageTVO.toResult();
    }

}
