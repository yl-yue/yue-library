package ai.yue.library.test.controller.data.mybatis;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.ipo.ValidationGroups;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.entity.TableExampleStandard;
import ai.yue.library.test.ipo.TableExampleIPO;
import ai.yue.library.test.service.TableExampleStandardService;
import ai.yue.library.web.util.ServletUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author	ylyue
 * @since	2020年2月21日
 */
@RestController
@RequestMapping("/data/mybatis/tableExampleTest")
public class TableExampleStandardController {

	@Autowired
    TableExampleStandardService tableExampleService;

    @PostMapping("/insert")
    public Result<?> insert(TableExampleIPO tableExampleTestIPO) {
        return tableExampleService.insert(tableExampleTestIPO);
    }

    @DeleteMapping("/deleteById")
    public Result<?> deleteById(long id) {
        boolean b = tableExampleService.removeById(id);
        return R.success(b);
    }

    @PutMapping("/updateById")
    public Result<?> updateById(@Validated(ValidationGroups.Update.class) TableExampleIPO tableExampleTestIPO) {
        boolean b = tableExampleService.updateById(Convert.toJavaBean(tableExampleTestIPO, TableExampleStandard.class));
        return R.success(b);
    }

    @GetMapping("/page")
    public Result<?> page(TableExampleIPO tableExampleTestIPO) {
        PageHelper.startPage(ServletUtils.getRequest());
        List<TableExampleStandard> list = tableExampleService.list(Wrappers.query(Convert.toJavaBean(tableExampleTestIPO, TableExampleStandard.class)));
        return R.success(PageInfo.of(list));
    }

}
