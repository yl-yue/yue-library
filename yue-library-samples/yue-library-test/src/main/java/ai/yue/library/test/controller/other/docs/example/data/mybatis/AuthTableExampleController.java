package ai.yue.library.test.controller.other.docs.example.data.mybatis;

import ai.yue.library.base.annotation.api.version.ApiVersion;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.ipo.TableExampleIPO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller示例
 *
 * @author yl-yue
 * @since 2023/1/17
 */
@AllArgsConstructor
@ApiVersion(1)
//@RestController
@RequestMapping("/auth/{version}/tableExample")
public class AuthTableExampleController {

    TableExampleService tableExampleService;

    /**
     * 插入数据
     */
    @PostMapping("/insert")
    public Result<?> insert(TableExampleIPO tableExampleIPO) {
        return tableExampleService.insert(tableExampleIPO);
    }

    /**
     * 分页
     */
    @GetMapping("/page")
    public Result<?> page(String fieldOne, String fieldTwo) {
        return tableExampleService.page(fieldOne, fieldTwo);
    }

}
