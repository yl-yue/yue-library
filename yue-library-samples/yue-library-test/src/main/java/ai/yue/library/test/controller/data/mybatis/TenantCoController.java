package ai.yue.library.test.controller.data.mybatis;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.entity.TableExampleStandard;
import ai.yue.library.test.service.TableExampleStandardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 逻辑删除测试
 *
 * @author	ylyue
 * @since	2020年2月21日
 */
@RestController
@RequestMapping("/data/mybatis/tenantCo")
public class TenantCoController {

	@Autowired
    TableExampleStandardService tableExampleStandardService;

    @PostMapping("/testDefault")
    public Result<?> testDefault(TableExampleStandard tableExampleStandard) {
        tableExampleStandardService.insert(tableExampleStandard);
        tableExampleStandard.setFieldThree("testDefault");
        tableExampleStandardService.updateById(tableExampleStandard);
        return R.success(tableExampleStandardService.getById(tableExampleStandard.getId()));
    }

}
