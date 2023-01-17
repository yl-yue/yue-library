package ai.yue.library.test.docs.example.data.mybatis;

import ai.yue.library.base.annotation.api.version.ApiVersion;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.ipo.TableExampleTestIPO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller示例
 *
 * @author yl-yue
 * @since 2023/1/17
 */
@AllArgsConstructor
@ApiVersion(1)
@RestController
@RequestMapping("/auth/{version}/tableExampleTest")
public class AuthTableExampleController {

    TableExampleService tableExampleTestService;

    /**
     * 插入数据
     */
    @PostMapping("/insert")
    public Result<?> insert(TableExampleTestIPO tableExampleTestIPO) {
        return tableExampleTestService.insert(tableExampleTestIPO);
    }

    /**
     * 分页
     */
    @GetMapping("/page")
    public Result<?> page(TableExampleTestIPO tableExampleTestIPO) {
        return tableExampleTestService.page(tableExampleTestIPO);
    }

}
