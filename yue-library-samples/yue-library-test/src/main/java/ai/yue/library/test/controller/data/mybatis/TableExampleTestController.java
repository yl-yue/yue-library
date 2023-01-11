package ai.yue.library.test.controller.data.mybatis;

import ai.yue.library.base.ipo.ValidationGroups;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.ipo.TableExampleTestIPO;
import ai.yue.library.test.service.TableExampleTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author	ylyue
 * @since	2020年2月21日
 */
@RestController
@RequestMapping("/data/mybatis/tableExampleTest")
public class TableExampleTestController {

	@Autowired
    TableExampleTestService tableExampleTestService;

    @PostMapping("/insert")
    public Result<?> insert(TableExampleTestIPO tableExampleTestIPO) {
        return tableExampleTestService.insert(tableExampleTestIPO);
    }

    @DeleteMapping("/deleteById")
    public Result<?> deleteById(long id) {
        return tableExampleTestService.deleteById(id);
    }

    @PutMapping("/updateById")
    public Result<?> updateById(@Validated(ValidationGroups.Update.class) TableExampleTestIPO tableExampleTestIPO) {
        return tableExampleTestService.updateById(tableExampleTestIPO);
    }

    @GetMapping("/page")
    public Result<?> page(TableExampleTestIPO tableExampleTestIPO) {
        return tableExampleTestService.page(tableExampleTestIPO);
    }

}
